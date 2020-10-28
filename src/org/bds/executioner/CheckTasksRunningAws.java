package org.bds.executioner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

	public CheckTasksRunningAws(Config config, Executioner executioner) {
		super(config, executioner);
	}

	/**
	 * Find all instances running
	 * @return A list of instances
	 */
	protected List<String> awsInstancesRunning() {
		Gpr.debug("UNIMPLEMENTED");
		// TODO: Query AWS for all ec2 instances running
		return new ArrayList<>();
	}

	@Override
	public void check() {
		if (!shouldCheck()) return; // Check every now and then
		checkTasksRunning();
	}

	/**
	 * Check that all tasks have instances running on the cloud
	 */
	protected void checkTasksRunning() {
		// Query to AWS to retrieve all instances, find tasks running
		List<String> awsInstances = awsInstancesRunning();
		Set<Task> taskFound = parseTaskFromInstaces(awsInstances);

		// If any 'running' tasks was not not found, mark is as finished ('ERROR')
		tasksRunning(taskFound);
	}

	/**
	 * Parse task IDs from cloud instances information and return a set of Tasks that are running
	 */
	Set<Task> parseTaskFromInstaces(List<String> awsInstances) {
		Gpr.debug("UNIMPLEMENTED");
		Set<Task> taskFound = new HashSet<>();
		return taskFound;
	}

}
