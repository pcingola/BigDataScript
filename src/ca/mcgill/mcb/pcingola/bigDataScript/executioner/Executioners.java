package ca.mcgill.mcb.pcingola.bigDataScript.executioner;

import java.util.Collection;
import java.util.HashMap;

import ca.mcgill.mcb.pcingola.bigDataScript.Config;

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
		SYS, LOCAL, SSH, CLUSTER, MOAB, SGE, CLOUD;

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
	private Executioner factory(ExecutionerType exType) {
		Executioner executioner;
		switch (exType) {
		case LOCAL:
			executioner = new ExecutionerLocal(config);
			break;
		case SSH:
			executioner = new ExecutionerSsh(config);
			break;
		case CLUSTER: // Cluster defaults to MOAB (probably not a wise default)
		case MOAB:
			executioner = new ExecutionerCluster(config);
			break;
		case SGE:
			executioner = new ExecutionerClusterSge(config);
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
	public synchronized Executioner get(ExecutionerType exType) {
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
	public synchronized Collection<Executioner> getAll() {
		return executioners.values();
	}

}
