package org.bds.executioner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bds.Config;
import org.bds.cluster.host.TaskResourcesAws;
import org.bds.task.Task;
import org.bds.util.GprAws;

import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesResponse;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;
import software.amazon.awssdk.services.ec2.model.Instance;
import software.amazon.awssdk.services.ec2.model.InstanceStateName;
import software.amazon.awssdk.services.ec2.model.Reservation;

/**
 * Check that tasks are still running.
 * Query AWS to find all instances running
 *
 * @author pcingola
 */
public class CheckTasksRunningAws extends CheckTasksRunning {

	public CheckTasksRunningAws(Config config, Executioner executioner) {
		super(config, executioner);
	}

	/**
	 * Find all instances running in "region"
	 * @return A set of instance IDs
	 */
	protected Set<String> awsInstancesRunning(String region, Set<String> instanceIds) {
		Set<String> foundInstanceIds = new HashSet<>();
		Ec2Client ec2 = GprAws.ec2Client(region);
		try {
			String nextToken = null;
			do {
				DescribeInstancesRequest request = DescribeInstancesRequest.builder().instanceIds(instanceIds).nextToken(nextToken).build();
				DescribeInstancesResponse response = ec2.describeInstances(request);

				for (Reservation reservation : response.reservations()) {
					for (Instance instance : reservation.instances()) {
						InstanceStateName s = instance.state().name();
						// If the instance is not in some of these states, it means it is "gone"
						// Note that we don't include SHUTTING_DOWN or TERMINATED as valid "found" states, because
						// in normal condition the instance should have finished executing the task (so those tasks
						// are no longer in our list "to check".
						// We want to detect the cases when an instance is terminated or stopped when executing a
						// task (e.g. a user accidentally terminates an instance that is running a task)
						if (s == InstanceStateName.PENDING || s == InstanceStateName.RUNNING) {
							foundInstanceIds.add(instance.instanceId());
						}
					}
				}
				nextToken = response.nextToken();
			} while (nextToken != null);
		} catch (Ec2Exception e) {
			error("Error getting list of AWS instances for region '" + region + "': " + e.awsErrorDetails().errorMessage());
		}
		return foundInstanceIds;
	}

	/**
	 * Find all tasks that are running (i.e. instances running that have tasks associated with them)
	 * @return A set of instance IDs
	 */
	@Override
	protected Set<Task> findRunningTasks() {
		// Find currently used regions and instancesIds from tasks
		Set<String> regions = new HashSet<>();
		Set<String> instanceIds = new HashSet<>();
		Map<String, Task> instanceIds2Task = new HashMap<>();
		for (Task t : taskById.values()) {
			TaskResourcesAws res = (TaskResourcesAws) t.getResources();
			String region = res.getRegion();
			if (region != null) regions.add(region);

			instanceIds.add(t.getPid());
			instanceIds2Task.put(t.getPid(), t);
		}

		// Query AWS to check if the instances for all the tasks are running
		// Find all instances running in all regions
		Set<String> instanceIdsFound = new HashSet<>();
		for (String r : regions) {
			instanceIdsFound.addAll(awsInstancesRunning(r, instanceIds));
		}

		// Find tasks for each instance Id that was found on AWS
		Set<Task> tasksFound = new HashSet<>();
		for (String iidf : instanceIdsFound)
			tasksFound.add(instanceIds2Task.get(iidf));

		return tasksFound;
	}

}
