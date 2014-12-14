package ca.mcgill.mcb.pcingola.bigDataScript.executioner;

import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.task.TaskState;

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
