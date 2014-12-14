package ca.mcgill.mcb.pcingola.bigDataScript.executioner;

import java.util.ArrayList;

import ca.mcgill.mcb.pcingola.bigDataScript.Config;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.Cmd;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.CmdLocal;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * Execute tasks in local computer.
 *
 * Uses a queue to avoid saturating resources (e.g. number of running
 * processes in the queue should not exceed number of CPUs)
 *
 * @author pcingola
 */
public class ExecutionerLocal extends Executioner {

	public static String LOCAL_EXEC_COMMAND[] = { "bds", "exec" };
	public static String LOCAL_KILL_COMMAND[] = { "bds", "kill" };
	public static String LOCAL_STAT_COMMAND[] = { "ps" };

	/**
	 * Create command line arguments for "bds -exec"
	 */
	public static String[] createBdsExecCmdArgs(Task task) {
		// Create command line
		ArrayList<String> args = new ArrayList<String>();
		for (String arg : LOCAL_EXEC_COMMAND)
			args.add(arg);
		long timeout = task.getResources().getTimeout() > 0 ? task.getResources().getTimeout() : 0;

		// Add command line parameters for "bds exec"
		args.add(timeout + ""); // Enforce timeout
		args.add(task.getStdoutFile() != null ? task.getStdoutFile() : "-"); // Redirect STDOUT to this file
		args.add(task.getStderrFile() != null ? task.getStderrFile() : "-"); // Redirect STDERR to this file
		args.add(task.getExitCodeFile() != null ? task.getExitCodeFile() : "-"); // Redirect exit code
		args.add(task.getProgramFileName()); // Program to execute

		return args.toArray(Cmd.ARGS_ARRAY_TYPE);
	}

	protected ExecutionerLocal(Config config) {
		super(config);
		checkTasksRunning = new CheckTasksRunningLocal(this);
	}

	/**
	 * Sometimes a "text file busy" error may appear when we execute a task.
	 * E.g.: The following script will produce "text file busy" error on
	 *       some Linux systems (local execution):
	 *
	 * 		$ cat z.bds
	 * 		#!/usr/bin/env bds
	 * 		for( int i=0 ; i < 10000 ; i++ ) task echo hi $i
	 *
	 * 		$ ./z.bds > /dev/null
	 * 		2014/01/25 16:52:36 fork/exec z.bds.20140125_165235_563/task.line_7.id_198.sh: text file busy
	 *
	 * To avoid this, we must make sure that JVM actually has
	 * closed the file. Surprisingly, invoking flush() and
	 * close() is not enough to make sure the file is actually
	 * fully closed.
	 *
	 * We need something like 'lsof' command in Java, which doesn't
	 * seem to exist.
	 *
	 * So far the only solution that seems to work is to wait a small
	 * amount of time between file creation and execution. I use
	 * 1 millisecond, since it is the minimum for sleep() method.
	 *
	 * There are two obvious problems:
	 * 		i) This obviously penalizes execution performance.
	 * 		ii) There is no warrantees that this will always work.
	 *
	 */
	void avoidTextFileBusyError() {
		// Hack to avoid "Text file busy" errors: Sleep 1 millisecond.
		// This is a horrible hack used to make sure the 'programFileName' has
		// been fully written to disk and we no have the file open for writing.
		// Even if we closed the file, sometimes a "text file busy" error
		// pops up
		// and not execute.
		try {
			sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Create a CmdRunner to execute the script
	 */
	@Override
	protected synchronized Cmd createCmd(Task task) {
		task.createProgramFile(); // We must create a program file

		// Create command line
		String args[] = createBdsExecCmdArgs(task);

		avoidTextFileBusyError();

		// Join args
		String cmdStr = "";
		for (String arg : args)
			cmdStr += arg + " ";

		// Run command
		if (debug) Timer.showStdErr("Running command: " + cmdStr);
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
		// This is killed internally by 'bds' (see GO program)
		// So, there is no need for special commands
		return null;
	}
}
