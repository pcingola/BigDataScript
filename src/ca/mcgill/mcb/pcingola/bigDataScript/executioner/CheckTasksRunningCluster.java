package ca.mcgill.mcb.pcingola.bigDataScript.executioner;

/**
 * Check that tasks are still running.
 * Use a 'qstat' command
 *
 * TODO: We should try to implement an XML parsing. Unfortunately, some
 * 		 clusters do not have 'qstat -xml' option (yikes!)
 *
 * @author pcingola
 */
public class CheckTasksRunningCluster extends CheckTasksRunning {

	public CheckTasksRunningCluster(Executioner executioner, String cmdArgs[]) {
		super(executioner);
		defaultCmdArgs = cmdArgs;
		cmdPidColumn = 0;
	}

}
