package org.bds.osCmd;

import org.bds.BdsLog;
import org.bds.cluster.host.Host;
import org.bds.cluster.host.HostResources;
import org.bds.executioner.Executioner;
import org.bds.executioner.NotifyTaskState;
import org.bds.executioner.PidParser;
import org.bds.run.BdsThread;
import org.bds.task.Task;
import org.bds.task.TaskState;

/**
 * Execute a command (typically a shell command)
 *
 * @author pcingola
 */
public abstract class Cmd extends Thread implements BdsLog {

	public static final String[] ARGS_ARRAY_TYPE = new String[0];
	public static final int ERROR_EXECUTING = -1;

	protected boolean debug;
	protected String id;
	protected String commandArgs[]; // Command and arguments
	protected String error = ""; // Errors
	private boolean executing = false; // Command state: Executing
	private boolean started = false; // Command state: Started
	protected int exitValue = 0; // Command exit value
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
	 * Update internal command state: Done
	 */
	protected void cmdStateDone() {
		started = true;
		executing = false;
	}

	/**
	 * Update internal command state: Executing
	 */
	protected void cmdStateExecuting() {
		executing = true;
	}

	/**
	 * Update internal command state: Running
	 */
	protected void cmdStateRunning() {
		started = true;
	}

	/**
	 * Update internal command state: Started
	 */
	protected void cmdStateStarted() {
		started = true;
	}

	/**
	 * Execute command
	 * @return exitCode
	 */
	public int exec() {
		// Prepare to execute task
		try {
			debug("Start command '" + id + "'");
			cmdStateExecuting();

			// Prepare to execute
			if (execPrepare()) {
				cmdStateStarted();
				stateStarted(); // We are ready to launch. Update states
			} else {
				execError(null, TaskState.START_FAILED, BdsThread.EXITCODE_ERROR);
				return exitValue;
			}
		} catch (Throwable t) {
			execError(t, TaskState.START_FAILED, BdsThread.EXITCODE_ERROR);
			return exitValue;
		}

		// Execute command or wait for execution to finish
		try {
			cmdStateRunning(); // Command is executing
			stateRunningBefore(); // Change state before executing command
			debug("Running command '" + id + "'");
			execCmd();
			stateRunningAfter(); // Change state after executing command (e.g. when sending a task to a cluster system)
		} catch (Throwable t) {
			execError(t, TaskState.ERROR, BdsThread.EXITCODE_ERROR);
			return exitValue;
		}

		// OK, we are done. Clean up and notify.
		debug("Done command '" + id + "'");
		execDone();
		cmdStateDone(); // Command is done
		stateDone(); // Change state after finished
		return exitValue;
	}

	/**
	 * Execute command
	 */
	protected abstract void execCmd() throws Exception;

	/**
	 * Clean up after finished executing a command
	 */
	protected void execDone() {
		// Nothing to do
	}

	/**
	 * Error while trying to 'exec' of a command, update states
	 */
	protected void execError(Throwable t, TaskState taskState, int exitCode) {
		cmdStateDone();
		exitValue = exitCode;
		stateDone(taskState);
		addError(t != null ? t.getMessage() : null);
		if (debug && t != null) {
			error("Error executing command '" + id + "':");
			t.printStackTrace();
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

	@Override
	public boolean isDebug() {
		return debug;
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
		debug("Process killed");

		killCmd();

		// Notify end of execution
		if (task != null) {
			task.setExitValue(BdsThread.EXITCODE_KILLED);
			if (notifyTaskState != null) notifyTaskState.taskFinished(task, TaskState.KILLED);
		}

	}

	/**
	 * Cmd-specfic implementation: How to kill the process.
	 */
	protected abstract void killCmd();

	@Override
	public String logMessagePrepend() {
		return "Cmd '" + getCmdId() + "'";
	}

	/**
	 * Update states: We are done. Either process finished or an error happened
	 */
	protected void notifyDone(TaskState taskState) {
		if (task != null) {
			task.setExitValue(exitValue);
			// Notify end of execution
			if (notifyTaskState != null) notifyTaskState.taskFinished(task, taskState);
		}

	}

	/**
	 * Update states: Running
	 */
	protected void notifyRunning() {
		if (notifyTaskState != null) {
			notifyTaskState.taskRunning(task);
			if (task.isDetached()) notifyTaskState.taskFinished(task, TaskState.DETACHED);
		}
	}

	/**
	 * Change state: Started
	 */
	protected void notifyStarted() {
		if (notifyTaskState != null) notifyTaskState.taskStarted(task);
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

	protected void stateDone() {
		stateDone(null);
	}

	protected void stateDone(TaskState taskState) {
		notifyDone(taskState);
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
		notifyRunning();
	}

	protected void stateStarted() {
		notifyStarted();
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
