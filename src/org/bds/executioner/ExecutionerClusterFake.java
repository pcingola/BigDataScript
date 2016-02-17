package org.bds.executioner;

import org.bds.Config;

/**
 * Fake Cluster: This is a small computer simulating to be a cluster. It is
 * used for debugging and development
 *
 * @author pcingola
 */
public class ExecutionerClusterFake extends ExecutionerCluster {

	public static String FAKE_CLUSTER = Config.DEFAULT_CONFIG_DIR + "/fakeCluster/";

	protected ExecutionerClusterFake(Config config) {
		super(config);

		// Define commands
		String runCommand[] = { FAKE_CLUSTER + "qsub" };
		String killCommand[] = { FAKE_CLUSTER + "qdel" };
		String statCommand[] = { FAKE_CLUSTER + "qstat" };
		String postMortemInfoCommand[] = { FAKE_CLUSTER + "qstat", "-f" };

		clusterRunCommand = runCommand;
		clusterKillCommand = killCommand;
		clusterStatCommand = statCommand;
		clusterPostMortemInfoCommand = postMortemInfoCommand;
	}

}
