package ca.mcgill.mcb.pcingola.bigDataScript.executioner;

import ca.mcgill.mcb.pcingola.bigDataScript.Config;

/**
 * Execute tasks in a MOAB cluster.
 * 
 * All commands are run using 'qsub' (or equivalent) commands
 * 
 * @author pcingola
 */
public class ExecutionerClusterPbs extends ExecutionerCluster {

	public ExecutionerClusterPbs(Config config) {
		super(config);

		// Define commnads
		String EXEC_COMMAND[] = { FAKE_CLUSTER + "msub" };
		String KILL_COMMAND[] = { FAKE_CLUSTER + "canceljob" };
		String STAT_COMMAND[] = { FAKE_CLUSTER + "showq" };

		CLUSTER_EXEC_COMMAND = EXEC_COMMAND;
		CLUSTER_KILL_COMMAND = KILL_COMMAND;
		CLUSTER_STAT_COMMAND = STAT_COMMAND;

	}

	@Override
	protected CheckTasksRunning getCheckTasksRunning() {
		if (checkTasksRunning == null) checkTasksRunning = new CheckTasksRunningCluster(this, CLUSTER_STAT_COMMAND);
		return checkTasksRunning;
	}

}
