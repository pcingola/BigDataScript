package org.bds.executioner;

import java.io.ObjectStreamException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.bds.BdsLog;
import org.bds.BdsLogger;
import org.bds.Config;
import org.bds.lang.value.Value;
import org.bds.run.BdsThread;
import org.bds.scope.GlobalScope;

/**
 * Systems that can execute tasks
 *
 * This is a singleton
 *
 * @author pcingola
 */
public class Executioners implements BdsLog {

	/**
	 * Type of executioners
	 */
	public enum ExecutionerType {
		AWS, CLUSTER, FAKE, GENERIC, LOCAL, MESOS, MOAB, PBS, SGE, SLURM, SSH;

		/**
		 * Parse an executioner name
		 * @return Corresponding ExecutionerType or LOCAL if there is any error
		 */
		public static ExecutionerType parseSafe(String exName) {
			// Parse executioner type
			try {
				return ExecutionerType.valueOf(exName.toUpperCase());
			} catch (Exception e) {
				BdsLogger.warning("Unknown system type '" + exName + "', using 'local'");
				return LOCAL;
			}
		}

	}

	// Singleton variable
	private static Executioners executionersInstance = null;

	// Map by type
	private ConcurrentHashMap<ExecutionerType, Executioner> executioners = new ConcurrentHashMap<>();

	Config config;
	boolean freeze;

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
		BdsLogger.debug("Reset");
		if (executionersInstance != null) {
			// Kill all executioners
			BdsLogger.debug("Reset: Kill all executioners");
			for (Executioner ex : executionersInstance.getAll()) {
				BdsLogger.debug("Reset: Killing executioner " + ex.getExecutionerId());
				ex.kill();
			}
		}

		// Reset instance
		BdsLogger.debug("Reset: Clear all executioners");
		executionersInstance = null;
	}

	private Executioners(Config config) {
		if (executionersInstance != null) throw new RuntimeException("Only one instance is allowed! This is a singleton.");
		this.config = config;
		executionersInstance = this;
		debug("Executioners: New instance");
	}

	/**
	 * Create (and start) an executioner
	 */
	private synchronized Executioner factory(ExecutionerType exType, BdsThread bdsThread) {
		Executioner executioner;

		debug("Executioner factory: Creating new executioner type '" + exType + "'");

		switch (exType) {
		case AWS:
			executioner = new ExecutionerCloudAws(config);
			if (bdsThread != null) {
				Value sqsPrefix = bdsThread.getValue(GlobalScope.GLOBAL_VAR_EXECUTIONER_QUEUE_NAME_PREFIX);
				((ExecutionerCloudAws) executioner).setQueueNamePrefix(sqsPrefix.asString());
			}
			break;

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

		case SLURM:
			executioner = new ExecutionerClusterSlurm(config);
			break;

		default:
			throw new RuntimeException("Unknown executioner type '" + exType + "'");
		}

		return executioner;
	}

	public synchronized Executioner get(ExecutionerType exType) {
		return get(exType, null);
	}

	/**
	 * Get an executioner by type
	 */
	public synchronized Executioner get(ExecutionerType exType, BdsThread bdsThread) {
		Executioner ex = executioners.get(exType);

		// Invalid or null? Create a new one
		if ((ex == null) || !ex.isValid()) {
			ex = factory(exType, bdsThread);
			executioners.put(exType, ex); // Cache instance
			ex.start(); // Start thread
		}

		return ex;
	}

	/**
	 * Get an executioner by name
	 */
	public synchronized Executioner get(String exName, BdsThread bdsThread) {
		return get(ExecutionerType.parseSafe(exName), bdsThread);
	}

	/**
	 * Get all available executioners
	 */
	public synchronized Collection<Executioner> getAll() {
		return executioners.values();
	}

	/**
	 * Get an executioner, even if it's null or invalid
	 */
	public synchronized Executioner getRaw(ExecutionerType exType) {
		return executioners.get(exType);
	}

	public boolean isFreeze() {
		return freeze;
	}

	public void kill() {
		debug("Killing executioners");
		for (Executioner executioner : executioners.values())
			executioner.kill();
	}

	/**
	 * Resolve un-serialization
	 */
	private Object readResolve() throws ObjectStreamException {
		executionersInstance = this; // Replace singleton instance
		return this;
	}

	public void setFreeze(boolean freeze) {
		debug("Freeze set to " + freeze);
		this.freeze = freeze;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Executioners: " + executioners.size());
		for (Executioner ex : executioners.values()) {
			sb.append(ex.toString() + "\n");
		}
		return sb.toString();
	}

}
