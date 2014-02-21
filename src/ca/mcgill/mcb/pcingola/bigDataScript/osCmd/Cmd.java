package ca.mcgill.mcb.pcingola.bigDataScript.osCmd;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostResources;
import ca.mcgill.mcb.pcingola.bigDataScript.executioner.Executioner;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task.TaskState;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * Execute a command (shell command)
 * 
 * @author pcingola
 */
public abstract class Cmd extends Thread {

	public static final String[] ARGS_ARRAY_TYPE = new String[0];
	public static final int ERROR_EXECUTING = -1;

	protected boolean debug = false;

	protected String id;
	protected String commandArgs[]; // Command and arguments
	protected String error = ""; // Errors
	protected boolean executing = false;
	protected boolean started = false; // Command states
	protected int exitValue = 0; // Command exit value
	protected Task task = null; // Task corresponding to this cmd
	protected Host host; // Host to execute command (in case it's ssh)
	protected HostResources resources; // Resources required by this command
	protected Executioner executioner; // Notify executioner when this command finishes executing

	public Cmd(String id, String args[]) {
		this.id = id;
		commandArgs = args;
		resources = new HostResources();
	}

	/**
	 * Append error message
	 * @param errMsg
	 */
	protected void addError(String errMsg) {
		if (errMsg != null) {
			if (error == null) error = "";
			error += errMsg;
			if (task != null) task.setErrorMsg(error);
		}
	}

	/**
	 * Execute command
	 * @return
	 */
	public int exec() {
		// Prepare to execute task
		try {
			executing = true;

			// Prepare to execute
			if (execPrepare()) stateStarted(); // Now we are really done and the process is started. Update states
			else {
				execError(null, TaskState.START_FAILED, Task.EXITCODE_ERROR);
				return exitValue;
			}
		} catch (Exception e) {
			execError(e, TaskState.START_FAILED, Task.EXITCODE_ERROR);
			return exitValue;
		}

		// Execute command or wait for execution to finish
		try {
			stateRunning(); // Now we are really done and the process is started. Update states
			execCmd();
		} catch (Exception e) {
			execError(e, TaskState.ERROR, Task.EXITCODE_ERROR);
			return exitValue;
		}

		// OK, we are done. Clean up and notify.
		execDone();
		return exitValue;
	}

	/**
	 * Execute command
	 */
	protected abstract void execCmd() throws Exception;

	/**
	 * Finished executing a command, update states, notify
	 */
	protected void execDone() {
		stateDone();
		if (executioner != null) executioner.taskFinished(task, null, exitValue); // Notify end of execution
	}

	/**
	 * Error while trying to 'exec' of a command, update states
	 */
	protected void execError(Exception e, TaskState taskState, int exitCode) {
		stateDone();
		exitValue = exitCode;
		addError(e != null ? e.getMessage() : null);
		if (debug && e != null) e.printStackTrace();
		if (executioner != null) executioner.taskFinished(task, taskState, exitCode);
	}

	/**
	 * Prepare to execute
	 */
	protected abstract boolean execPrepare() throws Exception;

	public String getCmdId() {
		return id;
	}

	public String[] getCommandArgs() {
		return commandArgs;
	}

	public String getError() {
		return error;
	}

	public int getExitValue() {
		return exitValue;
	}

	public Host getHost() {
		return host;
	}

	public HostResources getResources() {
		return resources;
	}

	public boolean isDone() {
		return started && !executing;
	}

	public boolean isExecuting() {
		return executing;
	}

	public boolean isStarted() {
		return started;
	}

	/**
	 * Kill a process
	 */
	public void kill() {
		killCmd();

		// Notify end of execution
		if ((executioner != null) && (task != null)) executioner.taskFinished(task, TaskState.KILLED, Task.EXITCODE_KILLED);

		if (debug) Gpr.debug("Process was killed");
	}

	/**
	 * Cmd-specfic implementation: How to kill the process.
	 */
	protected abstract void killCmd();

	@Override
	public void run() {
		exec();
	}

	public void setCommandArgs(String[] commandArgs) {
		this.commandArgs = commandArgs;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setExecutioner(Executioner executioner) {
		this.executioner = executioner;
	}

	public void setHost(Host host) {
		this.host = host;
	}

	public void setResources(HostResources resources) {
		this.resources = resources;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	/**
	 * We are done. Either process finished or an exception was raised.
	 */
	protected void stateDone() {
		started = true;
		executing = false;
	}

	protected void stateRunning() {
		started = true;
		if (executioner != null) executioner.taskRunning(task);
	}

	protected void stateStarted() {
		started = true;
		if (executioner != null) executioner.taskStarted(task);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getSimpleName() + ": ");
		for (String c : commandArgs)
			sb.append(c + " ");
		return sb.toString();
	}

}
