package ca.mcgill.mcb.pcingola.bigDataScript.executioner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.mcgill.mcb.pcingola.bigDataScript.Config;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.Exec;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.ExecResult;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.task.TaskState;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * Check that tasks are still running.
 * This method should query the operating system, cluster system or
 * whatever 'low level' system to make sure the tasks are still
 * running.
 * The idea is that if a task is killed, removed or somehow is no
 * longer running, we should catch it here (this is the last resort).
 *
 * @author pcingola
 */
public class CheckTasksRunning {

	public static final int CHECK_TASK_RUNNING_INTERVAL = 60;
	public static final int TASK_STATE_MIN_START_TIME = 30; // We assume that in less then this number of seconds we might not have a task reported by the cluster system
	public static final int TASK_NOT_FOUND_DISAPPEARED = 3; // How many times do we have to 'not find' a task to consider it gone

	protected boolean debug;
	protected boolean verbose;
	protected Timer time; // Timer for checking that tasks are still running
	protected String[] defaultCmdArgs;
	protected Executioner executioner;
	protected ExecResult cmdExecResult;
	protected int cmdPidColumn; // Column in which command outputs PID
	protected Map<String, Integer> missingCount; // How many times was a task missing?
	protected String pidPatternStr;
	protected Pattern pidPattern;

	public CheckTasksRunning(Config config, Executioner executioner) {
		this.executioner = executioner;
		defaultCmdArgs = new String[0];
		missingCount = new HashMap<String, Integer>();

		// Set debug
		debug = config.isDebug();

		// PID regex matcher
		pidPatternStr = config.getPidRegexCheckTasksRunning("");
		if (!pidPatternStr.isEmpty()) {
			if (debug) log("Using pidRegex (check tasks running) '" + pidPatternStr + "'");
			pidPattern = Pattern.compile(pidPatternStr);
		} else if (debug) log("Config parameter '" + Config.PID_CHECK_TASK_RUNNING_REGEX + "' not set.");

		// Select column where to look for PID
		cmdPidColumn = (int) config.getLong(Config.PID_CHECK_TASK_RUNNING_COLUMN, 1) - 1;
		if (cmdPidColumn < 0) cmdPidColumn = 0;
		if (debug) log("Using 'cmdPidColumn' " + cmdPidColumn);
	}

	/**
	 * Check that tasks are actually running
	 *
	 * Default behavior:
	 * 		i) Run a command to query running PIDs
	 * 		ii) Parse command output, extract all PIDs
	 * 		iii) For each PID, find the corresponding task, add task 'taskFoundId' (HashSet<String>)
	 * 		iv) If any task was not found, mark it as an finished/error
	 */
	public void check() {
		if (!shouldCheck()) return; // Check every now and then

		// Run a command to query running PIDs
		if (!runCommand()) return;

		// Parse command output, extract all PIDs
		Set<Task> taskFoundId = parseCommandOutput();

		// If any 'running' tasks was not not found, mark is as finished ('ERROR')
		tasksRunning(taskFoundId);
	}

	/**
	 * Find a running task given a PID
	 */
	protected Set<Task> findRunningTaskByPid(Set<String> pids) {
		HashSet<Task> tasks = new HashSet<Task>();

		// Find task by PID
		for (Task t : executioner.getTasksRunning().values()) {
			String pid = t.getPid();
			if (pid != null && pids.contains(pid)) {
				if (debug) log("Found task PID '" + pid + "'");
				tasks.add(t);
			}
		}

		return tasks;
	}

	/**
	 * Increment counter that keeps track on how many times in a row a task was missing
	 * @return true if task should be considered 'missing'
	 */
	protected boolean incMissingCount(Task task) {
		String id = task.getId();
		int count = (missingCount.containsKey(id) ? missingCount.get(id) + 1 : 1);
		missingCount.put(id, count);

		if (debug) Timer.showStdErr("WARNING: Task PID '" + task.getPid() + "' not found for task '" + id + "'. Incrementing 'missing counter': " + count + " (max. allowed " + TASK_NOT_FOUND_DISAPPEARED + ")");
		return count > TASK_NOT_FOUND_DISAPPEARED;
	}

	void log(String msg) {
		executioner.log(this.getClass().getSimpleName() + ":" + msg);
	}

	/**
	 * Parse command output, extract all PIDs
	 */
	protected Set<Task> parseCommandOutput() {
		// For each line in stdout...
		String lines[] = cmdExecResult.stdOut.split("\n");

		// Parse PIDs
		Set<String> pids = parseCommandOutput(lines);

		// Find a tasks matching these PIDs
		return findRunningTaskByPid(pids);
	}

	/**
	 * Parse command output, extract all PIDs
	 * For each PID, find the corresponding task, add task 'taskFoundId' (HashSet<String>)
	 */
	public Set<String> parseCommandOutput(String lines[]) {
		HashSet<String> pids = new HashSet<String>();

		// Parse lines
		for (String line : lines) {
			line = line.trim();

			if (debug) log("Parsing line:\t" + line);
			String pid = parsePidLine(line);

			// Any results?
			if (pid != null && !pid.isEmpty()) {
				// PID parsed OK
				if (pids.add(pid)) {
					if (debug) log("\tAdding ID: '" + pid + "'");
				}
			} else {
				// PID not matched by 'pidRegexCheckTaskRunning' Regex? (or regex not set) => Try other methods

				// Split fields
				String fields[] = line.split("\\s+");

				// Obtain PID (found in column number 'cmdPidColumn')
				if ((0 <= cmdPidColumn) && (cmdPidColumn < fields.length)) {
					pid = fields[cmdPidColumn];

					// Add first column (whole pid)
					if (pids.add(pid)) {
						if (debug) log("\tAdding ID (column number " + cmdPidColumn + "): '" + pid + "'");
					}

					// Use only first part (split using dot)
					String pidPart = pid.split("\\.")[0]; // Use only the first part before '.'
					if (pids.add(pidPart)) {
						if (debug) log("\tAdding ID (using string before fisrt dot): '" + pid + "'");
					}
				}
			}
		}

		return pids;
	}

