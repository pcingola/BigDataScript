package org.bds.executioner;

import java.util.HashSet;
import java.util.Set;

import org.bds.Config;
import org.bds.cluster.host.TaskResourcesAws;
import org.bds.task.Task;
import org.bds.util.Gpr;
import org.bds.util.GprAws;
import org.bds.util.Timer;

import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesResponse;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;
import software.amazon.awssdk.services.ec2.model.Instance;
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
	 * Find all instances running in all regions
	 * @return A set of instance IDs
	 */
	protected Set<String> awsInstancesRunning() {
		// Find currently used regions and instancesIds from tasks
		Set<String> regions = new HashSet<>();
		Set<String> instanceIds = new HashSet<>();
		for (Task t : taskById.values()) {
			TaskResourcesAws res = (TaskResourcesAws) t.getResources();
			String region = res.getRegion();
			if (region != null) regions.add(region);
			instanceIds.add(t.getPid());
		}

		// Query AWS to check if the instances for all the tasks are running
		for (String r : regions) {
			instanceIds.addAll(awsInstancesRunning(r, instanceIds));
		}
		return instanceIds;
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
						foundInstanceIds.add(instance.instanceId());
						Gpr.debug("FOUND INSTANCE: " + instance.instanceId());
					}
				}
				nextToken = response.nextToken();
			} while (nextToken != null);
		} catch (Ec2Exception e) {
			Timer.showStdErr("ERROR getting list of AWS instances for region '" + region + "': " + e.awsErrorDetails().errorMessage());
		}
		return foundInstanceIds;
	}

	/**
	 * Check that all tasks have instances running on the cloud
	 */
	@Override
	protected Set<Task> findRunningTasks() {
		// Query to AWS to retrieve all instances, find tasks running
		Set<String> awsInstanceIds = awsInstancesRunning();
		return findRunningTaskByPid(awsInstanceIds);
	}

}
