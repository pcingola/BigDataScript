package ca.mcgill.mcb.pcingola.bigDataScript.osCmd;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostResources;
import ca.mcgill.mcb.pcingola.bigDataScript.exec.Executioner;
import ca.mcgill.mcb.pcingola.bigDataScript.exec.Task;
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

	public static final int MAX_PID_LINE_LENGTH = 1024; // A 'PID line' should not be longer than this...

	public static String LOCAL_EXEC_COMMAND[] = { "bds", "exec" };
	public static String LOCAL_KILL_COMMAND[] = { "bds", "kill" };
	public static final String[] ARGS_ARRAY_TYPE = new String[0];

	public static boolean debug = false;

	String id;
	String commandArgs[]; // Command and arguments
	String error = ""; // Errors
	String stdin = ""; // Feed this to process' STDIN
	boolean readPid;
	boolean executing = false, started = false; // Command states
	String pid; // Only if child process reports PID and readPid is true
	int exitValue = 0; // Command exit value
	Task task = null; // Task corresponding to this cmd
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

			// Build process and start it
			ProcessBuilder pb = new ProcessBuilder(commandArgs);
			if (debug) {
				StringBuilder cmdsb = new StringBuilder();
				for (String arg : commandArgs)
					cmdsb.append(" " + arg);
				Gpr.debug("Executing: " + cmdsb);
			}
			process = pb.start();

			feedStdin(); // Feed something to STDIN?
			readPid(); // Child process prints PID to STDOUT? Read it
			started(); // Now we are really done and the process is started. Update states

			// Wait for the process to finish and store exit value
			exitValue = process.waitFor();
			if (debug) Gpr.debug("Exit value: " + exitValue);

		} catch (Exception e) {
			execError(e);
		} finally {
			execDone();
		}
		return exitValue;
	}

	/**
	 * Finished 'exec' of a command, update states
	 */
	protected void execDone() {
		// We are done. Either process finished or an exception was raised.
		started = true;
		executing = false;

		// Inform command stats 
		if (task != null) {
			task.setStarted(true);
			task.setExitValue(exitValue);
			task.setDone(true);
		}

		// Notify end of execution
		if (executioner != null) executioner.finished(id);
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
	 * Feed a string to process' STDIN
	 * @throws InterruptedException
	 * @throws IOException
	 */
	protected void feedStdin() throws InterruptedException, IOException {
		if ((stdin == null) || stdin.isEmpty()) return; // Nothing to do

		// Wait for STDOUT to become available
		while (getStdin() == null)
			sleep(1);

		// Write and close STDIN
		BufferedWriter bos = new BufferedWriter(new OutputStreamWriter(getStdin()));
		bos.write(stdin);
		bos.flush();
		bos.close();
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
		if (process != null) {
			// Do we have a PID number? Kill using that number
			int pidNum = Gpr.parseIntSafe(pid);
			if (pidNum > 0) killBds(pidNum);

			error += "Killed!\n";
			if (debug) Gpr.debug("Killing process " + id);
			process.destroy();
		}

		// Update task stats
		if (task != null) {
			if (debug) Gpr.debug("Killed: Setting stats for " + id);
			task.setExitValue(-1);
			task.setDone(true);
		}

		// Notify end of execution
		if (executioner != null) executioner.finished(id);

		if (debug) Gpr.debug("Process was killed");
	}

	/**
	 * Send a kill signal using 'bds kill'
	 * @param pid
	 */
	protected void killBds(int pid) {
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
	 * Read child process pid (or cluster job id)
	 * @return
	 * @throws InterruptedException
	 * @throws IOException
	 */
	protected void readPid() throws InterruptedException, IOException {
		// Nothing to do?
		pid = "";
		if (!readPid) return;

		// Wait for STDOUT to become available
		while (getStdout() == null)
			sleep(1);

		// Read one line.
		// Note: We want to limit the damage if something goes wrong, so we use a small buffer...
		StringBuilder sb = new StringBuilder();
		while (pid.isEmpty()) {
			for (int i = 0; true; i++) {
				char ch = (char) getStdout().read();
				if (ch == '\n') break;
				sb.append(ch);
				if (i >= MAX_PID_LINE_LENGTH) //
					throw new RuntimeException("PID line too long!\n" + sb.toString());
			}

			// Parse line. Format "PID \t pidNum \t childPidNum"
			if (debug) Gpr.debug("Got line: '" + sb + "'");

			pid = sb.toString().trim(); // Ignore empty lines
		}

		task.setPid(pid); // Update task's pid
	}

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

	public void setReadPid(boolean readPid) {
		this.readPid = readPid;
	}

	public void setResources(HostResources resources) {
		this.resources = resources;
	}

	public void setStdin(String stdin) {
		this.stdin = stdin;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	protected void started() {
		started = true;
		task.setStarted(true); // Set as started
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (String c : commandArgs)
			sb.append(c + " ");
		return sb.toString();
	}

}
