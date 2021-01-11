package org.bds.executioner;

import java.util.List;
import java.util.regex.Pattern;

import org.bds.Config;
import org.bds.cluster.host.TaskResourcesCluster;
import org.bds.task.Task;
import org.bds.util.Timer;

/**
 * Execute tasks in an SLURM cluster.
 *
 * @author pcingola
 */
public class ExecutionerClusterSlurm extends ExecutionerCluster {

	// Define commands
	public static final String KILL_COMMAND[] = { "scancel" };
	public static final String POST_MORTEM_COMMAND[] = { "scontrol", "-d", "show", "job" };
	public static final String STAT_COMMAND[] = { "squeue", "-h", "-a", "-o", "%A" };
	public static final String RUN_COMMAND[] = { "sbatch", "--parsable", "--no-requeue" };
	public static final String PID_REGEX_DEFAULT = "(\\d+)";

	boolean timeInMins = true; // Typically SLURM timeouts are in minutes

	public ExecutionerClusterSlurm(Config config) {
		super(config);

		clusterRunCommand = RUN_COMMAND;
		clusterKillCommand = KILL_COMMAND;
		clusterStatCommand = STAT_COMMAND;
		clusterPostMortemInfoCommand = POST_MORTEM_COMMAND;
		clusterRunCommandStdOutOption = "--output";
		clusterRunCommandStdErrOption = "--error";

		cpuParam = "--cpus-per-task";
		memParam = "--mem";
		wallTimeParam = "-t";

		useShellScript = true;

		// When running sbatch you get a line lie this:
		//
		//		$ sbatch x.sh
		// 		Submitted batch job 171984
		//
		//		$ sbatch --parsable x.sh
		// 		171984
		//
		// So, this is a pattern matcher to parse the PID
		pidRegexStr = config.getPidRegex(PID_REGEX_DEFAULT);
		pidRegex = Pattern.compile(pidRegexStr);
		debug("Using pidRegex '" + pidRegexStr + "'");
	}

	/**
	 * Add resource options to command line parameters
	 */
	@Override
	protected void addResources(Task task, List<String> args) {
		// Add resources request
		TaskResourcesCluster res = (TaskResourcesCluster) task.getResources();

		// Cpu
		if (res.getCpus() > 0) {
			args.add(cpuParam);
			args.add("" + res.getCpus());
		}

		// Memory
		if (res.getMem() > 0) {
			long memInM = Math.max(res.getMem() / (1024 * 1024), 1);
			args.add(memParam + "=" + memInM + "M");
		}

		// Timeout
		int clusterTimeout = calcTimeOut(res);
		if (clusterTimeout > 0) { // Hard timeout
			args.add(wallTimeParam);
			args.add(time(clusterTimeout));
		}

		// A particular queue was requested?
		String queue = res.getQueue();
		if (queue != null && !queue.isEmpty()) {
			args.add("-p");
			args.add(queue);
		}

	}

	/**
	 * Add shell script to command line parameters
	 * Note: Some clusters require the command to be executed to be in a shell script, while others accept STDIN
	 */
	@Override
	protected void addShellScript(Task task, List<String> args) {
		String shellScripFile = createShellScriptBdsCommand(task);
		args.add(shellScripFile);
	}

	/**
	 * Represent a time according for 'sbatch' command line arguments
	 */
	protected String time(int secs) {
		if (timeInMins) return secs / 60 + "";
		return Timer.toHHMMSS(secs * 1000L);
	}
}
