package org.bds.executioner;

import org.bds.Config;

/**
 * Check that tasks are still running.
 * Use a 'ps' command
 *
 * TODO: Change this to use 'ProcessHandle.allProcesses​()' which wasn't available when I first wrote this
 *
 * @author pcingola
 */
public class CheckTasksRunningLocal extends CheckTasksRunningCmd {

	public CheckTasksRunningLocal(Config config, Executioner executioner) {
		super(config, executioner);
		defaultCmdArgs = ExecutionerLocal.LOCAL_STAT_COMMAND;
	}

}
