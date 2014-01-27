package ca.mcgill.mcb.pcingola.bigDataScript.cluster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostHealth;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostHealthUpdater;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostResources;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * Cluster configuration information & hosts
 * 
 * @author pcingola
 */
public class Cluster implements Iterable<Host> {

	public static final Cluster FAKE_CLUSTER = new Cluster();

	// Reference values for alarms
	public HostHealth healthRed = new HostHealth(null);
	public HostHealth healthYellow = new HostHealth(null);

	String sshComand = "ssh"; // Default 'ssh' command
	int connectTimeout = 10; // Default connection timeout is 10 seconds
	long defaultWaitTime = 100; // Default time to use in 'wait' calls
	long refreshTime = 60; // Number of seconds before host info updates
	int sshKeepAlive = 240; // Default 'keep alive' value for ssh (i.e. ServerAliveInterval)
	boolean doNotRunOnRed = true; // If a host has a 'red' condition => do not run any tasks on them
	Map<String, Host> hosts; // All hosts indexed by name
	Map<String, HostHealthUpdater> hostHealthUpdaters; // All hosts updaters indexed by name

	public Cluster() {
		hosts = new HashMap<String, Host>();
		hostHealthUpdaters = new HashMap<String, HostHealthUpdater>();

		// Set default values for 'condition thresholds'
		healthRed.setLoadAvg(1.5);
		healthRed.setLoggedInUsersCount(2);
		healthRed.setMemUsage(0.90);

		healthYellow.setLoadAvg(1.00);
		healthYellow.setLoggedInUsersCount(1);
		healthYellow.setMemUsage(0.70);
	}

	/**
	 * Add a host
	 * @param host
	 */
	public void add(Host host) {
		String hostName = host.toString();

		hosts.put(hostName, host);

		// Create a host info updater (except for local host)
		if (host.canUpdateSsh()) {
			HostHealthUpdater hostInfoUpdater = new HostHealthUpdater(host);
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

	/**
	 * Get a list of host's info
	 * @return
	 */
	public List<Host> getHosts() {
		ArrayList<Host> lhi = new ArrayList<Host>();
		lhi.addAll(hosts.values());
		Collections.sort(lhi);
		return lhi;
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

	public String info() {
		long totMem = 0, totCpus = 0, totHosts = 0, availHosts = 0, availMem = 0, availCpus = 0;
		long totFs = 0, availFs = 0;
		for (Host h : hosts.values()) {

			HostHealth hh = h.getHealth();
			HostResources hr = h.getResources();
			HostResources hra = h.getResourcesAvaialble();

			totHosts++;
			if (hr.getCpus() > 0) {
				totCpus += hr.getCpus();
				if (hr.getMem() > 0) totMem += hr.getMem();
				totFs += hh.getFsTotal();

				if (hh.isAvailable()) {
					availHosts++;
					availCpus += hra.getCpus();
					if (hra.getMem() > 0) availMem += hra.getMem() * (1.0 - hh.getMemUsage());
					availFs += hh.getFsAvail();
				}
			}
		}

		return String.format("Hosts: %d / %d    Cpus: %d / %d    Memory: %s / %s    Disk: %s / %s", availHosts, totHosts, availCpus, totCpus//
				, Gpr.toStringMem(totMem * 1024 * 1024) //
				, Gpr.toStringMem(availMem * 1024 * 1024) //
				, Gpr.toStringMem(totFs * 1024) //
				, Gpr.toStringMem(availFs * 1024) //
				);
	}

	public boolean isDoNotRunOnRed() {
		return doNotRunOnRed;
	}

	@Override
	public Iterator<Host> iterator() {
		return hosts.values().iterator();
	}

	public int size() {
		return hosts.size();
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

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Number of host    : " + hosts.size() + "\n");
		sb.append("Ssh command       : " + sshComand + "\n");
		sb.append("Connect timeout   : " + connectTimeout + "\n");
		sb.append("Refresh time      : " + refreshTime + "\n");
		sb.append("Default wait time : " + defaultWaitTime + "\n");

		// Show all host's infos (sorted by host name)
		sb.append("Hosts             : \n");
		List<String> hostNames = new LinkedList<String>();
		hostNames.addAll(hosts.keySet());
		Collections.sort(hostNames);
		for (String hname : hostNames) {
			Host h = hosts.get(hname);
			sb.append("\t" + h + "\tResources: " + h.getResources() + "\tAvailable: " + h.getResourcesAvaialble() + "\n");
		}

		return sb.toString();
	}

}
