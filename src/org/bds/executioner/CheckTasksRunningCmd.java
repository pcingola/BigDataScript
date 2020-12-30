package org.bds.executioner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bds.Config;
import org.bds.osCmd.Exec;
import org.bds.osCmd.ExecResult;
import org.bds.task.Task;

/**
 * Check that tasks are still running, by running an OS command
 *
 * This method should query the operating system, cluster system or
 * whatever 'low level' system to make sure the tasks are still
 * running.
 *
 * The idea is that if a task is killed, removed or somehow is no
 * longer running, we should catch it here (this is the last resort).
 *
 * @author pcingola
 */
public class CheckTasksRunningCmd extends CheckTasksRunning {

	protected String[] defaultCmdArgs;
	protected ExecResult cmdExecResult;
	protected int cmdPidColumn; // Column in which command outputs PID
	protected String pidPatternStr;
	protected Pattern pidPattern;

	public CheckTasksRunningCmd(Config config, Executioner executioner) {
		super(config, executioner);
		defaultCmdArgs = new String[0];

		// PID regex matcher
		pidPatternStr = config.getPidRegexCheckTasksRunning("");
		if (!pidPatternStr.isEmpty()) {
			debug("Using pidRegex (check tasks running) '" + pidPatternStr + "'");
			pidPattern = Pattern.compile(pidPatternStr);
		} else debug("Config parameter '" + Config.PID_CHECK_TASK_RUNNING_REGEX + "' not set.");

		// Select column where to look for PID
		cmdPidColumn = (int) config.getLong(Config.PID_CHECK_TASK_RUNNING_COLUMN, 1) - 1;
		if (cmdPidColumn < 0) cmdPidColumn = 0;
		debug("Using 'cmdPidColumn' " + cmdPidColumn);
	}

	/**
	 * Run tasks that are currently running
	 * @return
	 */
	@Override
	protected Set<Task> findRunningTasks() {
		// Run a command to query running PIDs
		if (!runCommand()) return null;

		// Parse command output, extract all PIDs
		return parseCommandOutput();
	}

	@Override
	protected boolean matchesPids(Set<String> pids, String pid) {
		return pids.contains(pid) || pids.contains(parsePidPart(pid));
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
		HashSet<String> pids = new HashSet<>();

		// Parse lines
		for (String line : lines) {
			line = line.trim();

			debug("Parsing line:\t" + line);
			String pid = parsePidLine(line);

			// Any results?
			if (pid != null && !pid.isEmpty()) {
				// PID parsed OK
				if (pids.add(pid)) {
					debug("\tAdding ID: '" + pid + "'");
				}
			} else {
				// PID not matched by 'pidRegexCheckTaskRunning' regex (or regex not set)?
				// => Try other methods

				// Split fields
				String fields[] = line.split("\\s+");

				// Obtain PID (found in column number 'cmdPidColumn')
				if ((0 <= cmdPidColumn) && (cmdPidColumn < fields.length)) {
					pid = fields[cmdPidColumn];

					// Add first column (whole pid)
					if (pids.add(pid)) {
						debug("\tAdding ID (column number " + cmdPidColumn + "): '" + pid + "'");
					}

					// Use only first part (split using dot)
					String pidPart = parsePidPart(pid);
					if (pids.add(pidPart)) {
						debug("\tAdding ID (using string before fisrt dot): '" + pidPart + "'");
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

			debug("Regex '" + pidPatternStr + "' (" + Config.PID_CHECK_TASK_RUNNING_REGEX + ") matched '" + pid + "' in line: '" + line + "'");
			return pid;
		} else debug("Regex '" + pidPatternStr + "' (" + Config.PID_CHECK_TASK_RUNNING_REGEX + ") did NOT match line: '" + line + "'");

		return line;
	}

	/**
	 *  Use only the first part before '.' as PID
	 */
	protected String parsePidPart(String pid) {
		return pid.split("\\.")[0];
	}

	/**
	 * Run a command to find running processes PIDs
	 * @return true if OK, false on failure
	 */
	protected boolean runCommand() {
		// Prepare command line arguments
		ArrayList<String> args = new ArrayList<>();
		StringBuilder cmdsb = new StringBuilder();
		for (String arg : defaultCmdArgs) {
			args.add(arg);
			cmdsb.append(" " + arg);
		}

		// Execute command
		cmdExecResult = Exec.exec(args, true);
		debug("Check task running:" //
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
			warning("There was an error executing cluster stat command: '" + cmdsb.toString().trim() + "'.\nExit code: " + cmdExecResult.exitValue);
			return false;
		}

		// Any problems reported on STDERR?
		if (!cmdExecResult.stdErr.isEmpty()) {
			warning("There was an error executing cluster stat command: '" + cmdsb.toString().trim() + "'\nSTDERR:\n" + cmdExecResult.stdErr);
			return false;
		}

		// Empty STDOUT?
		// Note: This might not be a problem, but a cluster (or any computer) running
		// zero processes is really weird. Furthermore, this commands usually show some
		// kind of header, so STDOUT is never empty
		if (verbose && cmdExecResult.stdOut.isEmpty()) {
			warning("Empty STDOUT when executing cluster stat command: '" + cmdsb.toString().trim());
			return false;
		}

		// OK
		return true;
	}

}
