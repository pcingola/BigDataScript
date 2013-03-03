package ca.mcgill.mcb.pcingola.bigDataScript.exec;

import java.util.List;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.Cluster;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;

/**
 * A system that can execute an Exec
 * 
 * @author pcingola
 */
public abstract class Executioner extends Thread {

	protected boolean debug = false;
	protected boolean verbose = false;
	protected boolean running;
	protected int hostIdx = 0;
	protected Cluster cluster;

	public Executioner(Cluster cluster) {
		super();
		this.cluster = cluster;
	}

	/**
	 * Get next host for a given task
	 * 
	 * TODO: Hosts should be configured in a config file
	 * TODO: We have to match capabilities required and hosts available.
	 * 
	 * @return Next available host
	 */
	public Host getNextHost() {
		List<Host> hosts = cluster.getHosts();

		// Any host ready?
		for (int i = 0; i < hosts.size(); i++) {
			Host host = hosts.get((hostIdx++) % hosts.size());
			if (host.getHealth().isAlive()) return host;
		}

		return null;
	}

	/**
	 * Is this executioner running?
	 * @return
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Is this executioner valid?
	 * An Executioner may expire or become otherwise invalid
	 * 
	 * @return
	 */
	public abstract boolean isValid();

	/**
	 * Stop executioner and kill all tasks
	 */
	public void kill() {
		running = false;
	}

	/**
	 * Kill exec if it is already executing or
	 * delete a task from execution queue (if 
	 * it is not already running)
	 * 
	 * @param id
	 * @return
	 */
	public abstract boolean kill(String id);

	/**
	 * Queue an Exec and return a the id
	 * @return
	 */
	public abstract String queue(Task exec);

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	/**
	 * Create an exec for this executioner
	 * @param execId
	 * @param sysFileName
	 * @param commands
	 * @return
	 */
	public abstract Task task(String execId, String sysFileName, String commands);

}
