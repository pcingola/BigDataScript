package org.bigDataScript.executioner;

import org.bigDataScript.Config;

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

	public CheckTasksRunningCluster(Config config, Executioner executioner, String cmdArgs[]) {
		super(config, executioner);
		defaultCmdArgs = cmdArgs;
	}

}
