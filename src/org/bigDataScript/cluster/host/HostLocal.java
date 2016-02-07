package org.bigDataScript.cluster.host;

import org.bigDataScript.cluster.Cluster;
import org.bigDataScript.util.Gpr;

/**
 * Local host information
 *
 * @author pcingola@mcgill.ca
 */
public class HostLocal extends Host {

	public HostLocal(Cluster cluster) {
		super(cluster, "localhost");

		// Set basic parameters
		resources.setCpus(Gpr.NUM_CORES);
	}

}
