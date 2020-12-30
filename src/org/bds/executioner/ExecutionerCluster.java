package org.bds.executioner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bds.Config;
import org.bds.cluster.ComputerSystem;
import org.bds.cluster.host.Host;
import org.bds.cluster.host.HostInifinte;
import org.bds.cluster.host.Resources;
import org.bds.cluster.host.TaskResourcesCluster;
import org.bds.osCmd.Cmd;
import org.bds.osCmd.CmdCluster;
import org.bds.osCmd.Exec;
import org.bds.osCmd.ExecResult;
import org.bds.task.Task;
import org.bds.util.Gpr;
import org.bds.util.Timer;

/**
 * Execute tasks in a MOAB cluster.
 *
 * All commands are run using 'qsub' (or equivalent) commands
 *
 * @author pcingola
 */
public class ExecutionerCluster extends ExecutionerFileSystem {

	protected final String CLUSTER_DEFAULT_RUN_COMMAND_STDOUT_OPTION = "-o";
	protected final String CLUSTER_DEFAULT_RUN_COMMAND_STDERR_OPTION = "-e";

	protected String clusterRunCommand[];
	protected String clusterKillCommand[];
	protected String clusterStatCommand[];
	protected String clusterPostMortemInfoCommand[];

	protected String clusterRunCommandStdOutOption;
	protected String clusterRunCommandStdErrOption;

	protected String clusterRunAdditionalArgs[];
	protected String clusterKillAdditionalArgs[];
	protected String clusterStatAdditionalArgs[];
	protected String clusterPostMortemAdditionalArgs[];

	protected String memParam;
	protected String cpuParam;
	protected String wallTimeParam;

	protected boolean postMortemDisabled; // Disable post-mortem taks info?
	protected boolean useShellScript; // Use shell script or STDIN for feeding the commands?

	public int MIN_EXTRA_TIMEOUT = 60;
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

		blockRunTasks = true;

		clusterRunCommand = runCommand;
		clusterKillCommand = killCommand;
		clusterStatCommand = statCommand;
		clusterPostMortemInfoCommand = postMortemInfoCommand;

		clusterRunCommandStdOutOption = CLUSTER_DEFAULT_RUN_COMMAND_STDOUT_OPTION;
		clusterRunCommandStdErrOption = CLUSTER_DEFAULT_RUN_COMMAND_STDERR_OPTION;

		// Additional command line arguments
		clusterRunAdditionalArgs = config.getStringArray(Config.CLUSTER_RUN_ADDITIONAL_ARGUMENTS);
		clusterKillAdditionalArgs = config.getStringArray(Config.CLUSTER_KILL_ADDITIONAL_ARGUMENTS);
		clusterStatAdditionalArgs = config.getStringArray(Config.CLUSTER_STAT_ADDITIONAL_ARGUMENTS);
		clusterPostMortemAdditionalArgs = config.getStringArray(Config.CLUSTER_POSTMORTEMINFO_ADDITIONAL_ARGUMENTS);

		postMortemDisabled = config.getBool(Config.CLUSTER_POSTMORTEMINFO_DISABLED, false);

		memParam = "mem=";
		cpuParam = "nodes=1:ppn=";
		wallTimeParam = "walltime=";

		useShellScript = false;

		// PID regex matcher
		pidRegexStr = config.getPidRegex("");
		if (!pidRegexStr.isEmpty()) {
			debug("Using pidRegex '" + pidRegexStr + "'");
			pidRegex = Pattern.compile(pidRegexStr);
		}

		// Cluster task need monitoring
		monitorTask = MonitorTasks.get().getMonitorTaskExitFile();

