package org.bds.cluster.host;

import java.util.HashSet;
import java.util.Set;

import org.bds.cluster.ComputerSystem;
import org.bds.task.Task;
import org.bds.util.Gpr;

/**
 * Host's information
 *
 * @author pcingola@mcgill.ca
 */
public class Host implements Comparable<Host> {

	public static final int DEFAULT_PORT = 22;

	ComputerSystem system; // Is this host part of a 'cluster'
	String userName; // Username
	String hostName; // Host name (or IP address)
	int port = DEFAULT_PORT; // Ssh port
	HostResources resources; // Host resources (all cpus, memory, etc)
	HostResources resourcesAvaialble; // Available resources
	Set<Task> tasksRunning; // A list of tasks running in this host

	public Host(ComputerSystem system, String hostName) {
		this.system = system;
		init(system, hostName);
	}

	public Host(String hostName) {
		system = new ComputerSystem();
		init(system, hostName);
	}

	/**
	 * Add task to this host
	 */
	public synchronized void add(Task task) {
		if (tasksRunning.add(task)) updateResourcesAvailable();
	}

	@Override
	public int compareTo(Host host) {
		return hostName.compareTo(host.hostName);
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

	public synchronized HostResources getResourcesAvaialble() {
		if (resourcesAvaialble == null) updateResourcesAvailable();
		return resourcesAvaialble;
	}

	public ComputerSystem getSystem() {
		return system;
	}

	public String getUserName() {
		return userName;
	}

	public boolean hasResourcesAvailable() {
		return getResourcesAvaialble().isConsumed();
	}

	public boolean hasResourcesAvailable(Resources hr) {
		return getResourcesAvaialble().compareTo(hr) >= 0;
	}

	void init(ComputerSystem cluster, String hostName) {
		resources = new HostResources();

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
		} else {
			userName = System.getProperty("user.name");
		}

		tasksRunning = new HashSet<>();
		cluster.add(this);
	}

	public boolean isAlive() {
		return true;
	}

	/**
	 * Remove task from this host
	 */
	public synchronized void remove(Task task) {
		if (tasksRunning.remove(task)) updateResourcesAvailable();
	}

	@Override
	public String toString() {
		return (userName.isEmpty() ? "" : userName + "@") + hostName + ((port > 0) && (port != DEFAULT_PORT) ? ":" + port : "");
	}

	/**
	 * Update 'resources available'
	 */
	public synchronized void updateResourcesAvailable() {
		resourcesAvaialble = (HostResources) resources.clone();
		for (Task t : tasksRunning)
			resourcesAvaialble.consume(t.getResources());
	}
}
