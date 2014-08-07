package ca.mcgill.mcb.pcingola.bigDataScript.executioner;

import ca.mcgill.mcb.pcingola.bigDataScript.Config;

/**
 * Execute tasks in a MOAB cluster.
 *
 * All commands are run using 'qsub' (or equivalent) commands
 *
 * @author pcingola
 */
public class ExecutionerClusterMoab extends ExecutionerCluster {

	public ExecutionerClusterMoab(Config config) {
		super(config);

		// Define commands
		String execCommand[] = { FAKE_CLUSTER + "msub" };
		String killCommand[] = { FAKE_CLUSTER + "canceljob" };
		String statCommand[] = { FAKE_CLUSTER + "showq" };
		String postMortemInfoCommand[] = { FAKE_CLUSTER + "checkjob", "-v" };
		String additionalArgs[] = {};

		clusterExecCommand = execCommand;
		clusterKillCommand = killCommand;
		clusterStatCommand = statCommand;
		clusterPostMortemInfoCommand = postMortemInfoCommand;
		clusterAdditionalArgs = additionalArgs;

	}

	//	@Override
	//	protected CheckTasksRunning getCheckTasksRunning() {
	//		if (checkTasksRunning == null) checkTasksRunning = new CheckTasksRunningCluster(this, joinArgs(clusterStatCommand, clusterAdditionalArgs));
	//		return checkTasksRunning;
	//	}

}
