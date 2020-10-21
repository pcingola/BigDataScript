package org.bds.executioner;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Random;

/**
 * Monitor a task:
 *   - Read messages from the queue
 *   - Check if a task finished
 *
 * @author pcingola
 */
public abstract class MonitorTaskQueue extends MonitorTask implements Serializable {

	private static final long serialVersionUID = 2294558678839375573L;

	protected String queueName;

	public MonitorTaskQueue() {
		super();
	}

	/**
	 * Create queue
	 */
	public abstract void createQueue();

	/**
	 * Delete queue
	 */
	public abstract void deleteQueue();

	/**
	 * Create a unique name according to queue name restrictions
	 */
	protected String getQueueName() {
		if (queueName == null) {
			String r = Long.toHexString(Math.abs((new Random()).nextLong())); // Long random number in hex
			queueName = String.format("bds_%2$tY%2$tm%2$td_%2$tH%2$tM%2$tS_%2$tL_%s", Calendar.getInstance(), r);
		}
		return queueName;
	}

	/**
	 * Is there an active queue?
	 */
	public abstract boolean hasQueue();

	/**
	 * OS command to to delete task queue
	 */
	public abstract String osDeleteQueueCommand();

}
