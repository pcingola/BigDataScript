package org.bds.cluster.host;

import org.bds.lang.expression.ExpressionTask;
import org.bds.run.BdsThread;

/**
 * Represents resources consumed by a task
 *
 * @author pcingola
 */
public class TaskResources extends Resources {

	private static final long serialVersionUID = 761051924090735902L;

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
		setCpus((int) bdsThread.getInt(ExpressionTask.TASK_OPTION_CPUS));
		setMem(bdsThread.getInt(ExpressionTask.TASK_OPTION_MEM));
		setWallTimeout(bdsThread.getInt(ExpressionTask.TASK_OPTION_WALL_TIMEOUT));
		setTimeout(bdsThread.getInt(ExpressionTask.TASK_OPTION_TIMEOUT));
	}
}
