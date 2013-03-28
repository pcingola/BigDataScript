package ca.mcgill.mcb.pcingola.bigDataScript.exec;

import java.util.ArrayList;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.Cluster;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostResources;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.CmdRunner;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.CmdRunnerCluster;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * Execute tasks in a cluster.
 * 
 * All commands are run using 'qsub' (or equivalent) commands
 * 
 * @author pcingola
 */
public class ClusterExecutioner extends LocalExecutioner {

	public static String FAKE_CLUSTER = "";
	// public static String FAKE_CLUSTER = Gpr.HOME + "/workspace/BigDataScript/fakeCluster/";
	public static String CLUSTER_EXEC_COMMAND[] = { FAKE_CLUSTER + "msub" };
	public static String CLUSTER_KILL_COMMAND[] = { FAKE_CLUSTER + "canceljob" };
	public static String CLUSTER_BDS_COMMAND = "bds qexec ";

	public static final int MIN_EXTRA_TIME = 15;
	public static final int MAX_EXTRA_TIME = 120;

	TaskDone taskDone;

	public ClusterExecutioner(Cluster cluster) {
		super(null);
		taskDone = new TaskDone();

	}

	@Override
	protected void addTail(Task task) {
		tail.add(task.getStdoutFile(), null, false);
		tail.add(task.getStderrFile(), null, true);
	}

	@Override
	CmdRunner createCmdRunner(Task task) {
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
		args.add(task.getStdoutFile());

		// Stderr 
		args.add("-e");
		args.add(task.getStderrFile());

		// Show command string
		String cmdStr = "";
		for (String arg : args)
			cmdStr += arg + " ";

		// Create command to run (it feed to qsub via stdin)
		StringBuilder cmdStdin = new StringBuilder();
		cmdStdin.append(CLUSTER_BDS_COMMAND);
		cmdStdin.append(realTimeout + " ");
		cmdStdin.append(task.getExitCodeFile() + " ");
		cmdStdin.append(task.getProgramFileName());

		// Run command
		if (debug) Timer.showStdErr("Running command: echo \"" + cmdStdin + "\" | " + cmdStr);

		// Create command 
		CmdRunnerCluster cmd = new CmdRunnerCluster(task.getId(), args.toArray(CmdRunner.ARGS_ARRAY_TYPE));
		cmd.setTask(task);
		cmd.setExecutioner(this);
		cmd.setStdin(cmdStdin.toString());
		cmd.setReadPid(true); // We execute using "qsub" which prints a jobID to stdout

		cmdById.put(task.getId(), cmd); // Add to map

		return cmd;
	}

	/**
	 * Clean up after run loop
	 */
	@Override
	protected void runLoopAfter() {
		super.runLoopAfter();
		taskDone.kill(); // Kill taskDone process
	}

	/**
	 * Initialize before run loop
	 */
	@Override
	protected void runLoopBefore() {
		taskDone.start(); // Create a 'taskDone' process (get information when a process finishes)
		super.runLoopBefore();
	}

	/**
	 * This method is called after a task was successfully started 
	 * @param task
	 */
	@Override
	protected void runTaskStarted(Task task) {
		super.runTaskStarted(task);
		taskDone.add(this, task);
	}

	@Override
	public void setDebug(boolean debug) {
		super.setDebug(debug);
		taskDone.setDebug(debug);
	}
}
