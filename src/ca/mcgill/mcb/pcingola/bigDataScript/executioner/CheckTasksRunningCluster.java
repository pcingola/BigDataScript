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

	public CheckTasksRunningCluster(Executioner executioner) {
		super(executioner);
		defaultCmdArgs = ExecutionerCluster.CLUSTER_STAT_COMMAND;
		cmdPidColumn = 0;
	}

}
