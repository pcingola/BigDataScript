package org.bds.executioner;

import org.bds.Config;
import org.bds.cluster.Cluster;
import org.bds.cluster.host.HostInifinte;
import org.bds.mesos.BdsMesosFramework;
import org.bds.osCmd.Cmd;
import org.bds.run.BdsThread;
import org.bds.task.Task;
import org.bds.task.TaskState;
import org.bds.util.Timer;

/**
 * Execute tasks on Mesos
 *
 * @author pcingola
 */
public class ExecutionerMesos extends Executioner {

	public static final String MESOS_MASTER_PROPERTY_NAME = "mesos.master";
	public static final String DEFAULT_MESOS_MASTER = "127.0.1.1:5050";

	BdsMesosFramework mesosFramework;

	public ExecutionerMesos(Config config) {
		super(config);
		removeTaskCannotExecute = false; // In Mesos, host might appear and disappear all the time (according to offer).

		// Create a cluster and add an infinite capacity host (master)
		cluster = new Cluster();
		new HostInifinte(cluster);
	}

	/**
	 * Create a command form a task
	 */
	@Override
	protected synchronized Cmd createRunCmd(Task task) {
		task.createProgramFile(); // We must create a program file
		mesosFramework.add(task);
		return null;
	}

	/**
	 * Initialize Mesos framework
	 */
	void initMesos() {
		// Initialize framework
		String master = config.getString(MESOS_MASTER_PROPERTY_NAME, DEFAULT_MESOS_MASTER);
		mesosFramework = new BdsMesosFramework(this, master);
		mesosFramework.setVerbose(verbose);
		mesosFramework.setDebug(debug);
		mesosFramework.start();

		// Wait for Mesos connection
		try {
			while (!mesosFramework.isReady())
				Thread.sleep(SLEEP_TIME_MID);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Kill a task and move it from 'taskRunning' to 'tasksDone'
	 */
	@Override
	public synchronized void kill(Task task) {
		if (task.isDone()) return; // Nothing to do

		if (debug) Timer.showStdErr("Killing task '" + task.getId() + "'");

		// Tell Mesos to kill the task
		mesosFramework.kill(task);

		// Mark task as finished
		// Note: This will also be invoked by Cmd, so it will be redundant)
		task.setExitValue(BdsThread.EXITCODE_KILLED);
		taskFinished(task, TaskState.KILLED);
	}

	@Override
	public String[] osKillCommand(Task task) {
		return null;
	}

	/**
	 * Clean up after run loop
	 */
	@Override
	protected void runExecutionerLoopAfter() {
		super.runExecutionerLoopAfter();
		if (mesosFramework != null) mesosFramework.kill();
	}

	@Override
	protected void runExecutionerLoopBefore() {
		super.runExecutionerLoopBefore();
		initMesos();
	}

}
