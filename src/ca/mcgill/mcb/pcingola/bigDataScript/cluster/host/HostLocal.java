package ca.mcgill.mcb.pcingola.bigDataScript.cluster.host;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.Cluster;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

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
