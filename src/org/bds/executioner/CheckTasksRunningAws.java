package org.bds.executioner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bds.Config;
import org.bds.task.Task;
import org.bds.util.Gpr;

/**
 * Check that tasks are still running.
 * Query AWS to find all instances running
 *
 * @author pcingola
 */
public class CheckTasksRunningAws extends CheckTasksRunning {

	protected Map<String, Integer> countByRegion;

	public CheckTasksRunningAws(Config config, Executioner executioner) {
		super(config, executioner);
	}

	@Override
	public void add(Task task) {
		super.add(task);
		// TODO: Is this task running in a new region?
	}

	/**
	 * Find all instances running
	 * @return A list of instances
	 */
	protected Set<String> awsInstancesRunning() {
		List<String> instanceIds = new ArrayList<>();

		// TODO: Query all regions (we can have multiple instances in different regions)
		// TODO: Create a set of instances

		//		DescribeInstancesRequest request = DescribeInstancesRequest.builder().instanceIds(instanceIds).build();

		//		boolean done = false;
		//        String nextToken = null;
		//
		//        try {
		//            do {
		//                DescribeInstancesRequest request = DescribeInstancesRequest.builder().maxResults(6).nextToken(nextToken).build();
		//                DescribeInstancesResponse response = ec2.describeInstances(request);
		//
		//                for (Reservation reservation : response.reservations()) {
		//                    for (Instance instance : reservation.instances()) {
		//                    System.out.printf(
		//                            "Found Reservation with id %s, " +
		//                                    "AMI %s, " +
		//                                    "type %s, " +
		//                                    "state %s " +
		//                                    "and monitoring state %s",
		//                            instance.instanceId(),
		//                            instance.imageId(),
		//                            instance.instanceType(),
		//                            instance.state().name(),
		//                            instance.monitoring().state());
		//                    System.out.println("");
		//                }
		//            }
		//                nextToken = response.nextToken();
		//            } while (nextToken != null);
		Gpr.debug("UNIMPLEMENTED!");
		return new HashSet<>();
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

	@Override
	public synchronized void remove(Task task) {
		super.remove(task);
		// TODO: Are there regions that don't have more instances? We can stop checking those regions
	}

}
