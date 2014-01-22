package ca.mcgill.mcb.pcingola.bigDataScript.executioner;

import java.io.File;
import java.util.ArrayList;

import ca.mcgill.mcb.pcingola.bigDataScript.Config;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.Cluster;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostInifinte;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostResources;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.Cmd;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.CmdCluster;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Tuple;

/**
 * Execute tasks in a cluster.
 * 
 * All commands are run using 'qsub' (or equivalent) commands
 * 
 * @author pcingola
 */
public class ExecutionerCluster extends Executioner {

	// public static String FAKE_CLUSTER = "";
	public static String FAKE_CLUSTER = Gpr.HOME + "/workspace/BigDataScript/fakeCluster/";

	public static String CLUSTER_EXEC_COMMAND[] = { FAKE_CLUSTER + "qsub" };
	public static String CLUSTER_KILL_COMMAND[] = { FAKE_CLUSTER + "qdel" };
	public static String CLUSTER_BDS_COMMAND = "bds exec ";

	public static final int MIN_EXTRA_TIME = 15;
	public static final int MAX_EXTRA_TIME = 120;

	MonitorExitFile monitorExitFile;

	public ExecutionerCluster(Config config) {
		super(config);

		// Create a cluster having only one host with 'inifinite' capacity
		cluster = new Cluster();
		new HostInifinte(cluster);

		monitorExitFile = new MonitorExitFile();
		monitorExitFile.setDebug(config.isDebug());
	}

	/**
	 * Usually cluster management systems write STDOUT 
	 * & STDERR to files. We don't want the names to 
	 * be the same as the one we use, otherwise program's 
	 * output may be written twice.
	 *   On the other hand, we cannot trust the cluster 
	 * system to write those files, because sometimes 
	 * they don't do it properly, sometimes they add 
	 * headers & footers, sometimes they mixed STDOUT 
	 * and STDERR in a single file, etc.
	 *   
	 * @param fileName
	 * @return
	 */
	String clusterStdFile(String fileName) {
		return fileName + ".cluster";
	}

	@Override
	protected Cmd createCmd(Task task) {
		task.createProgramFile(); // We must create a program file

		// Create command line
		ArrayList<String> args = new ArrayList<String>();
		for (String arg : CLUSTER_EXEC_COMMAND)
			args.add(arg);

		// Add resources request
		HostResources res = task.getResources();
		StringBuilder resSb = new StringBuilder();
		if (res.getCpus() > 0) resSb.append((resSb.length() > 0 ? "," : "") + "nodes=1:ppn=" + res.getCpus());
		if (res.getMem() > 0) resSb.append((resSb.length() > 0 ? "," : "") + "mem=" + res.getMem());

		// Timeout 
		// We want to assign slightly larger timeout to the cluster (qsub/msub), because 
		// we prefer bds to kill the process (it's cleaner and we get exitCode file)
		int realTimeout = (int) res.getTimeout();
		if (realTimeout < 0) realTimeout = 0;
		int extraTime = (int) (realTimeout * 0.1);
		if (extraTime < MIN_EXTRA_TIME) extraTime = MIN_EXTRA_TIME;
		if (extraTime > MAX_EXTRA_TIME) extraTime = MAX_EXTRA_TIME;
		int clusterTimeout = realTimeout + extraTime;

		if (realTimeout > 0) resSb.append((resSb.length() > 0 ? "," : "") + "walltime=" + clusterTimeout);

		// Any resources requested? Add command line
		if (resSb.length() > 0) {
			args.add("-l");
			args.add(resSb.toString());
		}

		// Stdout 
		args.add("-o");
		args.add(clusterStdFile(task.getStdoutFile()));

		// Stderr 
		args.add("-e");
		args.add(clusterStdFile(task.getStderrFile()));

		// Show command string
		String cmdStr = "";
		for (String arg : args)
			cmdStr += arg + " ";

		// Create command to run (it feeds parameters to qsub via stdin)
		StringBuilder cmdStdin = new StringBuilder();
		cmdStdin.append(CLUSTER_BDS_COMMAND);
		cmdStdin.append(realTimeout + " ");
		cmdStdin.append("'" + task.getStdoutFile() + "' ");
		cmdStdin.append("'" + task.getStderrFile() + "' ");
		cmdStdin.append("'" + task.getExitCodeFile() + "' ");
		cmdStdin.append("'" + task.getProgramFileName() + "' ");

		// Run command
		if (debug) Timer.showStdErr("Running command: echo \"" + cmdStdin + "\" | " + cmdStr);

		// Create command 
		CmdCluster cmd = new CmdCluster(task.getId(), args.toArray(Cmd.ARGS_ARRAY_TYPE));
		cmd.setStdin(cmdStdin.toString());
		cmd.setReadPid(true); // We execute using "bds exec" which prints PID number before executing the sub-process

		return cmd;
	}

	/**
	 * This method is called after a task was successfully started 
	 * @param task
	 */
	@Override
	protected void follow(Task task) {
		super.follow(task);
		monitorExitFile.add(this, task); // Start monitoring exit file
	}

	@Override
	protected void followStop(Task task) {
		super.followStop(task);

		monitorExitFile.remove(task);

		// Make sure 'cluster' files are also removed if we are not logging
		if (!log && (task != null)) {
			new File(clusterStdFile(task.getStdoutFile())).deleteOnExit();
			new File(clusterStdFile(task.getStderrFile())).deleteOnExit();
		}
	}

	/**
	 * An OS command to kill this task
	 * @param task
	 * @return
	 */
	@Override
	public String[] osKillCommand(Task task) {
		return CLUSTER_KILL_COMMAND;
	}

	/**
	 * Clean up after run loop
	 */
	@Override
	protected void runExecutionerLoopAfter() {
		super.runExecutionerLoopAfter();
		monitorExitFile.kill(); // Kill taskDone process
	}

	/**
	 * Initialize before run loop
	 */
	@Override
	protected void runExecutionerLoopBefore() {
		monitorExitFile.start(); // Create a 'taskDone' process (get information when a process finishes)
		super.runExecutionerLoopBefore();
	}

	/**
	 * Select next task to run and which host it should run into
	 * For a cluster system, we rely on the cluster management to do this, so here we just schedule the task 
	 * @return
	 */
	@Override
	protected Tuple<Task, Host> selectTask() {
		// Nothing to run?
		if (tasksToRun.isEmpty()) return null;

		for (Task task : tasksToRun) {
			// Already selected? Skip
			if (tasksSelected.containsKey(task)) continue;

			// Can we run this task? 
			if (task.canRun()) {
				// Select host (we only have one)
				for (Host host : cluster) {

					// Do we have enough resources to run this task in this host?
					if (host.getResources().hasResources(task.getResources())) {
						// OK, execute this task in this host						
						add(task, host); // Add task to host (make sure resources are reserved)
						return new Tuple<Task, Host>(task, host);
					}
				}
			}
		}

		// Cannot run any task in any host
		return null;
	}

	@Override
	public synchronized void taskRunning(Task task) {
		super.taskRunning(task);

		// Cmd invoking 'qsub' finished execution => the task is in the cluster now
		String id = task.getId();
		Cmd cmd = cmdById.get(id);
		if (cmd != null) {
			Host host = cmd.getHost();
			remove(task, host); // Remove task form host
			cmdById.remove(id); // Remove command
		}

	}

}
