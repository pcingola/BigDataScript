package org.bds.executioner;

import org.bds.Config;

/**
 * Execute tasks in a PBS cluster.
 *
 * All commands are run using 'qsub' (or equivalent) commands
 *
 * @author pcingola
 */
public class ExecutionerClusterPbs extends ExecutionerCluster {

	public ExecutionerClusterPbs(Config config) {
		super(config);

		// Define commands
		String execCommand[] = { "msub" };
		String killCommand[] = { "canceljob" };
		String statCommand[] = { "showq" };
		String postMortemInfoCommand[] = { "checkjob", "-v" };

		clusterRunCommand = execCommand;
		clusterKillCommand = killCommand;
		clusterStatCommand = statCommand;
		clusterPostMortemInfoCommand = postMortemInfoCommand;
	}
}
