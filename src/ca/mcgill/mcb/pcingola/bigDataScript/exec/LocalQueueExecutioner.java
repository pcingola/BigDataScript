package ca.mcgill.mcb.pcingola.bigDataScript.exec;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostLocal;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostResources;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

//import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * Execute tasks in local computer.
 *  
 * Uses a queue to avoid saturating resources (e.g. number of running 
 * processes in the queue should not exceed number of CPUs)
 * 
 * @author pcingola
 */
public class LocalQueueExecutioner extends LocalExecutioner {

	Host host; // Local computer is the 'server' (localhost)

	public LocalQueueExecutioner() {
		super(null);
		host = new HostLocal();
	}

	/**
	 * We want the same behaviour as Executioner.add
	 */
	@Override
	public void add(Task task) {
		if (verbose) Timer.showStdErr("Queuing task '" + task.getId() + "'");
		tasksToRun.add(task);
	}

	/**
	 * Can we run the next task?
	 * @return true is there at least one host with enough resources to run the next task
	 */
	synchronized boolean canRunNextCmd() {
		if (tasksToRun.isEmpty()) return true;

		// Are running tasks are consuming all resources?
		HostResources res = host.getResources().clone();
		for (Task t : tasksRunning.values()) {
			// Gpr.debug("Task resources: " + t.getResources() + "\tLeft: " + res);
			res.consume(t.getResources());
		}

		return res.isValid();
	}

	/**
	 * Run task
	 */
	@Override
	protected boolean runLoop() {
		// Nothing to run?
		if (!hasTaskToRun()) return false;

		// Are there any more task to run?
		while (running && hasTaskToRun()) {
			// Can we run the next task?
			if (canRunNextCmd()) {
				// Get next task and run it
				runNext(host);
			} else {
				sleepShort();
				if (debug) Timer.showStdErr("Queue tasks:\tPending : " + tasksToRun.size() + "\tRunning: " + tasksRunning.size() + "\tDone: " + tasksDone.size());
			}
		}

		return true;
	}
}
