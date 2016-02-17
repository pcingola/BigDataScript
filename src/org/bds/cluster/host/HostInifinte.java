package org.bds.cluster.host;

import org.bds.cluster.Cluster;

/**
 * A host with infinite capacity
 *
 * @author pcingola@mcgill.ca
 */
public class HostInifinte extends Host {

	public HostInifinte(Cluster cluster) {
		super(cluster, "localhost");
		resources = new HostResourcesInf();
	}

}
