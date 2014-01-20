package ca.mcgill.mcb.pcingola.bigDataScript.cluster.host;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.Cluster;

/**
 * A host with infinite capacity
 * 
 * @author pcingola@mcgill.ca
 */
public class HostInifinte extends Host {

	public HostInifinte(Cluster cluster) {
		super(cluster, "localhost");

		health.setAlive(true); // This host is up
		// Note: We don't set resources => Unlimited
	}

	@Override
	public boolean canUpdateSsh() {
		return false;
	}

}
