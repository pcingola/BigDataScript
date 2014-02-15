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

	protected final String CLUSTER_EXEC_COMMAND[] = { FAKE_CLUSTER + "msub" };
	protected final String CLUSTER_KILL_COMMAND[] = { FAKE_CLUSTER + "canceljob" };
	protected final String CLUSTER_STAT_COMMAND[] = { FAKE_CLUSTER + "showq" };
	protected final String CLUSTER_BDS_COMMAND = "bds exec ";

	public ExecutionerClusterPbs(Config config) {
		super(config);
	}

	@Override
	protected CheckTasksRunning getCheckTasksRunning() {
		if (checkTasksRunning == null) checkTasksRunning = new CheckTasksRunningCluster(this, CLUSTER_STAT_COMMAND);
		return checkTasksRunning;
	}

}
