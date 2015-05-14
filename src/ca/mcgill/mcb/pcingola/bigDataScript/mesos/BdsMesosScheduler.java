package ca.mcgill.mcb.pcingola.bigDataScript.mesos;

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

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostResources;
import ca.mcgill.mcb.pcingola.bigDataScript.executioner.ExecutionerLocal;
import ca.mcgill.mcb.pcingola.bigDataScript.executioner.ExecutionerMesos;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.task.TaskState;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

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
	protected boolean verbose = true;
	protected List<Task> taskToLaunch;
	protected HashMap<String, Task> taskById;
	protected ExecutionerMesos executionerMesos;
	protected Map<String, HostResources> resourcesByHost;
	protected Map<String, Set<Offer>> offersByHost;
	protected Map<String, Offer> offersById;

	public BdsMesosScheduler(ExecutionerMesos executionerMesos, ExecutorInfo executor) {
		this.executionerMesos = executionerMesos;
		this.executor = executor;
		taskToLaunch = new LinkedList<Task>();
		taskById = new HashMap<String, Task>();
		resourcesByHost = new HashMap<>();
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
		HostResources slaveResources = resourcesByHost.get(hostName);
		if (slaveResources == null) resourcesByHost.put(hostName, offerResources);
		else slaveResources.add(offerResources);

		// Update offers by host
		Set<Offer> offers = offersByHost.get(hostName);
		if (offers == null) {
			offers = new HashSet<Offer>();
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

	/**
	 * Find a task that matches the offer
	 * Use the the offer that matches the first task in the list
	 * In case of multiple matches, the 'first' offer / first 'task' wins
	 *
	 * @return null if no task is found
	 */
	protected boolean matchTask(Collection<OfferID> offerIds, Collection<TaskInfo> taskInfos) {
		if (taskToLaunch.isEmpty()) return false;

		// TODO: We should probably not remove it completely until we
		// are sure that it was started by Mesos (stateChange)
		Task task = taskToLaunch.remove(0);

		return task != null;
	}

	protected boolean matchTask(Task task, Collection<OfferID> offerIds, Collection<TaskInfo> taskInfos) {
		for (String hostName : resourcesByHost.keySet())
			if (matchTask(task, hostName, offerIds, taskInfos)) // Can this task be run on this host?
				return true;

		return false;
	}

	protected synchronized boolean matchTask(Task task, String hostName, Collection<OfferID> offerIds, Collection<TaskInfo> taskInfos) {
		HostResources hr = resourcesByHost.get(hostName);
		if (hr == null || !hr.isValid()) return false;

		// Not enough resources in this host?
		if (!hr.hasResources(task.getResources())) return false;

		// OK, we should be able to run 'task' in hostName
		Set<Offer> offers = offersByHost.get(hostName);
		if (offers == null) {
			resourcesByHost.remove(hostName); // Remove any resources since we don't really seem to have any
			return false;
		}

		// Select offers and add taskInfo
		HostResources tr = new HostResources(task.getResources());
		for (Offer offer : offers) {
			// Consume resources until offers fulfill task requirements
			HostResources or = parseOffer(offer);
			tr.consume(or);

			// Add offer and taskInfo to lists
			offerIds.add(offer.getId());
			TaskInfo taskInfo = taskInfo(offer, task);
			taskInfos.add(taskInfo);

			// Are all resources needed for this task satisfied?
			if (tr.isConsumed()) return true;
		}

		Gpr.debug("Resource accounting problem: This should ever happen!");
		return true;
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

		for (Resource r : offer.getResourcesList()) {
			String resourceName = r.getName();
			int value = (int) r.getScalar().getValue();

			switch (resourceName) {
			case OFFER_MEM:
				hr.setMem(GB * value);
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
		HostResources slaveResources = resourcesByHost.get(hostName);
		if (slaveResources != null) {
			slaveResources.consume(offerResources);

			// Nothing left? => Remove entry
			if (!slaveResources.isValid()) resourcesByHost.remove(hostName);
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
		Collection<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
		Collection<OfferID> offerIds = new ArrayList<OfferID>();

		for (Offer offer : offers)
			add(offer);

		while (true) {
			boolean matched = matchTask(offerIds, taskInfos);
			Gpr.debug("MATCHED: " + matched);
			if (!matched) break;
		}

		if (verbose) Gpr.debug("Launching tasks: " + taskInfos.size());
		driver.launchTasks(offerIds, taskInfos);
	}

	/**
	 * Invoked when a slave has been determined unreachable (e.g.,
	 * machine failure, network partition). Most frameworks will need to
	 * reschedule any tasks launched on this slave on a new slave.
	 */
	@Override
	public void slaveLost(SchedulerDriver driver, SlaveID slaveId) {
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

		case TASK_FAILED:
			executionerMesos.taskFinished(task, TaskState.ERROR);
			break;

		case TASK_KILLED:
		case TASK_LOST:
			executionerMesos.taskFinished(task, TaskState.KILLED);
			break;

		default:
			Gpr.debug("Unhandled Mesos task state: " + status.getState());
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

		long memSize = (task.getResources().getMem() / MB) > 0 ? task.getResources().getMem() : 64;
		Resource mem = Resource.newBuilder().setName(OFFER_MEM).setType(Value.Type.SCALAR).setScalar(Value.Scalar.newBuilder().setValue(memSize)).build(); // Memory in MB

		// Executor
		ExecutorInfo execInfo = ExecutorInfo.newBuilder(executor).build();

		// Task's data: Command to execute
		String cmdArgs[] = ExecutionerLocal.createBdsExecCmdArgs(task);
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
