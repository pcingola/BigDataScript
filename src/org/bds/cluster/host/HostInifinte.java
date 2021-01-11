package org.bds.cluster.host;

import org.bds.cluster.ComputerSystem;

/**
 * A host with infinite capacity
 *
 * @author pcingola@mcgill.ca
 */
public class HostInifinte extends Host {

	public HostInifinte(ComputerSystem system) {
		super(system, "localhost");
		resources = new HostResourcesInf();
	}

}
