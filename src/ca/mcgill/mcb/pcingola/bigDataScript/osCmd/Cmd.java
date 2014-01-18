package ca.mcgill.mcb.pcingola.bigDataScript.osCmd;

import java.io.InputStream;
import java.io.OutputStream;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostResources;
import ca.mcgill.mcb.pcingola.bigDataScript.executioner.Executioner;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * Execute a command (shell command)
 * 
 * @author pcingola
 */
public abstract class Cmd extends Thread {

	public static final String[] ARGS_ARRAY_TYPE = new String[0];

	public static boolean debug = false;

	protected String id;
	protected String commandArgs[]; // Command and arguments
	protected String error = ""; // Errors
	protected boolean executing = false;
	protected boolean started = false; // Command states
	protected int exitValue = 0; // Command exit value
	protected Task task = null; // Task corresponding to this cmd
	protected Host host; // Host to execute command (in case it's ssh)
	protected HostResources resources; // Resources required by this command
	protected Process process; // Java process (the one that actually executes our command)
	protected Executioner executioner; // Notify when a process finishes

	public Cmd(String id, String args[]) {
		this.id = id;
		commandArgs = args;
		resources = new HostResources();
	}

	public int exec() {
		try {
			executing = true;
			execPrepare();
			started(); // Now we are really done and the process is started. Update states
			execCmd();

		} catch (Exception e) {
			execError(e);
		} finally {
			execDone();
		}
		return exitValue;
	}

	/**
	 * Execute command
	 */
	protected abstract void execCmd();

	/**
	 * Finished 'exec' of a command, update states
	 */
	protected void execDone() {
		// We are done. Either process finished or an exception was raised.
		started = true;
		executing = false;

		if (task != null) task.setExitValue(exitValue); // Update task
		if (executioner != null) executioner.finished(id); // Notify end of execution
	}

	/**
	 * Error while trying to 'exec' of a command, update states
	 */
	protected void execError(Exception e) {
		error = e.getMessage() + "\n";
		exitValue = -1;
		if (debug) e.printStackTrace();
	}

	/**
	 * Prepare to execute
	 */
	protected abstract void execPrepare();

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

	public InputStream getStderr() {
		if (process == null) return null;
		return process.getErrorStream();
	}

	public OutputStream getStdin() {
		if (process == null) return null;
		return process.getOutputStream();
	}

	public InputStream getStdout() {
		if (process == null) return null;
		return process.getInputStream();
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

		// Update task stats
		if (task != null) {
			if (debug) Gpr.debug("Killed: Setting stats for " + id);
			task.stateKilled();
		}

		// Notify end of execution
		if (executioner != null) executioner.finished(id);

		if (debug) Gpr.debug("Process was killed");
	}

	protected abstract void killCmd();

	@Override
	public void run() {
		exec();
	}

	public void setCommandArgs(String[] commandArgs) {
		this.commandArgs = commandArgs;
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

	protected void started() {
		started = true;
		if (task != null) task.stateStarted();
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (String c : commandArgs)
			sb.append(c + " ");
		return sb.toString();
	}

}
