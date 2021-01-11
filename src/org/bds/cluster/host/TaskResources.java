package org.bds.cluster.host;

import org.bds.executioner.Executioners.ExecutionerType;
import org.bds.run.BdsThread;
import org.bds.scope.GlobalScope;

/**
 * Represents resources consumed by a task
 *
 * @author pcingola
 */
public class TaskResources extends Resources {

	private static final long serialVersionUID = 761051924090735902L;

	/**
	 * Create an apropriate resource type, given an executioner
	 */
	public static TaskResources factory(ExecutionerType exType) {
		switch (exType) {
		case AWS:
			return new TaskResourcesAws();

		case CLUSTER:
		case FAKE:
		case GENERIC:
		case MESOS:
		case MOAB:
		case PBS:
		case SGE:
		case SLURM:
			return new TaskResourcesCluster();

		case LOCAL:
		case SSH:
			return new TaskResources();

		default:
			throw new RuntimeException("Unknown resource type for executioner '" + exType + "'. This should never happen!");
		}
	}

	public TaskResources() {
		super();
	}

	public TaskResources(TaskResources hr) {
		super(hr);
	}

	/**
	 * Set resources from bdsThread
	 * @param bdsThread
	 */
	public void setFromBdsThread(BdsThread bdsThread) {
		debug("Setting resources from bds variables");
		setCpus((int) bdsThread.getInt(GlobalScope.GLOBAL_VAR_TASK_OPTION_CPUS));
		setMem(bdsThread.getInt(GlobalScope.GLOBAL_VAR_TASK_OPTION_MEM));
		setWallTimeout(bdsThread.getInt(GlobalScope.GLOBAL_VAR_TASK_OPTION_WALL_TIMEOUT));
		setTimeout(bdsThread.getInt(GlobalScope.GLOBAL_VAR_TASK_OPTION_TIMEOUT));
	}
}
