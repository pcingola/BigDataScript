package ca.mcgill.mcb.pcingola.bigDataScript.executioner;

/**
 * Check that tasks are still running.
 * Use a 'ps' command
 * 
 * @author pcingola
 */
public class CheckTasksRunningLocal extends CheckTasksRunning {

	public CheckTasksRunningLocal(Executioner executioner) {
		super(executioner);
		defaultCmdArgs = ExecutionerLocal.LOCAL_STAT_COMMAND;
		cmdPidColumn = 0;
	}

}
