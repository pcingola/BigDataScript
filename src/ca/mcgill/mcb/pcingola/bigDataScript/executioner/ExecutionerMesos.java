package ca.mcgill.mcb.pcingola.bigDataScript.executioner;

import ca.mcgill.mcb.pcingola.bigDataScript.Config;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.Cluster;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostInifinte;
import ca.mcgill.mcb.pcingola.bigDataScript.mesos.BdsMesosFramework;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.Cmd;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.task.TaskState;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * Execute tasks on Mesos
 *
 * @author pcingola
 */
public class ExecutionerMesos extends Executioner {

	public static final String MESOS_MASTER_PROPERTY_NAME = "mesos.master";
	public static final String DEFAULE_MESOS_MASTER = "127.0.0.1:5050";

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
		String master = config.getString(MESOS_MASTER_PROPERTY_NAME, DEFAULE_MESOS_MASTER);
		mesosFramework = new BdsMesosFramework(this, master);
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
		task.setExitValue(Task.EXITCODE_KILLED);
		taskFinished(task, TaskState.KILLED);
	}

	@Override
	public String[] osKillCommand(Task task) {
		// TODO Auto-generated method stub
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

	/**
	 * Select task to be executed on a host
	 */
	@Override
	protected synchronized void selectTask(Task task, Host host) {

		if (verbose) log("Task selected '" + task.getId() + "' on host '" + host + "'");
		tasksSelected.put(task, host);
		host.add(task);
	}

}
