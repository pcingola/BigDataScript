package ca.mcgill.mcb.pcingola.bigDataScript.osCmd;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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
 * WARNING: In this case, we assume the child process takes care of redirections and we DO NOT take care of STDIN, STDOUT or timeout
 * 
 * @author pcingola
 */
public class CmdRunner extends Thread {

	public static String LOCAL_EXEC_COMMAND[] = { "bds", "exec" };
	public static String LOCAL_KILL_COMMAND[] = { "bds", "kill" };
	public static final String[] ARGS_ARRAY_TYPE = new String[0];

	public static boolean debug = false;

	String id;
	String commandArgs[]; // Command and arguments
	String error = ""; // Errors
	boolean readPid;
	boolean executing = false, started = false; // Command states
	int pid; // Only if child process reports PID and readPid is true
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
			if (debug) {
				StringBuilder cmdsb = new StringBuilder();
				for (String arg : commandArgs)
					cmdsb.append(" " + arg);
				Gpr.debug("Executing: " + cmdsb);
			}
			process = pb.start();

			// Child process prints PID to stdout: Read it
			if (readPid) pid = readPid();

			started = true;
			exitValue = process.waitFor(); // Wait for the process to finish and store exit value
			if (debug) Gpr.debug("Exit value: " + exitValue);
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
			// Do we have a PID number? Kill using that number
			if (pid > 0) killBds(pid);

			error += "Killed!\n";
			if (debug) Gpr.debug("Killing process " + id);
			process.destroy();
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

	/**
	 * Send a kill signal using 'bds kill'
	 * @param pid
	 */
	void killBds(int pid) {
		// Create arguments
		ArrayList<String> args = new ArrayList<String>();
		for (String arg : LOCAL_KILL_COMMAND)
			args.add(arg);
		args.add("" + pid);

		try {
			// Execute 'bds kill pid'
			Process proc = Runtime.getRuntime().exec(args.toArray(ARGS_ARRAY_TYPE));
			if (debug) Gpr.debug("Executing kill process for pid " + pid);
			int exitVal = proc.waitFor();
			if (exitVal != 0) System.err.println("Error killing process " + pid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Read child process pid
	 * @return
	 * @throws InterruptedException
	 * @throws IOException
	 */
	int readPid() throws InterruptedException, IOException {
		// Read PID from first line of child process

		// Wait for STDOUT to become available
		while (getStdout() == null)
			sleep(1);

		// Read one line
		StringBuilder sb = new StringBuilder();
		while (true) {
			char ch = (char) getStdout().read();
			if (ch == '\n') break;
			sb.append(ch);
		}

		// Parse line. Format "PID \t pidNum \t childPidNum"
		if (debug) Gpr.debug("Got line: '" + sb + "'");
		String fields[] = sb.toString().split("\t");
		if (!fields[0].equals("PID")) throw new RuntimeException("Expecting 'PID', received: '" + sb + "'");
		return Gpr.parseIntSafe(fields[1]);
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

	public void setReadPid(boolean readPid) {
		this.readPid = readPid;
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
