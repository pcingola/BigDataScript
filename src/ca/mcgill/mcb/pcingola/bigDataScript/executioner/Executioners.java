package ca.mcgill.mcb.pcingola.bigDataScript.executioner;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import ca.mcgill.mcb.pcingola.bigDataScript.Config;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

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
	 */
	public enum ExecutionerType {
		CLUSTER, FAKE, GENERIC, LOCAL, MESOS, MOAB, PBS, SGE, SSH, SYS;

		/**
		 * Parse an executioner name
		 * @return Corresponding ExecutionerType or LOCAL if there is any error
		 */
		public static ExecutionerType parseSafe(String exName) {
			// Parse executioner type
			try {
				return ExecutionerType.valueOf(exName.toUpperCase());
			} catch (Exception e) {
				System.out.println("UNknown system type '" + exName + "', using 'local'");
				return LOCAL;
			}
		}

	}

	// Singleton variable
	private static Executioners executionersInstance = null;

	// Map by type
	private ConcurrentHashMap<ExecutionerType, Executioner> executioners = new ConcurrentHashMap<ExecutionerType, Executioner>();

	Config config;

	/**
	 * Get instance
	 */
	public static Executioners getInstance() {
		return executionersInstance;
	}

	/**
	 * Get or create instance
	 */
	public static synchronized Executioners getInstance(Config config) {
		if (executionersInstance == null) executionersInstance = new Executioners(config);
		return executionersInstance;
	}

	/**
	 * Reset this singleton
	 */
	public static void reset() {
		if (executionersInstance != null) {
			// Kill all executioners
			for (Executioner ex : executionersInstance.getAll())
				ex.kill();
		}

		// Reset instance
		executionersInstance = null;
	}

	private Executioners(Config config) {
		if (executionersInstance != null) throw new RuntimeException("Only one instance is allowed! This is a singleton.");
		this.config = config;
		executionersInstance = this;
	}

	/**
	 * Create (and start) an executioner
	 */
	private synchronized Executioner factory(ExecutionerType exType) {
		Executioner executioner;

		if (config.isVerbose()) Timer.showStdErr("Executioner factory: Creating new executioner type '" + exType + "'");

		switch (exType) {
		case CLUSTER:
			executioner = new ExecutionerCluster(config);
			break;

		case FAKE:
			executioner = new ExecutionerClusterFake(config);
			break;

		case GENERIC:
			executioner = new ExecutionerClusterGeneric(config);
			break;

		case LOCAL:
			executioner = new ExecutionerLocal(config);
			break;

		case MESOS:
			executioner = new ExecutionerMesos(config);
			break;

		case MOAB:
			executioner = new ExecutionerClusterMoab(config);
			break;

		case PBS:
			executioner = new ExecutionerClusterPbs(config);
			break;

		case SSH:
			executioner = new ExecutionerSsh(config);
			break;

		case SGE:
			executioner = new ExecutionerClusterSge(config);
			break;

		default:
			throw new RuntimeException("Unknown executioner type '" + exType + "'");
		}

		// Set some parameters
		executioner.setVerbose(config.isVerbose());
		executioner.setDebug(config.isDebug());
		executioner.setLog(config.isLog());

		return executioner;
	}

	/**
	 * Get an executioner by type
	 */
	public synchronized Executioner get(ExecutionerType exType) {
		Executioner ex = executioners.get(exType);

		// Invalid or null? Create a new one
		if ((ex == null) || !ex.isValid()) {
			ex = factory(exType);
			executioners.put(exType, ex); // Cache instance
			ex.start(); // Start thread
		}

		return ex;
	}

	/**
	 * Get an executioner by name
	 */
	public synchronized Executioner get(String exName) {
		return get(ExecutionerType.parseSafe(exName));
	}

	/**
	 * Get all available executioners
	 */
	public synchronized Collection<Executioner> getAll() {
		return executioners.values();
	}

}
