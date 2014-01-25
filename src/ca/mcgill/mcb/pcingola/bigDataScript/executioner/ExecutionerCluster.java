package ca.mcgill.mcb.pcingola.bigDataScript.executioner;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import ca.mcgill.mcb.pcingola.bigDataScript.Config;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.Cluster;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostInifinte;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostResources;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.Cmd;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.CmdCluster;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.Exec;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.ExecResult;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task.TaskState;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

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

	public int MIN_QUEUE_TIME = 3; // We assume that in less then this number of seconds we might not have a task reported by the cluster system
	public int CLUSTER_STAT_INTERVAL = 5;

	public String CLUSTER_EXEC_COMMAND[] = { FAKE_CLUSTER + "qsub" };
	public String CLUSTER_KILL_COMMAND[] = { FAKE_CLUSTER + "qdel" };
	public String CLUSTER_STAT_COMMAND[] = { FAKE_CLUSTER + "qstat" };
	public String CLUSTER_BDS_COMMAND = "bds exec ";

	public int MIN_EXTRA_TIME = 15;
	public int MAX_EXTRA_TIME = 120;

	protected Timer timeClusterStat;

	public ExecutionerCluster(Config config) {
		super(config);

		// Cluster task need monitoring
		monitorTask = config.getMonitorTask();

		// Create a cluster having only one host with 'inifinite' capacity
		cluster = new Cluster();
		new HostInifinte(cluster);
	}

	/**
	 * Get states for all tasks running in the cluster
	 * @return
	 */
	HashMap<String, String> clusterStat() {
		HashMap<String, String> stats = new HashMap<String, String>();
		return stats;
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
	 * Find a running task given a PID
	 * @param pid
	 * @param state
	 */
	protected Task findRunningTaskByPid(String pid) {
		String pidPart = pid.split("\\.")[0]; // Use only the first part before '.'

		// Find task by PID
		for (Task t : tasksRunning.values())
			if (t.getPid().equals(pid) || t.getPid().equals(pidPart)) return t;

		return null;
	}

	@Override
	protected void followStop(Task task) {
		super.followStop(task);

		// Make sure 'cluster' files are also removed if we are not logging
		if (!log && (task != null)) {
			new File(clusterStdFile(task.getStdoutFile())).deleteOnExit();
			new File(clusterStdFile(task.getStderrFile())).deleteOnExit();
		}
	}

	/**
	 * Check that task are still scheduled in the cluster system
	 * 
	 * TODO: We should try to implement an XML parsing. Unfortunately, some 
	 * 		 clusters do not have 'qstat -xml' option (yikes!) 
	 */
	protected void monitorTaskCluster() {
		Gpr.debug("QSTAT");

		//---
		// Run command (qstat)
		//---

		// Prepare command line arguments
		ArrayList<String> args = new ArrayList<String>();
		StringBuilder cmdsb = new StringBuilder();
		for (String arg : CLUSTER_STAT_COMMAND) {
			args.add(arg);
			cmdsb.append(" " + arg);
		}

		// Execute command
		ExecResult execResult = Exec.exec(args, true);

		// Any problems? Report
		if (execResult.exitValue > 0) {
			Timer.showStdErr("WARNING: There was an error executing cluster stat command: '" + cmdsb.toString().trim() + "'");
			return;
		}

		//---
		// Parse command's output
		//---
		String stdout = execResult.stdOut;

		HashSet<String> taskFoundId = new HashSet<String>();
		for (String line : stdout.split("\n")) {
			// Parse fields
			String fields[] = line.split("\\s+");

			if (fields.length > 1) {
				String pid = fields[0]; // We only obtain the PID
				Task task = findRunningTaskByPid(pid);
				if (task != null) {
					taskFoundId.add(task.getId());
					Gpr.debug("FOUND: " + task.getPid());
				}
			}
		}

		// Any 'running' task that was not found should be marked asMark tasks as failed
		LinkedList<Task> finished = null;
		for (Task task : tasksRunning.values())
			if (!taskFoundId.contains(task.getId()) // Task not found in cluster's queue?
					&& (task.elapsedSecs() > MIN_QUEUE_TIME) // Make sure that it's been running for a while (otherwise it might that the task has just started and the cluster is not reporting it yet)
			) {
				if (finished == null) finished = new LinkedList<Task>();
				finished.add(task);
			}

		// Remove task that have been running for while, but are no longer in the cluster's queue
		if (finished != null) {
			for (Task task : finished) {
				Gpr.debug("TASK NOT FOUND:\tID '" + task.getId() + "'\tPID '" + task.getPid() + "'");
				task.setErrorMsg("Task dissapeared from cluster's queue. Task or node failure?");
				taskFinished(task, TaskState.ERROR, Task.EXITCODE_ERROR);
			}
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

	@Override
	protected boolean runExecutionerLoop() {
		boolean ret = super.runExecutionerLoop();
		if (isClusterStatTime()) monitorTaskCluster(); // Check that task are still scheduled in the cluster system
		return ret;
	}

	/**
	 * Should we show a report?
	 * @return
	 */
	protected boolean isClusterStatTime() {
		if (timeClusterStat == null) timeClusterStat = new Timer();
		if (timeClusterStat.elapsedSecs() > CLUSTER_STAT_INTERVAL) {
			timeClusterStat.start(); // Restart timer
			return true;
		}
		return false;
	}

	/**
	 * Clean up after run loop
	 */
	@Override
	protected void runExecutionerLoopAfter() {
		super.runExecutionerLoopAfter();
		monitorTask.kill(); // Kill taskDone process
	}

	/**
	 * Initialize before run loop
	 */
	@Override
	protected void runExecutionerLoopBefore() {
		monitorTask.start(); // Create a 'taskDone' process (get information when a process finishes)
		super.runExecutionerLoopBefore();
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
