package org.bds.cluster.host;

import org.bds.cluster.ComputerSystem;

/**
 * Local host information
 *
 * @author pcingola@mcgill.ca
 */
public class HostSsh extends Host {

	HostHealth health;

	public HostSsh(ComputerSystem cluster, String hostName) {
		super(cluster, hostName);

		// Set basic parameters
		resources.setCpus(1);
		health = new HostHealth(this);	
		health.setAlive(true);
	}

	public HostHealth getHealth() {
		return health;
	}

	@Override
	public boolean isAlive() {
		return getHealth().isAlive();
	}

}
