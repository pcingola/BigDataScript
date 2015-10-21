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
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * Execute tasks in a MOAB cluster.
 *
 * All commands are run using 'qsub' (or equivalent) commands
 *
 * @author pcingola
 */
public class ExecutionerCluster extends Executioner {

	protected String clusterRunCommand[];
	protected String clusterKillCommand[];
	protected String clusterStatCommand[];
	protected String clusterPostMortemInfoCommand[];

	protected String clusterRunCommandStdOutOption = "-o";
	protected String clusterRunCommandStdErrOption = "-e";

	protected String clusterRunAdditionalArgs[];
	protected String clusterKillAdditionalArgs[];
	protected String clusterStatAdditionalArgs[];
	protected String clusterPostMortemAdditionalArgs[];

	protected String bdsCommand = "bds exec ";

	protected String memParam;
	protected String cpuParam;
	protected String wallTimeParam;

	protected boolean postMortemDisabled; // Disable post-mortem taks info?

	public int MIN_EXTRA_TIMEOUT = 15;
	public int MAX_EXTRA_TIMEOUT = 120;

	protected String pidRegexStr; // Regular expression matching a PID from 'qsub' command
	protected Pattern pidRegex; // Regular expression (compiled) matching a PID from 'qsub' command

	protected ExecutionerCluster(Config config) {
		super(config);

		// Define commands
		String runCommand[] = { "qsub" };
		String killCommand[] = { "qdel" };
		String statCommand[] = { "qstat" };
		String postMortemInfoCommand[] = { "qstat", "-f" };

		clusterRunCommand = runCommand;
		clusterKillCommand = killCommand;
		clusterStatCommand = statCommand;
		clusterPostMortemInfoCommand = postMortemInfoCommand;

		// Additional command line arguments
		clusterRunAdditionalArgs = config.getStringArray(Config.CLUSTER_RUN_ADDITIONAL_ARGUMENTS);
		clusterKillAdditionalArgs = config.getStringArray(Config.CLUSTER_KILL_ADDITIONAL_ARGUMENTS);
		clusterStatAdditionalArgs = config.getStringArray(Config.CLUSTER_STAT_ADDITIONAL_ARGUMENTS);
		clusterPostMortemAdditionalArgs = config.getStringArray(Config.CLUSTER_POSTMORTEMINFO_ADDITIONAL_ARGUMENTS);

		postMortemDisabled = config.getBool(Config.CLUSTER_POSTMORTEMINFO_DISABLED, false);

		memParam = "mem=";
		cpuParam = "nodes=1:ppn=";
		wallTimeParam = "walltime=";

		// PID regex matcher
		pidRegexStr = config.getPidRegex("");
		if (!pidRegexStr.isEmpty()) {
			if (debug) log("Using pidRegex '" + pidRegexStr + "'");
			pidRegex = Pattern.compile(pidRegexStr);
		}

		// Cluster task need monitoring
		monitorTask = config.getMonitorTask();

		// Create a cluster having only one host with 'inifinite' capacity
		cluster = new Cluster();
		new HostInifinte(cluster);
	}

