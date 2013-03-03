package ca.mcgill.mcb.pcingola.bigDataScript.cluster.host;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.Cluster;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * Host's information
 * 
 * @author pcingola@mcgill.ca
 */
public class Host implements Comparable<Host> {

	public static final int DEFAULT_PORT = 22;

	Cluster cluster;
	String userName; // Username
	String hostName; // Host name (or IP address)
	int port = DEFAULT_PORT; // ssh port
	HostResources resources;
	HostHealth health;

	public Host(Cluster cluster, String hostName) {
		this.cluster = cluster;
		resources = new HostResources();
		health = new HostHealth(this);

		// Parse "user@hostname:port"
		this.hostName = hostName;
		int idx = hostName.indexOf(':');
		if (idx > 0) {
			port = Gpr.parseIntSafe(hostName.substring(idx + 1));
			this.hostName = hostName.substring(0, idx);
		}

		idx = this.hostName.indexOf('@');
		if (idx > 0) {
			userName = this.hostName.substring(0, idx);
			this.hostName = this.hostName.substring(idx + 1);
		} else userName = "";
	}

	@Override
	public int compareTo(Host host) {
		return hostName.compareTo(host.hostName);
	}

	public Cluster getCluster() {
		return cluster;
	}

	public HostHealth getHealth() {
		return health;
	}

	public String getHostName() {
		return hostName;
	}

	public int getPort() {
		return port;
	}

	public HostResources getResources() {
		return resources;
	}

	public String getUserName() {
		return userName;
	}

	@Override
	public String toString() {
		return (userName.isEmpty() ? "" : userName + "@") + hostName + ((port > 0) && (port != DEFAULT_PORT) ? ":" + port : "");
	}
}
