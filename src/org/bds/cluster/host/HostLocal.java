package org.bds.cluster.host;

import org.bds.cluster.Cluster;
import org.bds.util.Gpr;

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
