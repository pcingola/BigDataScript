package org.bds.mesos;

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.mesos.Protos.ExecutorID;
import org.apache.mesos.Protos.ExecutorInfo;
import org.apache.mesos.Protos.FrameworkID;
import org.apache.mesos.Protos.MasterInfo;
import org.apache.mesos.Protos.Offer;
import org.apache.mesos.Protos.OfferID;
import org.apache.mesos.Protos.Resource;
import org.apache.mesos.Protos.SlaveID;
import org.apache.mesos.Protos.TaskID;
import org.apache.mesos.Protos.TaskInfo;
import org.apache.mesos.Protos.TaskStatus;
import org.apache.mesos.Protos.Value;
import org.apache.mesos.Scheduler;
import org.apache.mesos.SchedulerDriver;
import org.bds.cluster.ComputerSystem;
import org.bds.cluster.host.Host;
import org.bds.cluster.host.HostInifinte;
import org.bds.cluster.host.HostResources;
import org.bds.executioner.Executioner;
import org.bds.executioner.ExecutionerMesos;
import org.bds.task.Task;
import org.bds.task.TaskState;
import org.bds.util.Gpr;

import com.google.protobuf.ByteString;

/**
 * BDS scheduler for Mesos
 *
 * @author pcingola
 */
public class BdsMesosScheduler implements Scheduler {

	static final long MB = 1024 * 1024;
	static final long GB = 1024 * MB;
	public static final String OFFER_CPUS = "cpus";
	public static final String OFFER_MEM = "mem";

	private final ExecutorInfo executor;
	protected boolean verbose = false;
	protected ComputerSystem cluster;
	protected HashMap<String, Task> taskById;
	protected ExecutionerMesos executionerMesos;
	protected Map<String, Set<Offer>> offersByHost;
	protected Map<String, Offer> offersById;
	protected Set<Task> taskToLaunch;

	public BdsMesosScheduler(ExecutionerMesos executionerMesos, ExecutorInfo executor) {
		this.executionerMesos = executionerMesos;
		this.executor = executor;
		cluster = executionerMesos.getSystem();
		taskById = new HashMap<>();
		taskToLaunch = new HashSet<>();
		offersByHost = new HashMap<>();
		offersById = new HashMap<>();
	}

	/**
	 * Add resource offered
	 */
	public synchronized void add(Offer offer) {
		// Parse offer
		String hostName = offer.getHostname();
		String offerId = offer.getId().getValue();
		HostResources offerResources = parseOffer(offer);
		if (verbose) Gpr.debug("Adding offer [" + offerId + "]:" + hostName + "\t" + offerResources);

		// Update resources by host
		Host host = cluster.getHost(hostName);
		if (host == null) {
			// No host with that name? Create (and add to cluster)
			host = new Host(cluster, hostName);
			host.getResources().set(offerResources);
		} else {
			host.getResources().add(offerResources);
			host.updateResourcesAvailable();
		}

		// Update offers by host
		Set<Offer> offers = offersByHost.get(hostName);
		if (offers == null) {
			offers = new HashSet<>();
			offersByHost.put(hostName, offers);
		}
		offers.add(offer);

		// Update offer by Id
		offersById.put(offerId, offer);
	}

	/**
	 * Add a task to be launched
	 */
	public synchronized void add(Task task) {
		taskToLaunch.add(task);
	}

	/**
	 * Decline all pending offers
	 */
	void declineAllOffers(SchedulerDriver driver) {
		if (offersById.isEmpty()) return;
		if (verbose) Gpr.debug("No tasks to run: Declining all remaining offers");

		// Create a new collection to avoid 'concurrent modification'
		List<Offer> offers = new LinkedList<>();
		offers.addAll(offersById.values());

		// Decline all
		for (Offer offer : offers) {
			if (verbose) Gpr.debug("Declining offer: " + offer.getId());
			driver.declineOffer(offer.getId());
		}

	}

	/**
	 * Invoked when the scheduler becomes "disconnected" from the master
	 * (e.g., the master fails and another is taking over).
	 */
	@Override
	public void disconnected(SchedulerDriver driver) {
		if (verbose) Gpr.debug("Scheduler: Disconnected");
	}

	/**
	 * Invoked when there is an unrecoverable error in the scheduler or
	 * scheduler driver. The driver will be aborted BEFORE invoking this
	 * callback.
	 */
	@Override
	public void error(SchedulerDriver driver, String message) {
		if (verbose) Gpr.debug("Scheduler: Error '" + message + "'");
	}

