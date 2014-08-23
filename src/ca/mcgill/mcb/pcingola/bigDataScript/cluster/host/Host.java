package ca.mcgill.mcb.pcingola.bigDataScript.cluster.host;

import java.util.HashSet;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.Cluster;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * Host's information
 *
 * @author pcingola@mcgill.ca
 */
public class Host implements Comparable<Host> {

	public static final int DEFAULT_PORT = 22;

	Cluster cluster; // Is this host part of a 'cluster'
	String userName; // Username
	String hostName; // Host name (or IP address)
	int port = DEFAULT_PORT; // Ssh port
	HostResources resources; // Host resources (all cpus, memory, etc)
	HostResources resourcesAvaialble; // Available resources
	HostHealth health;
	HashSet<Task> tasksRunning; // A list of tasks running in this host

	public Host(Cluster cluster, String hostName) {
		this.cluster = cluster;
		init(cluster, hostName);
	}

	public Host(String hostName) {
		cluster = new Cluster();
		init(cluster, hostName);
	}

	/**
	 * Add task to this host
	 * @param task
	 */
	public synchronized void add(Task task) {
		if (tasksRunning.add(task)) updateResources();
	}

	public boolean canUpdateSsh() {
		return true;
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

	public HostResources getResourcesAvaialble() {
		if (resourcesAvaialble == null) updateResources();
		return resourcesAvaialble;
	}

	public String getUserName() {
		return userName;
	}

	void init(Cluster cluster, String hostName) {
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

		tasksRunning = new HashSet<Task>();
		cluster.add(this);
	}

	/**
	 * Remove task from this host
	 */
	public synchronized void remove(Task task) {
		if (tasksRunning.remove(task)) updateResources();
	}

	@Override
	public String toString() {
		return (userName.isEmpty() ? "" : userName + "@") + hostName + ((port > 0) && (port != DEFAULT_PORT) ? ":" + port : "");
	}

	/**
	 * Update 'resources available'
	 */
	protected synchronized void updateResources() {
		resourcesAvaialble = resources.clone();
		for (Task t : tasksRunning)
			resourcesAvaialble.consume(t.getResources());
	}
}