	/**
	 * Parse PID line from 'qstat' (Cmd)
	 */
	public String parsePidLine(String line) {
		if (pidPattern == null || line.isEmpty()) return "";

		// Pattern pattern = Pattern.compile("Your job (\\S+)");
		Matcher matcher = pidPattern.matcher(line);
		if (matcher.find()) {
			String pid = null;
			if (matcher.groupCount() > 0) pid = matcher.group(1); // Use first group
			else pid = matcher.group(0); // Use whole pattern

			if (debug) log("Regex '" + pidPatternStr + "' (" + Config.PID_CHECK_TASK_RUNNING_REGEX + ") matched '" + pid + "' in line: '" + line + "'");
			return pid;
		} else if (debug) log("Regex '" + pidPatternStr + "' (" + Config.PID_CHECK_TASK_RUNNING_REGEX + ") did NOT match line: '" + line + "'");

		return line;
	}

	/**
	 * Reset counter (task was found)
	 */
	protected void resetMissingCount(Task task) {
		missingCount.remove(task.getId());
	}

	/**
	 * Run a command to find running processes PIDs
	 * @return true if OK, false on failure
	 */
	protected boolean runCommand() {
		// Prepare command line arguments
		ArrayList<String> args = new ArrayList<String>();
		StringBuilder cmdsb = new StringBuilder();
		for (String arg : defaultCmdArgs) {
			args.add(arg);
			cmdsb.append(" " + arg);
		}

		// Execute command
		cmdExecResult = Exec.exec(args, true);
		if (debug) Timer.showStdErr("Check task running:" //
				+ "\n\tCommand    : '" + cmdsb.toString().trim() + "'" //
				+ "\n\tExit value : " + cmdExecResult.exitValue //
				+ "\n\tStdout     : " + cmdExecResult.stdOut //
				+ "\n\tStderr     : " + cmdExecResult.stdErr //
				);

		//---
		// Sanity checks!
		//---

		// Failed command?
		if (cmdExecResult.exitValue > 0) {
			Timer.showStdErr("WARNING: There was an error executing cluster stat command: '" + cmdsb.toString().trim() + "'.\nExit code: " + cmdExecResult.exitValue);
			return false;
		}

		// Any problems reported on STDERR?
		if (!cmdExecResult.stdErr.isEmpty()) {
			Timer.showStdErr("WARNING: There was an error executing cluster stat command: '" + cmdsb.toString().trim() + "'\nSTDERR:\n" + cmdExecResult.stdErr);
			return false;
		}

		// Empty STDOUT?
		// Note: This might not be a problem, but a cluster (or any computer) running
		// zero processes is really weird. Furthermore, this commands usually show some
		// kind of header, so STDOUT is never empty
		if (verbose && cmdExecResult.stdOut.isEmpty()) {
			Timer.showStdErr("WARNING: Empty STDOUT when executing cluster stat command: '" + cmdsb.toString().trim());
			return false;
		}

		// OK
		return true;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	/**
	 * Should we query task states? (i.e. run a command to see if tasks are still alive)
	 */
	protected boolean shouldCheck() {
		if (time == null) time = new Timer();
		if (time.elapsedSecs() > CHECK_TASK_RUNNING_INTERVAL) {
			time.start(); // Restart timer
			return true;
		}
		return false;
	}

	/**
	 * Update tasks according to cluster status
	 */
	protected synchronized void tasksRunning(Set<Task> taskFoundId) {
		LinkedList<Task> finished = null;

		// Any 'running' task that was not found should be marked as finished/ERROR
		for (Task task : executioner.getTasksRunning().values()) {

			if (!taskFoundId.contains(task) // Task not found by command?
					&& (task.elapsedSecs() > TASK_STATE_MIN_START_TIME) // Make sure that it's been running for a while (otherwise it might that the task has just started and the cluster is not reporting it yet)
					&& !task.isDone() // Is the task "not finished"?
					) {
				// Task is missing.
				// Update counter: Should we consider this task as 'missing'?
				if (incMissingCount(task)) {
					if (finished == null) finished = new LinkedList<Task>();
					finished.add(task);
				}
			} else {
				// Task was found, reset 'missing' counter (if any)
				resetMissingCount(task);
			}
		}

		//---
		// Any task to mark as finished/ERROR?
		//---
		if (finished != null) {
			for (Task task : finished) {
				String tpid = task.getPid() != null ? task.getPid() : "";

				if (!tpid.isEmpty() && !task.isDone()) { // Make sure the task is not finished (race conditions?)
					if (debug) log("Task PID '" + task.getPid() + "' not found. Marking it as finished.");
					task.setErrorMsg("Task dissapeared from cluster's queue. Task or node failure?");
					task.setExitValue(Task.EXITCODE_ERROR);
					executioner.taskFinished(task, TaskState.ERROR);
				}
			}
		}
	}

}
