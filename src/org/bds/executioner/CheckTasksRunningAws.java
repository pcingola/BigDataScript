package org.bds.executioner;

import org.bds.Config;

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

	// TODO: Add query to AWS to retrieve all instances
	// TODO: Run every 2 minutes?

}
