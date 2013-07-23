package ca.mcgill.mcb.pcingola.bigDataScript.exec;

import java.util.Collection;
import java.util.HashMap;

import ca.mcgill.mcb.pcingola.bigDataScript.Config;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.Cluster;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostLocal;

/**
 * Systems that can execute tasks
 * 
 * This is a singleton
 * 
 * @author pcingola
 */
public class Executioners {

	/**
	 * Type of executioners
	 * @author pcingola
	 *
	 */
	public enum ExecutionerType {
		SYS, LOCAL, SSH, CLUSTER, CLOUD;

		/**
		 * Parse an executioner name
		 * @param exName
		 * @return Corresponding ExecutionerType or LOCAL if there is any error
		 */
		public static ExecutionerType parseSafe(String exName) {
			// Parse executioner type
			try {
				return ExecutionerType.valueOf(exName.toUpperCase());
			} catch (Exception e) {
				return LOCAL;
			}
		}

	}

	// Singleton variable
	private static Executioners executionersInstance = null;;

	Config config;
	HashMap<ExecutionerType, Executioner> executioners = new HashMap<ExecutionerType, Executioner>();
	PidLogger pidLogger;

	/**
	 * Get instance
	 * @return
	 */
	public static Executioners getInstance() {
		return executionersInstance;
	}

	public static Executioners getInstance(Config config) {
		if (executionersInstance == null) executionersInstance = new Executioners(config);
		return executionersInstance;
	}

	private Executioners(Config config) {
		if (executionersInstance != null) throw new RuntimeException("Only one instance is allowed! This is a singleton.");
		this.config = config;
		executionersInstance = this;
	}

	/**
	 * Create (and start) an executioner
	 * @param exName
	 * @return
	 */
	public Executioner factory(ExecutionerType exType) {
		Executioner executioner;
		Cluster cluster;

		switch (exType) {
		case SYS:

			//---
			// Create local 'sys' executioner
			//---

			// Create 'local'
			cluster = new Cluster();
			cluster.add(new HostLocal());
			executioner = new LocalExecutioner(getPidLogger());
			break;
		case LOCAL:

			//---
			// Create local executioner
			//---

			// Create 'local' cluster' (a cluster having only this computer)
			cluster = new Cluster();
			cluster.add(new HostLocal());
			executioner = new LocalQueueExecutioner(getPidLogger());
			break;

		case SSH:
			//---
			// Create ssh executioner
			//---

			// Create a cluster 
			cluster = new Cluster();

			// Add nodes from config file
			for (String sshNode : executionersInstance.config.getSshNodes())
				cluster.add(new Host(cluster, sshNode));

			executioner = new SshExecutioner(cluster);

			break;
		case CLUSTER:
			//---
			// Create cluster executioner
			//---
			executioner = new ClusterExecutioner(getPidLogger());
			break;

		default:
			throw new RuntimeException("Unknown executioner type '" + exType + "'");
		}

		// Set flags
		executioner.setVerbose(config.isVerbose());
		executioner.setDebug(config.isDebug());
		executioner.setLog(config.isLog());

		// Start thread
		executioner.start();
		return executioner;

	}

	/**
	 * Get an executioner
	 * @param exName
	 * @return
	 */
	public Executioner get(ExecutionerType exType) {
		Executioner ex = executioners.get(exType);

		// Invalid or null? Create a new one
		if ((ex == null) || !ex.isValid()) {
			ex = factory(exType);
			executioners.put(exType, ex);
		}

		return ex;
	}

	/**
	 * Get an executioner
	 * @param exName
	 * @return
	 */
	public Executioner get(String exName) {
		return get(ExecutionerType.parseSafe(exName));
	}

	/**
	 * Get all available exectioners
	 * @return
	 */
	public Collection<Executioner> getAll() {
		return executioners.values();
	}

	public PidLogger getPidLogger() {
		if (pidLogger == null) {
			pidLogger = new PidLogger(config.getPidFile());
		}
		return pidLogger;
	}

}
