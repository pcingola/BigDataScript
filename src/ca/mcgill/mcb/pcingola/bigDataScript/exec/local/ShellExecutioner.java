package ca.mcgill.mcb.pcingola.bigDataScript.exec.local;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.Cluster;
import ca.mcgill.mcb.pcingola.bigDataScript.exec.Executioner;
import ca.mcgill.mcb.pcingola.bigDataScript.exec.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.CmdRunner;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.TaskQueue;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * Execute tasks in local computer
 * 
 * It is implemented by running a local queue (OsCmdQueue)
 * 
 * @author pcingola
 */
public class ShellExecutioner extends Executioner {

	protected TaskQueue taskQueue;
	protected int sleepTime = 250; // Default sleep time

	public ShellExecutioner(Cluster cluster) {
		super(cluster);
		taskQueue = new TaskQueue(cluster);
		taskQueue.setVerbose(true);
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
	public synchronized String queue(Task task) {
		if (debug) Gpr.debug("Queuing task: " + task.getId());
		CmdRunner oscmd = task.cmdRunner();
		taskQueue.add(oscmd);
		return oscmd.getCmdId();
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

	@Override
	public Task task(String execId, String sysFileName, String commands) {
		return new ShellTask(execId, sysFileName, commands);
	}
}
