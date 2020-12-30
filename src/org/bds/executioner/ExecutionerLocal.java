package org.bds.executioner;

import org.bds.Config;
import org.bds.osCmd.Cmd;
import org.bds.osCmd.CmdLocal;
import org.bds.task.Task;
import org.bds.util.Gpr;

/**
 * Execute tasks in local computer.
 *
 * Uses a queue to avoid saturating resources (e.g. number of running
 * processes in the queue should not exceed number of CPUs)
 *
 * @author pcingola
 */
public class ExecutionerLocal extends ExecutionerFileSystem {

	public static String LOCAL_KILL_COMMAND[] = { "bds", "kill" };
	public static String LOCAL_STAT_COMMAND[] = { "ps" };

	private static final String[] TASK_LOGGER_INTERNAL_KILL = { TaskLogger.CMD_KILL };

	protected ExecutionerLocal(Config config) {
		super(config);
		checkTasksRunning = new CheckTasksRunningLocal(config, this);
		checkTasksRunning.setDebug(config.isDebug());
		checkTasksRunning.setVerbose(config.isVerbose());
	}

	/**
	 * Create a CmdRunner to execute the script
	 */
	@Override
	public synchronized Cmd createRunCmd(Task task) {
		task.createProgramFile(); // We must create a program file

		// Create command line
		String args[] = createBdsExecCmdArgs(task);

		avoidTextFileBusyError();

		// Run command
		if (debug) {
			// Join args
			String cmdStr = "";
			for (String arg : args)
				cmdStr += arg + " ";
			debug("Running command: " + cmdStr);
		}

		CmdLocal cmd = new CmdLocal(task.getId(), args);
		cmd.setDebug(debug);
		cmd.setReadPid(true); // We execute using "bds exec" which prints PID number before executing the sub-process

		return cmd;
	}

	/**
	 * Follow a task's STDOUT and STDERR
	 */
	@Override
	protected synchronized void follow(Task task) {
		if (taskLogger != null) taskLogger.add(task, this); // Log PID (if any)

		// We need to feed the InputStreams from the process, instead of file names
		CmdLocal cmd = (CmdLocal) getCmd(task);
		if (cmd == null) {
			Gpr.debug("Cannot find command (null command) for task '" + task.getId() + "'. This should never happen!\nTask:\n" + task.getProgramTxt());
			return;
		}

		// Wait for cmd thread to start, STDOUT and STDERR to became available
		while (!cmd.isStarted() || (cmd.getStdout() == null) || (cmd.getStderr() == null)) {
			sleepShort();
		}

		// Add to tail
		tail.add(cmd.getStdout(), task.getStdoutFile(), false);
		tail.add(cmd.getStderr(), task.getStderrFile(), true);

		if (monitorTask != null) monitorTask.add(this, task); // Start monitoring exit file
	}

	@Override
	public String[] osKillCommand(Task task) {
		// This is killed internally by 'bds' (see GO program) so the kill command is '@kill' (internal commands start with '@')
		return TASK_LOGGER_INTERNAL_KILL;
	}
}
