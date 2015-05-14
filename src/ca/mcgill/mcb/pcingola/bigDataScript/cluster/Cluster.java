package ca.mcgill.mcb.pcingola.bigDataScript.cluster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostResources;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * Cluster: A bunch of hosts
 *
 * @author pcingola
 */
public class Cluster implements Iterable<Host> {

	public static final Cluster FAKE_CLUSTER = new Cluster();

	protected boolean doNotRunOnRed = true; // If a host has a 'red' condition => do not run any tasks on them
	protected Map<String, Host> hosts; // All hosts indexed by name

	public Cluster() {
		hosts = new HashMap<String, Host>();
	}

	/**
	 * Add a host
	 */
	public void add(Host host) {
		String hostName = host.toString();
		Gpr.debug("ADDING HOST: " + host);
		hosts.put(hostName, host);
	}

	/**
	 * Get a host
	 */
	public Host getHost(String hostName) {
		return hosts.get(hostName);
	}

	/**
	 * Get a list of host's info
	 */
	public List<Host> getHosts() {
		ArrayList<Host> lhi = new ArrayList<Host>();
		lhi.addAll(hosts.values());
		Collections.sort(lhi);
		return lhi;
	}

	public String info() {
		long totMem = 0, totCpus = 0, totHosts = 0;
		for (Host h : hosts.values()) {
			HostResources hr = h.getResources();
			totHosts++;
			if (hr.getCpus() > 0) {
				totCpus += hr.getCpus();
				if (hr.getMem() > 0) totMem += hr.getMem();
			}
		}

		return String.format("Hosts: %d / %d    Cpus: %d\tMemory: %s", totHosts, totCpus, Gpr.toStringMem(totMem * 1024 * 1024));
	}

	public boolean isDoNotRunOnRed() {
		return doNotRunOnRed;
	}

	@Override
	public Iterator<Host> iterator() {
		return hosts.values().iterator();
	}

	public void remove(Host host) {
		String hostName = host.toString();
		hosts.remove(hostName);
	}

	public int size() {
		return hosts.size();
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Number of host    : " + hosts.size() + "\n");

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
