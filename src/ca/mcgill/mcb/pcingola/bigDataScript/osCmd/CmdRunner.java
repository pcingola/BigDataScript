package ca.mcgill.mcb.pcingola.bigDataScript.osCmd;

import java.io.InputStream;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostResources;
import ca.mcgill.mcb.pcingola.bigDataScript.exec.Executioner;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * Launches an 'OS command' (e.g. "ls", "dir")
 * 
 * Note: Launching a system command in Java is not trivial, we need to start 2 threads that read STDOUT and STDERR of
 * the process, otherwise it will block (actually it may even cause a deadlock)
 * 
 * References: 
 * 		http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html?page=1
 * 		http://kylecartmell.com/?p=9
 * 		http://www.kylecartmell.com/public_files/ProcessTimeoutExample.java
 * 
 * WARNING: In this case, we assume the child process takes care of redirections and we DO NOT take care of STDIN and STDOUT
 * 
 * @author pcingola
 */
public class CmdRunner extends Thread {

	public static boolean debug = true;

	String id;
	String commandArgs[]; // Command and arguments
	String error = ""; // Errors
	boolean executing = false, started = false; // Command states
	int exitValue = 0; // Command exit value
	CmdStats cmdStats = null; // Notify this object when we are done
	Host host; // Host to execute command (in case it's ssh)
	HostResources resources; // Resources required by this command
	Process process; // Java process (the one that actually executes our command)
	Executioner executioner; // Notify when a process finishes

	public CmdRunner(String id, String args[]) {
		this.id = id;
		commandArgs = args;
		resources = new HostResources();
	}

	public int exec() {
		try {
			executing = true;
			ProcessBuilder pb = new ProcessBuilder(commandArgs);
			process = pb.start();
			started = true;
			exitValue = process.waitFor(); // Wait for the process to finish and store exit value
			if (debug && (exitValue != 0)) Gpr.debug("Exit value: " + exitValue);
		} catch (Exception e) {
			error = e.getMessage() + "\n";
			exitValue = -1;
			if (debug) e.printStackTrace();
		} finally {
			// We are done. Either process finished or an exception was raised.
			started = true;
			executing = false;

			// Inform command stats 
			if (cmdStats != null) {
				cmdStats.setExitValue(exitValue);
				cmdStats.setDone(true);
			}

			// Notify end of execution
			if (executioner != null) executioner.finished(id);
		}
		return exitValue;
	}

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
		return process.getErrorStream();
	}

	public InputStream getStdout() {
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
		if (process != null) {
			error += "Killed!\n";
		}

		// Update task stats
		if (cmdStats != null) {
			if (debug) Gpr.debug("Killed: Setting stats for " + id);
			cmdStats.setExitValue(-1);
			cmdStats.setDone(true);
		}

		// Notify end of execution
		if (executioner != null) executioner.finished(id);

		if (debug) Gpr.debug("Process was killed");
	}

	@Override
	public void run() {
		exec();
	}

	public void setCmdStats(CmdStats cmdStats) {
		this.cmdStats = cmdStats;
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

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (String c : commandArgs)
			sb.append(c + " ");
		return sb.toString();
	}

}