	/**
	 * Invoked when an executor has exited/terminated. Note that any
	 * tasks running will have TASK_LOST status updates automagically
	 * generated.
	 */
	@Override
	public void executorLost(SchedulerDriver driver, ExecutorID executorId, SlaveID slaveId, int status) {
		if (verbose) Gpr.debug("Scheduler: Executor lost " + executorId.getValue());
	}

	/**
	 * Invoked when an executor sends a message. These messages are best
	 * effort; do not expect a framework message to be retransmitted in
	 * any reliable fashion.
	 */
	@Override
	public void frameworkMessage(SchedulerDriver driver, ExecutorID executorId, SlaveID slaveId, byte[] data) {
		if (verbose) Gpr.debug("Scheduler: Framework message" //
				+ "\n\tExecutorId : " + executorId.getValue() //
				+ "\n\tSlaveId    : " + slaveId.getValue() //
				+ "\n\tMesssage   : '" + new String(data) + "'" //
		);
	}

	protected boolean matchTask(Task task, Collection<OfferID> offerIds, Collection<TaskInfo> taskInfos) {
		for (Host host : cluster) {
			if (host instanceof HostInifinte) {
				// This represents the head node (skip)
			} else if (matchTask(task, host, offerIds, taskInfos)) {
				// Can this task be run on this host?
				return true;
			}
		}

		return false;
	}

	/**
	 * Match a task to the offer/s
	 */
	protected synchronized boolean matchTask(Task task, Host host, Collection<OfferID> offerIds, Collection<TaskInfo> taskInfos) {
		// Not enough resources in this host?
		if (!host.hasResourcesAvailable(task.getResources())) return false;

		if (verbose) Gpr.debug("Matching task: " + task.getId() + "\t resources: " + task.getResources() + "\thost:" + host.getHostName() + ", resources: " + host.getResourcesAvaialble());

		// OK, we should be able to run 'task' in hostName
		String hostName = host.toString();
		Set<Offer> offers = offersByHost.get(hostName);
		if (offers == null) {
			Gpr.debug("Offer accounting problem in host '" + hostName + "': This should ever happen!");
			cluster.remove(host); // Remove any resources since we don't really seem to have any
			return false;
		}

		// Select offers and add taskInfo
		HostResources tr = new HostResources(task.getResources());
		for (Offer offer : offers) {
			// Consume resources until offers fulfill task requirements
			HostResources or = parseOffer(offer);
			tr.consume(or);

			// Add offer and taskInfo to collections
			offerIds.add(offer.getId());
			TaskInfo taskInfo = taskInfo(offer, task);
			taskInfos.add(taskInfo);

			// Are all resources needed for this task satisfied?
			if (tr.isConsumed()) return true;
		}

		Gpr.debug("Resource accounting problem in host '" + hostName + "': This should ever happen!");
		return false;
	}

	/**
	 * Invoked when an offer is no longer valid (e.g., the slave was
	 * lost or another framework used resources in the offer). If for
	 * whatever reason an offer is never rescinded (e.g., dropped
	 * message, failing over framework, etc.), a framwork that attempts
	 * to launch tasks using an invalid offer will receive TASK_LOST
	 * status updates for those tasks (see Scheduler::resourceOffers).
	 */
	@Override
	public void offerRescinded(SchedulerDriver driver, OfferID offerId) {
		if (verbose) Gpr.debug("Scheduler: Offer Rescinded " + offerId.getValue());
		remove(offerId.getValue());
	}

	/**
	 * Convert offer to hostResources
	 */
	HostResources parseOffer(Offer offer) {
		HostResources hr = new HostResources();

		hr.setMem(0);
		hr.setCpus(0);

		for (Resource r : offer.getResourcesList()) {
			String resourceName = r.getName();
			int value = (int) r.getScalar().getValue();

			switch (resourceName) {
			case OFFER_MEM:
				hr.setMem(MB * value);
				break;
			case OFFER_CPUS:
				hr.setCpus(value);
				break;
			}
		}

		return hr;
	}

