package org.bds.executioner;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.bds.task.Task;

/**
 * Monitor a task:
 *   - Read messages from the queue
 *   - Check if a task finished
 *
 * @author pcingola
 */
public class MonitorTaskQueue extends MonitorTask implements Serializable {

	private static final long serialVersionUID = 2294558678839375573L;

	List<Task> finished;

	public MonitorTaskQueue() {
		super();
	}

	/** Add to the list if 'finished' tasks
	*/
	public synchronized void addFinished(Task task) {
		if (finished == null) finished = new LinkedList<>();
		finished.add(task);
	}

	/**
	 * Executioner cloud received 'exit' messages from the queue, so it
	 * doesn't need to actively check if a task finished (as opposed to
	 * executionerCluster which actively checks the existence of an 'exitFile')
	 */
	@Override
	public synchronized void check() {
		// Finished tasks are added to 'finished' list by the QueueThread, here we just update those states.
		updateFinished();
	}

	@Override
	protected void updateFinished() {
		// An task to delete?
		if (finished != null) {
			for (Task task : finished) {
				updateFinished(task);
				remove(task); // We don't need to monitor this task any more
			}
			finished = null;
		}
	}

}
