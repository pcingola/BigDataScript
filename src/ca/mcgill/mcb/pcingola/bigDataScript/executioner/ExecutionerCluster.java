package ca.mcgill.mcb.pcingola.bigDataScript.executioner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.mcgill.mcb.pcingola.bigDataScript.Config;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.Cluster;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostInifinte;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostResources;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.Cmd;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.CmdCluster;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * Execute tasks in a MOAB cluster.
 * 
 * All commands are run using 'qsub' (or equivalent) commands
 * 
 * @author pcingola
 */
public class ExecutionerCluster extends Executioner {

	// FAKE_CLUSTER: Only used for debugging when you don't have a cluster (or don't want to wait for cluster's respose)
	//	public static String FAKE_CLUSTER = Gpr.HOME + "/workspace/BigDataScript/fakeCluster/";
	public static String FAKE_CLUSTER = "";

	protected String clusterExecCommand[];
	protected String clusterKillCommand[];
	protected String clusterStatCommand[];

	protected String bdsCommand = "bds exec ";

	public int MIN_EXTRA_TIMEOUT = 15;
	public int MAX_EXTRA_TIMEOUT = 120;

	Pattern pidPattern;

	public ExecutionerCluster(Config config) {
		super(config);

		// Define commnads
		String execCommand[] = { FAKE_CLUSTER + "qsub" };
		String killCommand[] = { FAKE_CLUSTER + "qdel" };
		String statCommand[] = { FAKE_CLUSTER + "qstat" };

		clusterExecCommand = execCommand;
		clusterKillCommand = killCommand;
		clusterStatCommand = statCommand;

		// Cluster task need monitoring
		monitorTask = config.getMonitorTask();

		// Create a cluster having only one host with 'inifinite' capacity
		cluster = new Cluster();
		new HostInifinte(cluster);
	}

	/**
	 * Add resource options to command line parameters
	 * 
	 * @param task
	 * @param args
	 */
	protected void addResources(Task task, List<String> args) {
		StringBuilder resSb = new StringBuilder();

		// Add resources request
		HostResources res = task.getResources();

		int clusterTimeout = calcTimeOut(res);

		// MOAB style
		if (res.getCpus() > 0) resSb.append((resSb.length() > 0 ? "," : "") + "nodes=1:ppn=" + res.getCpus());
		if (res.getMem() > 0) resSb.append((resSb.length() > 0 ? "," : "") + "mem=" + res.getMem());
		if (clusterTimeout > 0) resSb.append((resSb.length() > 0 ? "," : "") + "walltime=" + clusterTimeout);

		// Any resources requested? Add command line
		if (resSb.length() > 0) {
			args.add("-l");
			args.add(resSb.toString());
		}
	}

	/**
	 * Calculate timeout parameter. We want to assign slightly larger timeout 
	 * to the cluster (qsub/msub), because we prefer bds to kill the process (it's 
	 * cleaner and we get exitCode file)
	 * 
	 * @param res
	 * @return
	 */
	protected int calcTimeOut(HostResources res) {
		int realTimeout = (int) res.getTimeout();
		if (realTimeout < 0) return 0;

		int extraTime = (int) (realTimeout * 0.1);
		if (extraTime < MIN_EXTRA_TIMEOUT) extraTime = MIN_EXTRA_TIMEOUT;
		if (extraTime > MAX_EXTRA_TIMEOUT) extraTime = MAX_EXTRA_TIMEOUT;
		int clusterTimeout = realTimeout + extraTime;

		return clusterTimeout;
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

		if (debug) Timer.showStdErr("Executioner: " + this.getClass().getSimpleName() + ", task " + task.getId());

		// Create command line
		ArrayList<String> args = new ArrayList<String>();
		for (String arg : clusterExecCommand)
			args.add(arg);

		// Add resources request
		HostResources res = task.getResources();
		int realTimeout = (int) res.getTimeout();

		// Add resources to command line parameters
		addResources(task, args);

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
		cmdStdin.append(bdsCommand);
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
		if (debug) Timer.showStdErr("Command (CmdCluster): " + cmd);

		return cmd;
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

	@Override
	protected CheckTasksRunning getCheckTasksRunning() {
		if (checkTasksRunning == null) checkTasksRunning = new CheckTasksRunningCluster(this, clusterStatCommand);
		return checkTasksRunning;
	}

	public String[] getCommandStat() {
		return clusterStatCommand;
	}

	/**
	 * An OS command to kill this task
	 * @param task
	 * @return
	 */
	@Override
	public String[] osKillCommand(Task task) {
		return clusterKillCommand;
	}

	/**
	 * Parse PID line from 'qsub' (Cmd)
	 * @param line
	 * @return
	 */
	@Override
	public String parsePidLine(String line) {
		line = line.trim();
		if (line.isEmpty()) return "";

		if (pidPattern != null) {
			// Pattern pattern = Pattern.compile("Your job (\\S+)");
			Matcher matcher = pidPattern.matcher(line);
			if (matcher.find()) {
				String pid = matcher.group(1);
				return pid;
			}
		}

		return line;
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
