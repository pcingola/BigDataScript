package org.bds.cluster.host;

import org.bds.lang.expression.ExpressionTask;
import org.bds.run.BdsThread;

/**
 * Represents resources consumed by a task
 *
 * @author pcingola
 */
public class TaskResourcesCluster extends TaskResources {

	private static final long serialVersionUID = -1849044143332368043L;

	protected String queue; // Preferred execution queue

	public TaskResourcesCluster() {
		super();
	}

	public TaskResourcesCluster(TaskResourcesCluster hr) {
		super(hr);
	}

	public String getQueue() {
		return queue;
	}

	@Override
	public void setFromBdsThread(BdsThread bdsThread) {
		super.setFromBdsThread(bdsThread);
		setQueue(bdsThread.getString(ExpressionTask.TASK_OPTION_QUEUE));
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

}
