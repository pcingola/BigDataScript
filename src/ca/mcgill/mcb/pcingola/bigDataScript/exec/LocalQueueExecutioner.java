package ca.mcgill.mcb.pcingola.bigDataScript.exec;

import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.CmdQueue;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * Execute tasks in local computer.
 *  
 * Uses a queue to avoid saturating resources (e.g. number of running 
 * processes in the queue should not exceed number of CPUs)
 * 
 * It is implemented by running a local queue (OsCmdQueue)
 * 
 * @author pcingola
 */
public class LocalQueueExecutioner extends Executioner {

	protected CmdQueue taskQueue;
	protected int sleepTime = 250; // Default sleep time

	public LocalQueueExecutioner() {
		super();
		taskQueue = new CmdQueue(null);
		taskQueue.setVerbose(verbose);
	}

	@Override
	public synchronized void add(Task task) {
		throw new RuntimeException("UNIMPLEMENTED!!");
	}

	@Override
	public synchronized boolean isValid() {
		return taskQueue.isRunning() || taskQueue.hasTaskToRun();
	}

	@Override
	public synchronized void kill() {
		super.kill();

		// Kill queue
		taskQueue.kill();
	}

	@Override
	public synchronized boolean kill(String id) {
		if (debug) Gpr.debug("Killing task: " + id);
		taskQueue.kill(id);
		return true;
	}

	@Override
	public void run() {
		running = true;

		while (running) {
			taskQueue.run();

			// Wait a bit before running it again
			try {
				if (running) Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				running = false;
				e.printStackTrace();
			}
		}

		running = false;
	}
}
