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

	protected String CLUSTER_EXEC_COMMAND[] = { FAKE_CLUSTER + "msub" };
	protected String CLUSTER_KILL_COMMAND[] = { FAKE_CLUSTER + "canceljob" };
	protected String CLUSTER_STAT_COMMAND[] = { FAKE_CLUSTER + "showq" };
	protected String CLUSTER_BDS_COMMAND = "bds exec ";

	public ExecutionerClusterPbs(Config config) {
		super(config);
	}

	@Override
	protected CheckTasksRunning getCheckTasksRunning() {
		if (checkTasksRunning == null) checkTasksRunning = new CheckTasksRunningCluster(this, CLUSTER_STAT_COMMAND);
		return checkTasksRunning;
	}

}
