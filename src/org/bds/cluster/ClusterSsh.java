package org.bds.cluster;

import java.util.HashMap;
import java.util.Map;

import org.bds.cluster.host.Host;
import org.bds.cluster.host.HostHealth;
import org.bds.cluster.host.HostHealthUpdater;
import org.bds.cluster.host.HostSsh;

/**
 * Cluster configuration information & hosts
 *
 * @author pcingola
 */
public class ClusterSsh extends ComputerSystem {

	// Reference values for alarms
	HostHealth healthRed = new HostHealth(null);
	HostHealth healthYellow = new HostHealth(null);

	String sshComand = "ssh"; // Default 'ssh' command
	int connectTimeout = 10; // Default connection timeout is 10 seconds
	long defaultWaitTime = 100; // Default time to use in 'wait' calls
	int sshKeepAlive = 240; // Default 'keep alive' map for ssh (i.e. ServerAliveInterval)
	long refreshTime = 60; // Number of seconds before host info updates
	protected Map<String, HostHealthUpdater> hostHealthUpdaters; // All hosts updaters indexed by name

	public ClusterSsh() {
		super();

		// Set default values for 'condition thresholds'
		healthRed.setLoadAvg(1.5);
		healthRed.setLoggedInUsersCount(2);
		healthRed.setMemUsage(0.90);

		healthYellow.setLoadAvg(1.00);
		healthYellow.setLoggedInUsersCount(1);
		healthYellow.setMemUsage(0.70);

		hostHealthUpdaters = new HashMap<String, HostHealthUpdater>();
	}

	/**
	 * Add a host
	 */
	@Override
	public void add(Host host) {
		String hostName = host.toString();
		hosts.put(hostName, host);

		// Create a host info updater (except for local host)
		if (host instanceof HostSsh) {
			HostHealthUpdater hostInfoUpdater = new HostHealthUpdater((HostSsh) host);
			hostHealthUpdaters.put(hostName, hostInfoUpdater);
		}
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public long getDefaultWaitTime() {
		return defaultWaitTime;
	}

	public HostHealth getHealthRed() {
		return healthRed;
	}

	public HostHealth getHealthYellow() {
		return healthYellow;
	}

	public long getRefreshTime() {
		return refreshTime;
	}

	public String getSshComand() {
		return sshComand;
	}

	public int getSshKeepAlive() {
		return sshKeepAlive;
	}

	/**
	 * Start running all threads for HostInfoUpdaters
	 */
	public void startHostInfoUpdaters() {
		for (HostHealthUpdater hiu : hostHealthUpdaters.values())
			hiu.start();
	}

	/**
	 * Stop running all threads for HostInfoUpdaters
	 */
	public void stopHostInfoUpdaters() {
		for (HostHealthUpdater hiu : hostHealthUpdaters.values())
			hiu.kill();
	}

}
