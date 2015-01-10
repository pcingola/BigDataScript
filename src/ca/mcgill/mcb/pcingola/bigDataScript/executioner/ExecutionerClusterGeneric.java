package ca.mcgill.mcb.pcingola.bigDataScript.executioner;

import ca.mcgill.mcb.pcingola.bigDataScript.Config;

/**
 * Execute tasks in a PBS cluster.
 *
 * All commands are run using 'qsub' (or equivalent) commands
 *
 * @author pcingola
 */
public class ExecutionerClusterGeneric extends ExecutionerCluster {

	public ExecutionerClusterGeneric(Config config) {
		super(config);

		// Define commands
		String execCommand[] = { FAKE_CLUSTER + "msub" };
		String killCommand[] = { FAKE_CLUSTER + "canceljob" };
		String statCommand[] = { FAKE_CLUSTER + "showq" };
		String postMortemInfoCommand[] = { FAKE_CLUSTER + "checkjob", "-v" };

		clusterRunCommand = execCommand;
		clusterKillCommand = killCommand;
		clusterStatCommand = statCommand;
		clusterPostMortemInfoCommand = postMortemInfoCommand;
	}
}