	/**
	 * Join arguments
	 */
	protected String[] additionalCommandLineArgs(String[] argsOri, String[] argsAdditional) {
		ArrayList<String> args = new ArrayList<String>();

		// Cluster kill commands
		for (int i = 0; i < argsOri.length; i++) {
			args.add(argsOri[i]);

			// Add additional command line arguments right after the command
			if (i == 0) {
				for (String arg : argsAdditional)
					args.add(arg);
			}
		}

		return args.toArray(new String[0]);
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
	 * Create bds-exec commnad
	 */
	protected String bdsCommand(Task task) {
		StringBuilder bdsCmd = new StringBuilder();

		// Calculate timeout
		HostResources res = task.getResources();
		int realTimeout = (int) res.getTimeout();

		// Create command
		bdsCmd.append(bdsCommand);
		bdsCmd.append(realTimeout + " ");
		bdsCmd.append("'" + task.getStdoutFile() + "' ");
		bdsCmd.append("'" + task.getStderrFile() + "' ");
		bdsCmd.append("'" + task.getExitCodeFile() + "' ");
		bdsCmd.append("'" + task.getProgramFileName() + "' ");

		return bdsCmd.toString();
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
	 */
	String clusterStdFile(String fileName) {
		return fileName + ".cluster";
	}

	@Override
	protected Cmd createRunCmd(Task task) {
		task.createProgramFile(); // We must create a program file

		if (debug) log("Running task " + task.getId());

		//---
		// Create command line to dispatch 'task' to the cluster management system
		//---
		ArrayList<String> args = new ArrayList<String>();

		// Append command line arguments
		for (String arg : getCommandRun())
			args.add(arg);

		// Add resources to command line parameters
		addResources(task, args);

		// Tell cluster to redirect Stdout to a file
		if (clusterRunCommandStdOutOption != null) {
			args.add(clusterRunCommandStdOutOption);
			args.add(clusterStdFile(task.getStdoutFile()));
		}

		// Tell cluster to redirect Stderr to a file
		if (clusterRunCommandStdErrOption != null) {
			args.add(clusterRunCommandStdErrOption);
			args.add(clusterStdFile(task.getStderrFile()));
		}

		//---
		// Cluster command is feed some parameters via STDIN. This is
		// similar to running "echo ... | qsub" on a shell.
		// This part creates those 'stdin' parameters
		//---
		String cmdStdin = bdsCommand(task);
		if (debug) {
			// Show command string
			StringBuilder cmdStr = new StringBuilder();
			for (String arg : args)
				cmdStr.append(arg + " ");

			log("Running task " + task.getId() + ", command:\n\techo \"" + cmdStdin + "\" | " + cmdStr);
		}

		//---
		// Create full command
		//---
		CmdCluster cmd = new CmdCluster(task.getId(), args.toArray(Cmd.ARGS_ARRAY_TYPE));
		cmd.setStdin(cmdStdin);
		cmd.setReadPid(true); // We execute using "bds exec" which prints PID number before executing the sub-process
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
		if (checkTasksRunning == null) {
			checkTasksRunning = new CheckTasksRunningCluster(config, this, getCommandStat());
			checkTasksRunning.setDebug(config.isDebug());
			checkTasksRunning.setVerbose(config.isVerbose());
		}
		return checkTasksRunning;
	}

	public String[] getCommandKill() {
		return additionalCommandLineArgs(clusterKillCommand, clusterKillAdditionalArgs);
	}

	public String[] getCommandPostMortemInfo() {
		return additionalCommandLineArgs(clusterPostMortemInfoCommand, clusterPostMortemAdditionalArgs);
	}

	public String[] getCommandRun() {
		return additionalCommandLineArgs(clusterRunCommand, clusterRunAdditionalArgs);
	}

	public String[] getCommandStat() {
		return additionalCommandLineArgs(clusterStatCommand, clusterStatAdditionalArgs);
	}

	/**
	 * An OS command to kill this task
	 */
	@Override
	public String[] osKillCommand(Task task) {
		return getCommandKill();
	}

	/**
	 * Parse PID line from 'qsub'
	 */
	@Override
	public String parsePidLine(String line) {
		line = line.trim();
		if (line.isEmpty()) return "";

		if (pidRegex != null) {
			// Pattern pattern = Pattern.compile("Your job (\\S+)");
			Matcher matcher = pidRegex.matcher(line);
			if (matcher.find()) {

				String pid = null;
				if (matcher.groupCount() > 0) pid = matcher.group(1); // Use first group
				else pid = matcher.group(0); // Use whole pattern

				if (debug) log("Regex '" + pidRegexStr + "' (" + Config.PID_REGEX + ") matched '" + pid + "' in line: '" + line + "'");
				return pid;
			} else if (verbose || debug) log("Regex '" + pidRegexStr + "' (" + Config.PID_REGEX + ") did NOT match line: '" + line + "'");
		} else if (debug) {
			log("No PID regex configured in (missing " + Config.PID_REGEX + " entry in config file?). Using whole line");
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
		// Post-mortem info disabled?
		if (postMortemDisabled) return;

		// Get command line arguments and execute them
		String cmd[] = getCommandPostMortemInfo();
		if (cmd.length <= 0) return;
		if (task.getPid() == null || task.getPid().isEmpty()) return;

		// Prepare command line arguments
		ArrayList<String> args = new ArrayList<String>();
		StringBuilder cmdsb = new StringBuilder();
		for (String arg : cmd) {
			args.add(arg);
			cmdsb.append(" " + arg);
		}
		args.add(task.getPid());
		cmdsb.append(task.getPid());

		// Run command
		ExecResult cmdExecResult = Exec.exec(args, true);
		if (debug) log("Finding postMortemInfo for task " + task.getId() //
				+ "\n\tCommand executed : '" + cmdsb + "'" //
				+ "\n\tExit value       : " + cmdExecResult.exitValue //
				+ "\n\tStdout           : " + cmdExecResult.stdOut //
				+ "\n\tStderr           : " + cmdExecResult.stdErr //
		);

		// Collect the data
		if (cmdExecResult.exitValue == 0) task.setPostMortemInfo(cmdExecResult.stdOut);
		else log("Error trying to find out post-mortem info on task (PID '" + task.getPid() + "')." //
				+ "\n\tCommand executed : '" + cmdsb + "'" //
				+ "\n\tExit code        : " + cmdExecResult.exitValue //
				+ "\n\tStdout           : " + cmdExecResult.stdOut //
				+ "\n\tStderr           : " + cmdExecResult.stdErr //
		);
	}

	@Override
	protected void runTask(Task task, Host host) {
		// Create a (shell) command to run task in cluster
		Cmd cmd = createRunCmd(task);
		if (cmd != null) {
			addCmd(task, cmd);
			cmd.setHost(host);
			cmd.setExecutioner(this);
			cmd.setTask(task);
			cmd.setDebug(debug);
		}

		host.add(task);

		// Run command
		// Note: We run in blocking mode to avoid choking the head node with 
		// too many threads, too many file descriptors, etc..
		if (cmd != null) {
			try {
				cmd.start();
				cmd.join(); // Wait for this thread to finish
			} catch (InterruptedException e) {
				throw new RuntimeException("Error while waiting for command execution:\n\tCommand: " + cmd, e);
			}
		}

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
