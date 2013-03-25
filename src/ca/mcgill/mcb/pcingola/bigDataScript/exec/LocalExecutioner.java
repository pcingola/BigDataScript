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

	public LocalExecutioner() {
		super();
		cmdById = new HashMap<String, CmdRunner>();
	}

	@Override
	public void add(Task task) {
		super.add(task);
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

		ArrayList<String> args = new ArrayList<String>();
		for (String arg : CmdRunner.LOCAL_EXEC_COMMAND)
			args.add(arg);
		args.add(task.getProgramFileName());

		String cmdStr = "";
		for (String arg : args)
			cmdStr += arg + " ";

		// Run command
		if (debug) Timer.showStdErr("Running command: " + cmdStr);
		CmdRunner cmd = new CmdRunner(task.getId(), args.toArray(CmdRunner.ARGS_ARRAY_TYPE));
		cmd.setCmdStats(task);
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
		if (tasksToRun.isEmpty()) return false; // Nothing to do

		// Create a new collection to avoid 'concurrent modification error'
		ArrayList<Task> run = new ArrayList<Task>();
		run.addAll(tasksToRun);

		// Run all tasks
		for (Task task : run) {
			// Run each task
			run(task, null);

			// Wait for task to finish
			CmdRunner cmd = cmdById.get(task.getId());
			try {
				cmd.join();
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
	protected boolean runTask(Task task, Host host) {
		// Create and start a thread
		CmdRunner cmd = createCmdRunner(task);
		cmdById.put(task.getId(), cmd);
		cmd.start();
		return true;
	}
}
