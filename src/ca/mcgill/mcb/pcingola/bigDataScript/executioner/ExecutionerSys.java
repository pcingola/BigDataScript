package ca.mcgill.mcb.pcingola.bigDataScript.executioner;

import java.util.ArrayList;

import ca.mcgill.mcb.pcingola.bigDataScript.Config;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.Cmd;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.CmdLocal;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * Execute tasks in local computer. 
 * Executed the command immediately without controlling host resources.
 *  
 * @author pcingola
 */
public class ExecutionerSys extends Executioner {

	public ExecutionerSys(Config config) {
		super(config);
	}

	/**
	 * Create a CmdRunner to execute the script
	 * @param task
	 * @param host
	 * @return
	 */
	@Override
	protected Cmd createCmd(Task task) {
		task.createProgramFile(); // We must create a program file

		// Create command line
		ArrayList<String> args = new ArrayList<String>();
		for (String arg : CmdLocal.LOCAL_EXEC_COMMAND)
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
		CmdLocal cmd = new CmdLocal(task.getId(), args.toArray(Cmd.ARGS_ARRAY_TYPE));
		cmd.setTask(task);
		cmd.setExecutioner(this);
		cmd.setReadPid(true); // We execute using "bds exec" which prints PID number before executing the sub-process
		cmd.setDebug(debug);
		cmdById.put(task.getId(), cmd);

		return cmd;
	}

	@Override
	protected void follow(Task task) {
		// We need to feed the InputStreams from the process, instead of file names
		CmdLocal cmd = (CmdLocal) cmdById.get(task.getId());

		// Wait for cmd thread to start, STDOUT and STDERR to became available
		while (!cmd.isStarted() || (cmd.getStdout() == null) || (cmd.getStderr() == null))
			sleepShort();

		// Add to tail
		tail.add(cmd.getStdout(), task.getStdoutFile(), false);
		tail.add(cmd.getStderr(), task.getStderrFile(), true);
	}

	@Override
	public String osKillCommand(Task task) {
		return "kill";
	}
}