	/**
	 * Invoked when the scheduler successfully registers with a Mesos
	 * master. A unique ID (generated by the master) used for
	 * distinguishing this framework from others and MasterInfo
	 * with the ip and port of the current master are provided as arguments.
	 */
	@Override
	public void registered(SchedulerDriver driver, FrameworkID frameworkId, MasterInfo masterInfo) {
		if (verbose) Gpr.debug("Scheduler: Registered framework " + frameworkId.getValue() + ", master " + masterInfo.getHostname());
	}

	/**
	 * Add resource offered
	 */
	public synchronized void remove(String offerId) {
		// Update offer by Id
		Offer offer = offersById.remove(offerId);

		// Offer not found? Nothing to remove
		if (offer == null) {
			Gpr.debug("Unknown offer " + offerId);
			return;
		}

		// Parse offer
		String hostName = offer.getHostname();
		HostResources offerResources = parseOffer(offer);
		if (verbose) Gpr.debug("Removing offer [" + offerId + "]:" + hostName + "\t" + offerResources);

		// Update resources by host
		Host host = cluster.getHost(hostName);
		if (host != null) {
			HostResources hr = host.getResources();
			hr.consume(offerResources);

			// Nothing left? => Remove entry
			if (!hr.isValid()) cluster.remove(host);
			host.updateResourcesAvailable();
		}

		// Update offers by host
		Set<Offer> offers = offersByHost.get(hostName);
		if (offers != null) {
			offers.remove(offer);
			if (offers.isEmpty()) offersByHost.remove(hostName);
		}
	}

	/**
	 * Invoked when the scheduler re-registers with a newly elected Mesos master.
	 * This is only called when the scheduler has previously been registered.
	 * MasterInfo containing the updated information about the elected master
	 * is provided as an argument.
	 */
	@Override
	public void reregistered(SchedulerDriver driver, MasterInfo masterInfo) {
		if (verbose) Gpr.debug("Scheduler: Re-Registered " + masterInfo.getHostname());
	}

	/**
	 * Invoked when resources have been offered to this framework. A
	 * single offer will only contain resources from a single slave.
	 * Resources associated with an offer will not be re-offered to
	 * _this_ framework until either (a) this framework has rejected
	 * those resources (see SchedulerDriver::launchTasks) or (b) those
	 * resources have been rescinded (see Scheduler::offerRescinded).
	 * Note that resources may be concurrently offered to more than one
	 * framework at a time (depending on the allocator being used). In
	 * that case, the first framework to launch tasks using those
	 * resources will be able to use them while the other frameworks
	 * will have those resources rescinded (or if a framework has
	 * already launched tasks with those resources then those tasks will
	 * fail with a TASK_LOST status and a message saying as much).
	 */
	@Override
	public void resourceOffers(SchedulerDriver driver, List<Offer> offers) {
		if (verbose) Gpr.debug("Scheduler: Resource Offers");

		//---
		// Process offers
		//---
		// No tasks to run? Decline all offers
		if (taskToLaunch.isEmpty()) {
			for (Offer offer : offers) {
				if (verbose) Gpr.debug("Declining offer: " + offer.getId());
				driver.declineOffer(offer.getId());
			}

			// Also decline all remaining offers
			declineAllOffers(driver);
			return;
		}

		// Add offers
		for (Offer offer : offers)
			add(offer);

		//---
		// Match offers (and resources) to tasks
		//---
		Set<Task> assignedTasks = new HashSet<>();
		Collection<TaskInfo> taskInfos = new HashSet<>();
		Collection<OfferID> offerIds = new HashSet<>();
		Set<OfferID> offerIdsUsed = new HashSet<>();

		for (Host host : cluster) {
			if (host instanceof HostInifinte) {
				// Skip master node
			} else {
				// Try to match as many tasks as possible in this host
				for (Task task : taskToLaunch) {
					if (verbose) Gpr.debug("Trying to launch task " + task.getId());
					if (matchTask(task, host, offerIds, taskInfos)) {
						host.add(task); // Account used resources
						assignedTasks.add(task); // Task was assigned
						executionerMesos.taskStarted(task); // Jump to 'started' state

						// No more resources? => No point on trying to match more tasks
						if (!host.getResourcesAvaialble().isValid()) {
							if (verbose) Gpr.debug("Host '" + host.getHostName() + "' has no more resources available");
							break;
						}
					}
				}

				// Any task to launch?
				if (!assignedTasks.isEmpty()) {
					// Do we need to check driver status?
					if (verbose) Gpr.debug("Launching tasks: offer: " + offerIds.size() + "\ttasks:" + taskInfos.size());
					driver.launchTasks(offerIds, taskInfos);

					// Remove assigned tasks
					taskToLaunch.removeAll(assignedTasks);
					offerIdsUsed.addAll(offerIds);

					// Initialize for next round
					taskInfos = new ArrayList<>();
					offerIds = new ArrayList<>();
					assignedTasks = new HashSet<>();
				}
			}
		}

		// Remove used offers
		if (!offerIdsUsed.isEmpty()) {
			for (OfferID offerId : offerIdsUsed)
				remove(offerId.getValue());
		}

		// No more task to launch? => decline all remaining offers
		if (taskToLaunch.isEmpty()) declineAllOffers(driver);
	}

