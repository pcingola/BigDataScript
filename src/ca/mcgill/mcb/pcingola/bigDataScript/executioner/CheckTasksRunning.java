package ca.mcgill.mcb.pcingola.bigDataScript.executioner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.Exec;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.ExecResult;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.task.TaskState;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
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

	public static boolean debug = false;

	public static final int CHECK_TASK_RUNNING_INTERVAL = 60;
	public static final int TASK_STATE_MIN_START_TIME = 30; // We assume that in less then this number of seconds we might not have a task reported by the cluster system

	protected Timer time; // Timer for checking that tasks are still running
	protected String[] defaultCmdArgs;
	protected Executioner executioner;
	protected ExecResult cmdExecResult;
	protected int cmdPidColumn; // Column in which command outputs PID

	public CheckTasksRunning(Executioner executioner) {
		this.executioner = executioner;
		defaultCmdArgs = new String[0];
		cmdPidColumn = 0;
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
		if (!isCheckTasksRunningTime()) return; // Check every now and then

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
				if (debug) Gpr.debug("Found task PID '" + pid + "'");
				tasks.add(t);
			}
		}

		return tasks;
	}

	/**
	 * Should we query task states? (i.e. run a command to see if tasks are still alive)
	 */
	protected boolean isCheckTasksRunningTime() {
		if (time == null) time = new Timer();
		if (time.elapsedSecs() > CHECK_TASK_RUNNING_INTERVAL) {
			time.start(); // Restart timer
			return true;
		}
		return false;
	}

	/**
	 * Parse command output, extract all PIDs
	 * For each PID, find the corresponding task, add task 'taskFoundId' (HashSet<String>)
	 */
	protected Set<Task> parseCommandOutput() {
		// For each line in stdout...
		HashSet<String> pids = new HashSet<String>();

		for (String line : cmdExecResult.stdOut.split("\n")) {
			// Parse fields
			String fields[] = line.trim().split("\\s+");

			// Obtain PID (found in column number 'cmdPidColumn')
			if (fields.length > cmdPidColumn) {
				String pid = fields[cmdPidColumn];
				pids.add(pid);

				String pidPart = pid.split("\\.")[0]; // Use only the first part before '.'
				pids.add(pidPart);
			}
		}

		// Find a tasks matching these PIDs
		return findRunningTaskByPid(pids);
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
		if (debug) Gpr.debug("Check task running command: exit value " + cmdExecResult.exitValue + ", stdout len: " + cmdExecResult.stdOut.length());

		// Any problems? Report
		if (cmdExecResult.exitValue > 0) {
			Timer.showStdErr("WARNING: There was an error executing cluster stat command: '" + cmdsb.toString().trim() + "'");
			return false;
		}

		return true;
	}

	/**
	 * Update tasks according to 'qstat' command
	 * @param taskFoundId
	 */
	protected synchronized void tasksRunning(Set<Task> taskFoundId) {
		LinkedList<Task> finished = null;

		// Any 'running' task that was not found should be marked as finished/ERROR
		for (Task task : executioner.getTasksRunning().values()) {

			if (!taskFoundId.contains(task) // Task not found by command?
					&& (task.elapsedSecs() > TASK_STATE_MIN_START_TIME) // Make sure that it's been running for a while (otherwise it might that the task has just started and the cluster is not reporting it yet)
					&& !task.isDone() // Is the task "not finished"?
					) {
				if (finished == null) finished = new LinkedList<Task>();
				finished.add(task);
			}
		}

		// Any task to mark as finished/ERROR?
		if (finished != null) {
			for (Task task : finished) {
				String tpid = task.getPid() != null ? task.getPid() : "";

				if (!tpid.isEmpty() && !task.isDone()) { // Make sure the task is not finished (race conditions?)
					if (debug) Gpr.debug("Task PID '" + task.getPid() + "' not found. Marking it as finished.");
					task.setErrorMsg("Task dissapeared from cluster's queue. Task or node failure?");
					task.setExitValue(Task.EXITCODE_ERROR);
					executioner.taskFinished(task, TaskState.ERROR);
				}
			}
		}
	}

}
