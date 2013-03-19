package ca.mcgill.mcb.pcingola.bigDataScript.cluster.host;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.Cluster;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * Local host information
 * 
 * @author pcingola@mcgill.ca
 */
public class HostLocal extends Host {

	public HostLocal() {
		super(Cluster.FAKE_CLUSTER, "localhost");

		// Set basic parameters
		health.setAlive(true);
		resources.setCpus(Gpr.NUM_CORES);
	}

}