	/**
	 * Invoked when a slave has been determined unreachable (e.g.,
	 * machine failure, network partition). Most frameworks will need to
	 * reschedule any tasks launched on this slave on a new slave.
	 */
	@Override
	public void slaveLost(SchedulerDriver driver, SlaveID slaveId) {
		// Mark call tasks in that host fail?
		if (verbose) Gpr.debug("Scheduler: Slave Lost " + slaveId.getValue());
	}

	/**
	 * Invoked when the status of a task has changed (e.g., a slave is
	 * lost and so the task is lost, a task finishes and an executor
	 * sends a status update saying so, etc). Note that returning from
	 * this callback _acknowledges_ receipt of this status update! If
	 * for whatever reason the scheduler aborts during this callback (or
	 * the process exits) another status update will be delivered (note,
	 * however, that this is currently not true if the slave sending the
	 * status update is lost/fails during that time).
	 */
	@Override
	public void statusUpdate(SchedulerDriver driver, TaskStatus status) {
		String taskId = status.getTaskId().getValue();
		if (verbose) Gpr.debug("Scheduler: Status update, task " + taskId + ", state " + status.getState());

		// Find task
		Task task = taskById.get(taskId);
		if (task == null) throw new RuntimeException("task ID '" + taskId + "' not found. This should never happen!");

		// Update state
		switch (status.getState()) {
		case TASK_RUNNING:
			executionerMesos.taskRunning(task);
			break;

		case TASK_FINISHED:
			executionerMesos.taskFinished(task, TaskState.FINISHED);
			break;

		case TASK_ERROR:
		case TASK_FAILED:
			executionerMesos.taskFinished(task, TaskState.ERROR);
			break;

		case TASK_KILLED:
		case TASK_LOST:
			executionerMesos.taskFinished(task, TaskState.KILLED);
			break;

		default:
			throw new RuntimeException("Unhandled Mesos task state: " + status.getState());
		}
	}

	/**
	 * Create a mesos taskInfo
	 */
	TaskInfo taskInfo(Offer offer, Task task) {
		// Assign a task ID and name
		String taskIdMesos = BdsMesosFramework.taskIdMesos(task);
		taskById.put(taskIdMesos, task);
		TaskID taskId = TaskID.newBuilder().setValue(taskIdMesos).build();
		String taskName = task.getName();

		// Resources
		int numCpus = task.getResources().getCpus() > 0 ? task.getResources().getCpus() : 1;
		Resource cpus = Resource.newBuilder().setName(OFFER_CPUS).setType(Value.Type.SCALAR).setScalar(Value.Scalar.newBuilder().setValue(numCpus)).build(); // Number of CPUS

		long memSize = (task.getResources().getMem() / MB) > 0 ? (task.getResources().getMem() / MB) : 64;
		Resource mem = Resource.newBuilder().setName(OFFER_MEM).setType(Value.Type.SCALAR).setScalar(Value.Scalar.newBuilder().setValue(memSize)).build(); // Memory in MB

		// Executor
		ExecutorInfo execInfo = ExecutorInfo.newBuilder(executor).build();

		// Task's data: Command to execute
		String cmdArgs[] = Executioner.createBdsExecCmdArgs(task);
		ByteString data = ByteString.copyFromUtf8(BdsMesosFramework.packArray(cmdArgs));

		// Create task
		TaskInfo taskInfo = TaskInfo.newBuilder() //
				.setName(taskName)//
				.setTaskId(taskId) //
				.setSlaveId(offer.getSlaveId()) //
				.addResources(cpus) //
				.addResources(mem) //
				.setExecutor(execInfo) //
				.setData(data) //
				.build();

		return taskInfo;
	}
}
