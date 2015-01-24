package ca.mcgill.mcb.pcingola.bigDataScript.executioner;

import ca.mcgill.mcb.pcingola.bigDataScript.Config;

/**
 * Check that tasks are still running.
 * Use a 'ps' command
 *
 * @author pcingola
 */
public class CheckTasksRunningLocal extends CheckTasksRunning {

	public CheckTasksRunningLocal(Config config, Executioner executioner) {
		super(config, executioner);
		defaultCmdArgs = ExecutionerLocal.LOCAL_STAT_COMMAND;
	}

}
