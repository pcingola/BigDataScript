package ca.mcgill.mcb.pcingola.bigDataScript.exec;

import java.util.ArrayList;
import java.util.HashMap;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.CmdRunner;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * Execute tasks in local computer. 
 * Executed the command immediately without controlling host resources.
 *  
 * @author pcingola
 */
public class LocalExecutioner extends Executioner {

	HashMap<String, CmdRunner> cmdById;

	public LocalExecutioner(PidLogger pidLogger) {
		super(pidLogger);
		cmdById = new HashMap<String, CmdRunner>();
	}

	@Override
	protected void addTail(Task task) {
		// We need to feed the InputStreams from the process, instead of file names
		CmdRunner cmd = cmdById.get(task.getId());

		// Wait for cmd thread to start, STDOUT and STDERR to became available
		while (!cmd.isStarted() || (cmd.getStdout() == null) || (cmd.getStderr() == null))
			sleepShort();

		// Add to tail
		tail.add(cmd.getStdout(), task.getStdoutFile(), false);
		tail.add(cmd.getStderr(), task.getStderrFile(), true);
	}

	/**
	 * Create a CmdRunner to execute the script
	 * @param task
	 * @param host
	 * @return
	 */
	CmdRunner createCmdRunner(Task task) {
		task.createProgramFile(); // We must create a program file

		// Create command line
		ArrayList<String> args = new ArrayList<String>();
		for (String arg : CmdRunner.LOCAL_EXEC_COMMAND)
			args.add(arg);
		long timeout = task.getResources().getTimeout() > 0 ? task.getResources().getTimeout() : 0;

		// Add command line parameters for "bds exec"
		args.add(timeout + ""); // Enforce timeout
		args.add(task.getStdoutFile()); // Redirect STDOUT to this file
		args.add(task.getStderrFile()); // Redirect STDERR to this file
		args.add("-"); // No need to create exitCode file in local execution
		args.add(task.getProgramFileName()); // Program to execute

		String cmdStr = "";
		for (String arg : args)
			cmdStr += arg + " ";

		// Run command
		if (debug) Timer.showStdErr("Running command: " + cmdStr);
		CmdRunner cmd = new CmdRunner(task.getId(), args.toArray(CmdRunner.ARGS_ARRAY_TYPE));
		cmd.setTask(task);
		cmd.setExecutioner(this);
		cmd.setReadPid(true); // We execute using "bds exec" which prints PID number before executing the sub-process
		cmdById.put(task.getId(), cmd);
		return cmd;
	}

	@Override
	public synchronized boolean finished(String id) {
		cmdById.remove(id);
		return super.finished(id);
	}

	@Override
	protected boolean killTask(Task task) {
		CmdRunner cmd = cmdById.get(task.getId());
		if (cmd == null) return false;
		cmd.kill();
		return true;
	}

	@Override
	protected boolean runLoop() {
		// Nothing to do?
		if (tasksToRun.isEmpty()) return false;

		// Create a new collection to avoid 'concurrent modification error'
		ArrayList<Task> run = new ArrayList<Task>();
		run.addAll(tasksToRun);

		// Run all tasks
		for (Task task : run) {
			// Run each task
			runTask(task, null);

			// Wait for task to finish
			CmdRunner cmd = cmdById.get(task.getId());
			try {
				if (cmd != null) cmd.join(); // cmd can be null, since it can finish before "cmdById.get(...)" thus there is no command in the hash
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		return true;
	}

	/**
	 * Execute on local computer. 
	 * Host parameter is ignored
	 */
	@Override
	protected boolean runTaskCommand(Task task, Host host) {
		// Create and start a thread
		CmdRunner cmd = createCmdRunner(task);
		cmdById.put(task.getId(), cmd);
		cmd.start();
		return true;
	}

	/**
	 * Wait until a task finishes
	 * @param taskId
	 * @return
	 */
	public int wait(Task task) {
		String taskId = task.getId();
		CmdRunner cmd = null;

		// Wait for command to appear
		while (cmd == null) {
			cmd = cmdById.get(taskId);
			sleepShort();
		}

		// Joind the thread (wait until execution ends)
		try {
			cmd.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Now the task is 'done'
		return task.getExitValue();
	}
}
