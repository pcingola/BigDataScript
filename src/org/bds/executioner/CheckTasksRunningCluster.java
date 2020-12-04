package org.bds.executioner;

import org.bds.Config;

/**
 * Check that tasks are still running.
 * Use a 'qstat' command
 *
 * @author pcingola
 */
public class CheckTasksRunningCluster extends CheckTasksRunningCmd {

	public CheckTasksRunningCluster(Config config, Executioner executioner, String cmdArgs[]) {
		super(config, executioner);
		defaultCmdArgs = cmdArgs;
	}

}