		// Create a cluster having only one host with 'infinite' capacity
		system = new ComputerSystem();
		new HostInifinte(system);
	}

	/**
	 * Join arguments
	 */
	protected String[] additionalCommandLineArgs(String[] argsOri, String[] argsAdditional) {
		ArrayList<String> args = new ArrayList<>();

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
		TaskResourcesCluster res = (TaskResourcesCluster) task.getResources();

		long clusterTimeout = calcTimeOut(res);
		String clusterTimeoutStr = timeStr(clusterTimeout);

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
		String queue = res.getQueue();
		if (queue != null && !queue.isEmpty()) {
			args.add("-q");
			args.add(queue);
		}
	}

	/**
	 * Add shell script to command line parameters
	 * Note: Some clusters require the command to be executed to be in a shell script, while others accept STDIN
	 */
	protected void addShellScript(Task task, List<String> args) {
	}

	/**
	 * Calculate timeout parameter. We want to assign slightly larger timeout
	 * to the cluster (qsub/msub), because we prefer bds to kill the process (it's
	 * cleaner and we get exitCode file)
	 */
	protected int calcTimeOut(Resources res) {
		int realTimeout = (int) res.getTimeout();
		if (realTimeout <= 0) return 0;

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
	public Cmd createRunCmd(Task task) {
		task.createProgramFile(); // We must create a program file

		debug("Running task " + task.getId());

		//---
		// Create command line to dispatch 'task' to the cluster management system
		//---
		ArrayList<String> args = new ArrayList<>();

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

		// Add commands either by shell script or STDIN
		String cmdStdin = null;
		if (useShellScript) {
			// Add shell script
			addShellScript(task, args);
		} else {
			//---
			// Cluster command is feed some parameters via STDIN. This is
			// similar to running "echo ... | qsub" on a shell.
			// This part creates those 'stdin' parameters
			//---
			cmdStdin = createBdsExecCmdStr(task);
			if (debug) {
				// Show command string
				StringBuilder cmdStr = new StringBuilder();
				for (String arg : args)
					cmdStr.append(arg + " ");

				log("Running task " + task.getId() + ", command:\n\techo \"" + cmdStdin + "\" | " + cmdStr);
			}
		}

		//---
		// Create full command
		//---
		CmdCluster cmd = new CmdCluster(task.getId(), args.toArray(Cmd.ARGS_ARRAY_TYPE));
		if (!useShellScript) cmd.setStdin(cmdStdin);
		cmd.setReadPid(true); // We execute using a cluster submit command that which prints PID
		return cmd;
	}

	/**
	 * You cannot pass a command to SLURM, only a shell script.
	 * We create shell script containing the bds command to execute.
	 * @param task
	 * @return Shell script name
	 */
	protected String createShellScriptBdsCommand(Task task) {
		// Get shell script
		StringBuilder sb = new StringBuilder();
		sb.append("#!" + Config.get().getTaskShell() + "\n\n");
		sb.append(createBdsExecCmdStr(task));
		sb.append("\n");

		// Save to file
		String fileName = shellFileName(task);
		Gpr.toFile(fileName, sb.toString());

		// Make sure file is executable
		File f = new File(fileName);
		f.setExecutable(true);
		if (!log) f.deleteOnExit();

		return fileName;
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

				debug("Regex '" + pidRegexStr + "' (" + Config.PID_REGEX + ") matched '" + pid + "' in line: '" + line + "'");
				return pid;
			} else log("Regex '" + pidRegexStr + "' (" + Config.PID_REGEX + ") did NOT match line: '" + line + "'");
		} else debug("No PID regex configured in (missing " + Config.PID_REGEX + " entry in config file?). Using whole line");

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
		ArrayList<String> args = new ArrayList<>();
		StringBuilder cmdsb = new StringBuilder();
		for (String arg : cmd) {
			args.add(arg);
			cmdsb.append(" " + arg);
		}
		args.add(task.getPid());
		cmdsb.append(task.getPid());

		// Run command
		ExecResult cmdExecResult = Exec.exec(args, true);
		log("Finding postMortemInfo for task " + task.getId() //
				+ "\n\tCommand executed : '" + cmdsb + "'" //
				+ "\n\tExit value       : " + cmdExecResult.exitValue //
				+ "\n\tStdout           : " + cmdExecResult.stdOut //
				+ "\n\tStderr           : " + cmdExecResult.stdErr //
		);

		// Collect the data
		if (cmdExecResult.exitValue == 0) task.setPostMortemInfo(cmdExecResult.stdOut);
		else log("Error trying to find out post-mortem info on task (PID '" + task.getPid() + "')." //
				+ "\n\tCommand executed : '" + cmdsb + "'" //
				+ "\n\tExit value       : " + cmdExecResult.exitValue //
				+ "\n\tStdout           : " + cmdExecResult.stdOut //
				+ "\n\tStderr           : " + cmdExecResult.stdErr //
		);
	}

	/**
	 * Create a shell file name for a slurm script (basically invoke bds command)
	 * @param task
	 * @return
	 */
	protected String shellFileName(Task task) {
		String programFileName = task.getProgramFileName();
		try {
			File file = new File(programFileName);
			File dir = file.getCanonicalFile().getParentFile();
			String programFileDir = dir.getCanonicalPath();
			String baseName = file.getName();
			int idx = baseName.lastIndexOf('.');
			if (idx > 0) baseName = baseName.substring(0, idx);
			return programFileDir + "/" + baseName + ".slurm.sh";
		} catch (IOException e) {
			// Nothing to do
		}
		return programFileName + ".slurm.sh";
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

	/**
	 * Get timeout in a format that the cluster command expects
	 * @param clusterTimeout : Time out in seconds
	 */
	protected String timeStr(long clusterTimeout) {
		return Timer.toHHMMSS(1000 * clusterTimeout);
	}

}
