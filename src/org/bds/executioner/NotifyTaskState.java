package org.bds.executioner;

import org.bds.task.Task;
import org.bds.task.TaskState;

/**
 * Notify task state change
 *
 * @author pcingola
 */
public interface NotifyTaskState {

	/**
	 * Task has finished. It may have finished with an error condition.
	 */
	public void taskFinished(Task task, TaskState taskState);

	/**
	 * Task running
	 */
	public void taskRunning(Task task);

	/**
	 * Task started
	 */
	public void taskStarted(Task task);

}
