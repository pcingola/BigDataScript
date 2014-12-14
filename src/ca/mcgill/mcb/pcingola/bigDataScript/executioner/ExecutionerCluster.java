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
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.Exec;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.ExecResult;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
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
	protected String clusterPostMortemInfoCommand[];
	protected String clusterAdditionalArgs[];

	protected String bdsCommand = "bds exec ";

	protected String memParam;
	protected String cpuParam;
	protected String wallTimeParam;

	public int MIN_EXTRA_TIMEOUT = 15;
	public int MAX_EXTRA_TIMEOUT = 120;

	String pidPatternStr;
	Pattern pidPattern;

	protected ExecutionerCluster(Config config) {
		super(config);

		// Define commands
		String execCommand[] = { FAKE_CLUSTER + "qsub" };
		String killCommand[] = { FAKE_CLUSTER + "qdel" };
		String statCommand[] = { FAKE_CLUSTER + "qstat" };
		String postMortemInfoCommand[] = { FAKE_CLUSTER + "qstat", "-f" };
		String additionalArgs[] = {};

		clusterExecCommand = execCommand;
		clusterKillCommand = killCommand;
		clusterStatCommand = statCommand;
		clusterPostMortemInfoCommand = postMortemInfoCommand;
		clusterAdditionalArgs = additionalArgs;

		memParam = "mem=";
		cpuParam = "nodes=1:ppn=";
		wallTimeParam = "walltime=";

		// PID regex matcher
		pidPatternStr = config.getString(PID_REGEX, "").trim();
		if (!pidPatternStr.isEmpty()) {
			if (debug) Timer.showStdErr("ExecutionerCluster: Using pidPattern '" + pidPatternStr + "'");
			pidPattern = Pattern.compile(pidPatternStr);
		}

		// Cluster task need monitoring
		monitorTask = config.getMonitorTask();

		// Create a cluster having only one host with 'inifinite' capacity
		cluster = new Cluster();
		new HostInifinte(cluster);
	}

	/**
	 * Add resource options to command line parameters
	 */
	protected void addResources(Task task, List<String> args) {
		StringBuilder resSb = new StringBuilder();

		// Add resources request
		HostResources res = task.getResources();

		long clusterTimeout = calcTimeOut(res);
		String clusterTimeoutStr = Timer.toHHMMSS(1000 * clusterTimeout);

		// Cpu, memory and timeout
		if (res.getCpus() > 0) resSb.append((resSb.length() > 0 ? "," : "") + cpuParam + res.getCpus());
		if (res.getMem() > 0) resSb.append((resSb.length() > 0 ? "," : "") + memParam + res.getMem());
		if (clusterTimeout > 0) resSb.append((resSb.length() > 0 ? "," : "") + wallTimeParam + clusterTimeoutStr);

		// Any resources requested? Add command line
		if (resSb.length() > 0) {
			args.add("-l");
			args.add(resSb.toString());
		}

		// A particular queue was requested?
		String queue = task.getQueue();
		if (queue != null && !queue.isEmpty()) {
			args.add("-q");
			args.add(queue);
		}

	}

	/**
	 * Calculate timeout parameter. We want to assign slightly larger timeout
	 * to the cluster (qsub/msub), because we prefer bds to kill the process (it's
	 * cleaner and we get exitCode file)
	 */
	protected int calcTimeOut(HostResources res) {
		int realTimeout = (int) res.getTimeout();
		if (realTimeout < 0) return 0;

		int extraTime = (int) (realTimeout * 0.1);
		if (extraTime < MIN_EXTRA_TIMEOUT) extraTime = MIN_EXTRA_TIMEOUT;
		if (extraTime > MAX_EXTRA_TIMEOUT) extraTime = MAX_EXTRA_TIMEOUT;

		return realTimeout + extraTime;
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

		// Add additional commands
		for (String arg : clusterAdditionalArgs)
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
		if (checkTasksRunning == null) checkTasksRunning = new CheckTasksRunningCluster(this, joinArgs(clusterStatCommand, clusterAdditionalArgs));
		return checkTasksRunning;
	}

	public String[] getCommandStat() {
		return clusterStatCommand;
	}

	protected String[] joinArgs(String[] argsOri, String[] argsAdditional) {
		ArrayList<String> args = new ArrayList<String>();

		// Cluster kill commands
		for (String arg : argsOri)
			args.add(arg);

		// Add additional commands
		for (String arg : argsAdditional)
			args.add(arg);

		return args.toArray(new String[0]);
	}

	/**
	 * An OS command to kill this task
	 */
	@Override
	public String[] osKillCommand(Task task) {
		return joinArgs(clusterKillCommand, clusterAdditionalArgs);
	}

	/**
	 * Parse PID line from 'qsub' (Cmd)
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
				if (debug) Timer.showStdErr("ExecutionerCluster: Regex '" + pidPatternStr + "' matched '" + pid + "' in line: " + line);
				return pid;
			} else if (verbose || debug) Timer.showStdErr("ExecutionerCluster: Regex '" + pidPatternStr + "' did NOT match line: " + line);
		}

		return line;
	}

	/**
	 * Try to find some 'post-mortem' info about this
	 * task, in order to asses systematic errors.
	 *
	 * @param task
	 */
	@Override
	protected void postMortemInfo(Task task) {
		if (clusterPostMortemInfoCommand == null || clusterPostMortemInfoCommand.length < 1) return;
		if (task.getPid() == null || task.getPid().isEmpty()) return;

		// Prepare command line arguments
		ArrayList<String> args = new ArrayList<String>();
		StringBuilder cmdsb = new StringBuilder();
		for (String arg : clusterPostMortemInfoCommand) {
			args.add(arg);
			cmdsb.append(" " + arg);
		}
		args.add(task.getPid());

		// Run command
		ExecResult cmdExecResult = Exec.exec(args, true);
		if (debug) Gpr.debug("Finding postMortemInfo for task " + task.getId() + ": Command executed. Exit value " + cmdExecResult.exitValue + ". Stdout len: " + cmdExecResult.stdOut.length());

		// Collect the data
		if (cmdExecResult.exitValue == 0) task.setPostMortemInfo(cmdExecResult.stdOut);
		else Timer.showStdErr("Error trying to find out post-mortem info on task (PID '" + task.getPid() + "')." //
				+ "\n\tExit code : " + cmdExecResult.exitValue //
				+ "\n\tStdout    : " + cmdExecResult.stdOut //
				+ "\n\tStderr    : " + cmdExecResult.stdErr //
				);

	}

	@Override
	protected synchronized boolean taskUpdateRunning(Task task) {
		boolean ret = super.taskUpdateRunning(task);
		if (!ret) return false;

		task.getId();
		Cmd cmd = getCmd(task);
		if (cmd != null) {
			Host host = cmd.getHost();
			remove(task, host); // Remove task form host
			removeCmd(task); // Remove command
		}

		return true;
	}

}
