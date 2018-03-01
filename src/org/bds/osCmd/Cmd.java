package org.bds.osCmd;

import org.bds.cluster.host.Host;
import org.bds.cluster.host.HostResources;
import org.bds.executioner.Executioner;
import org.bds.executioner.NotifyTaskState;
import org.bds.executioner.PidParser;
import org.bds.task.Task;
import org.bds.task.TaskState;
import org.bds.util.Timer;

/**
 * Execute a command (shell command)
 *
 * @author pcingola
 */
public abstract class Cmd extends Thread {

	public static final String[] ARGS_ARRAY_TYPE = new String[0];
	public static final int ERROR_EXECUTING = -1;

	protected boolean debug;
	protected String id;
	protected String commandArgs[]; // Command and arguments
	protected String error = ""; // Errors
	protected boolean executing = false;
	protected boolean started = false; // Command states
	protected int exitValue = 0; // Command exit map
	protected Task task = null; // Task corresponding to this cmd
	protected Host host; // Host to execute command (in case it's ssh)
	protected HostResources resources; // Resources required by this command
	protected NotifyTaskState notifyTaskState; // Notify executioner when command finishes executing
	protected PidParser pidParser; // Parse PID from command line

	public Cmd(String id, String args[]) {
		this.id = id;
		commandArgs = args;
		resources = new HostResources();
	}

	/**
	 * Append error message
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
	 * @return exitCode
	 */
	public int exec() {
		// Prepare to execute task
		try {
			if (debug) log("Start");
			executing = true;

			// Prepare to execute
			if (execPrepare()) stateStarted(); // We are ready to launch. Update states
			else {
				execError(null, TaskState.START_FAILED, Task.EXITCODE_ERROR);
				return exitValue;
			}
		} catch (Throwable t) {
			execError(t, TaskState.START_FAILED, Task.EXITCODE_ERROR);
			return exitValue;
		}

		// Execute command or wait for execution to finish
		try {
			stateRunningBefore(); // Change state before executing command
			if (debug) log("Running");
			execCmd();
			stateRunningAfter(); // Change state after executing command (e.g. when sending a task to a cluster system)
		} catch (Throwable t) {
			execError(t, TaskState.ERROR, Task.EXITCODE_ERROR);
			return exitValue;
		}

		// OK, we are done. Clean up and notify.
		if (debug) log("Done");
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
		if (task != null) {
			task.setExitValue(exitValue);
			if (notifyTaskState != null) notifyTaskState.taskFinished(task, null); // Notify end of execution
		}
	}

	/**
	 * Error while trying to 'exec' of a command, update states
	 */
	protected void execError(Throwable t, TaskState taskState, int exitCode) {
		stateDone();
		exitValue = exitCode;

		addError(t != null ? t.getMessage() : null);

		if (debug && t != null) t.printStackTrace();

		if (task != null) {
			task.setExitValue(exitCode);
			if (notifyTaskState != null) notifyTaskState.taskFinished(task, taskState);
		}
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
		if (debug) log("Process killed");

		killCmd();

		// Notify end of execution
		if (task != null) {
			task.setExitValue(Task.EXITCODE_KILLED);
			if (notifyTaskState != null) notifyTaskState.taskFinished(task, TaskState.KILLED);
		}

	}

	/**
	 * Cmd-specfic implementation: How to kill the process.
	 */
	protected abstract void killCmd();

	public void log(String msg) {
		Timer.showStdErr(getClass().getSimpleName() + " '" + getCmdId() + "': " + msg);
	}

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
		notifyTaskState = executioner;
		pidParser = executioner;
	}

	public void setHost(Host host) {
		this.host = host;
	}

	public void setNotifyTaskState(NotifyTaskState notifyTaskState) {
		this.notifyTaskState = notifyTaskState;
	}

	public void setPidParser(PidParser pidParser) {
		this.pidParser = pidParser;
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
		if (notifyTaskState != null) notifyTaskState.taskRunning(task);
	}

	/**
	 * Change state after executing command
	 */
	protected void stateRunningAfter() {
		// Nothing to do
	}

	/**
	 * Change state before executing command
	 */
	protected void stateRunningBefore() {
		stateRunning();
	}

	protected void stateStarted() {
		started = true;
		if (notifyTaskState != null) notifyTaskState.taskStarted(task);
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
