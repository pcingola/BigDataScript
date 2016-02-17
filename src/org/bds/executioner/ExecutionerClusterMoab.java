package org.bds.executioner;

import org.bds.Config;

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
