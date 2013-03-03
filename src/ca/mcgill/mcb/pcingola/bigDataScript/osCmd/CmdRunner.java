package ca.mcgill.mcb.pcingola.bigDataScript.osCmd;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostResources;
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
 * @author pcingola
 */
public class CmdRunner extends Thread implements Progress {

	public static boolean debug = false;

	String cmdId;
	String commandArgs[]; // Command and arguments
	String error = ""; // Errors
	String pwd = null; // Path to command

	boolean quietStdout = false; // Be quite (i.e. do not copy to stdout )
	boolean quietStderr = false; // Be quite (i.e. do not copy to stderr )
	boolean saveStd = false; // Save lines to buffer
	boolean executing = false, started = false; // Command states
	boolean binaryStdout = false; // Is STDOUT binary?
	boolean binaryStderr = false; // Is STDERR binary? (this would be really odd)
	boolean showExceptions = true; // Show exceptions when running the program
	boolean timedOut = false; // Did the process time out?
	int progress = 0; // Any way to measure progress?
	int exitValue = 0; // Command exit value
	String redirectStdout = null; // Where to redirect STDOUT
	String redirectStderr = null; // Where to redirect STDERR
	TaskStats cmdStats = null; // Notify this object when we are done
	OutputStream stdin = null; // We write to command's STDIN (so for us is an output stream)
	StreamGobbler stdErrGobbler = null, stdOutGobbler = null; // Gobblers for command's STDOUT and STDERR
	LineFilter stdOutFilter = null; // Line filter: Keep (and show) everything from STDOUT that matches this filter
	Host host; // Host to execute command (in case it's ssh)
	HostResources resources; // Resources required by this command
	Process process; // Java process (the one that actually executes our command) 

	public CmdRunner(String cmdId, String args[]) {
		this.cmdId = cmdId;
		commandArgs = args;
		resources = new HostResources();
	}

	public CmdRunner(String cmdId, String command) {
		this.cmdId = cmdId;
		commandArgs = new String[1];
		commandArgs[0] = command;
		resources = new HostResources();
	}

	/**
	 * Create a timeout command if needed 
	 * Note: There is no reasonable, standard, platform independent way to kill a 
	 * 			process and all it's sub-processes in Java. Furthermore, there is no way to
	 * 			get the PID of a process and send a kill signal to it (or to the group).
	 * 			Anything we do here will be an OS-dependent hack.
	 * 			The most "elegant" way to solve it is to use the "timeout" command (Unix) to 
	 * 			execute the script. 
	 * 
	 * 			Other OS? I don't know. I guess I'll have to provide a "timeout" command...
	 */
	protected void createTimeoutCommand() {
		// Timeout requested in resources?
		if (resources.getTimeout() > 0) {
			// Create a new command line using "timeout numSecs oldCommandLine..."
			ArrayList<String> newCommandArgs = new ArrayList<String>();
			newCommandArgs.add("timeout");
			newCommandArgs.add(resources.getTimeout() + ""); // Command line accepts timeout in seconds
			for (String arg : commandArgs)
				newCommandArgs.add(arg);

			Gpr.debug("TImeout command added!");
			// OK use new command line
			commandArgs = newCommandArgs.toArray(new String[0]);
		}
	}

	public int exec() {
		try {
			createTimeoutCommand(); // Handle timeout requirement here (if possible)

			executing = true;
			ProcessBuilder pb = new ProcessBuilder(commandArgs);
			if (pwd != null) pb.directory(new File(pwd));

			if (debug) {
				Gpr.debug("PWD: " + pwd);
				for (String arg : commandArgs)
					Gpr.debug("ARGS: " + arg);
			}

			process = pb.start();

			//---
			// Prepare & start stdout/sdterr reader processes
			//---
			stdErrGobbler = new StreamGobbler(getErrorStream(), true); // StdErr
			stdOutGobbler = new StreamGobbler(getOutputStream(), false); // StdOut

			// Quiet? => Do not show
			if (quietStderr) stdErrGobbler.setQuietMode();
			if (quietStdout) stdOutGobbler.setQuietMode();

			// Keep a copy in memory?
			stdErrGobbler.setSaveLinesInMemory(saveStd);
			stdOutGobbler.setSaveLinesInMemory(saveStd);

			// Binary?
			stdErrGobbler.setBinary(binaryStderr);
			stdOutGobbler.setBinary(binaryStdout);

			// Redirect?
			if (redirectStderr != null) stdErrGobbler.setRedirectTo(redirectStderr);
			if (redirectStdout != null) stdOutGobbler.setRedirectTo(redirectStdout);

			// Filter stdout
			stdOutGobbler.setLineFilter(stdOutFilter);

			// Set this object as the progress monitor
			stdErrGobbler.setProgress(this);
			stdOutGobbler.setProgress(this);

			// Start gobblers
			stdErrGobbler.start();
			stdOutGobbler.start();

			// Assign StdIn
			stdin = process.getOutputStream();

			//---
			// Start process & wait until completion
			//---
			started = true;

			// Wait for the process to finish and store exit value
			exitValue = process.waitFor();

			// Wait for gobblers to finish processing the remaining of STDIN & STDERR
			while (stdOutGobbler.isRunning() || stdErrGobbler.isRunning())
				Thread.sleep(100);

			if (debug && (exitValue != 0)) Gpr.debug("Exit value: " + exitValue);
		} catch (Exception e) {
			error = e.getMessage() + "\n";
			exitValue = -1;
			if (showExceptions) e.printStackTrace();
		} finally {
			// We are done. Either process finished or an exception was raised.
			started = true;
			executing = false;
			if (cmdStats != null) {
				// Add command stats (now we only have exitValue)
				cmdStats.setExitValue(exitValue);
				cmdStats.setDone(true);
			}
		}
		return exitValue;
	}

	public String getCmdId() {
		return cmdId;
	}

	public String[] getCommandArgs() {
		return commandArgs;
	}

	public String getError() {
		return error;
	}

	/**
	 * This is STDERR from the process
	 * @return
	 */
	protected InputStream getErrorStream() {
		return process.getErrorStream();
	}

	public int getExitValue() {
		return exitValue;
	}

	/**
	 * First lines of stdout
	 */
	public String getHead() {
		if (stdOutGobbler != null) return stdOutGobbler.getHead();
		return "";
	}

	/**
	 * First lines of stderr
	 */
	public String getHeadStderr() {
		if (stdErrGobbler != null) return stdErrGobbler.getHead();
		return "";
	}

	public Host getHost() {
		return host;
	}

	/**
	 * This is STDOUT from the process
	 * @return
	 */
	protected InputStream getOutputStream() {
		return process.getInputStream();
	}

	@Override
	public int getProgress() {
		return progress;
	}

	public String getPwd() {
		return pwd;
	}

	public String getRedirectStderr() {
		return redirectStderr;
	}

	public String getRedirectStdout() {
		return redirectStdout;
	}

	public HostResources getResources() {
		return resources;
	}

	public String getStderr() {
		return stdErrGobbler == null ? "" : stdErrGobbler.getAllLines();
	}

	public OutputStream getStdin() {
		return stdin;
	}

	public String getStdout() {
		return stdOutGobbler == null ? "" : stdOutGobbler.getAllLines();
	}

	public LineFilter getStdOutFilter() {
		return stdOutFilter;
	}

	public boolean isAlertDone() {
		return stdOutGobbler.isAlertDone();
	}

	public boolean isBinaryStderr() {
		return binaryStderr;
	}

	public boolean isBinaryStdout() {
		return binaryStdout;
	}

	public boolean isDone() {
		return started && !executing;
	}

	public boolean isExecuting() {
		return executing;
	}

	public boolean isQuiet() {
		return quietStdout;
	}

	public boolean isSaveStd() {
		return saveStd;
	}

	public boolean isStarted() {
		return started;
	}

	/**
	 * Kill a process
	 */
	public void kill() {
		if (process != null) {
			process.destroy();
			error += "Killed!\n";
		}

		// Update status
		if (cmdStats != null) {
			if (debug) Gpr.debug("Killed: Setting stats " + cmdStats);
			cmdStats.setExitValue(-1);
			cmdStats.setDone(true);
		}

		if (debug) Gpr.debug("Process was killed");
	}

	/**
	 * Report progress
	 */
	@Override
	public void progress() {
		progress++;
	}

	public void resetBuffers() {
		stdOutGobbler.resetBuffer();
		stdErrGobbler.resetBuffer();
	}

	@Override
	public void run() {
		if (debug) Gpr.debug("Running ExecOsCommand thread");
		exec();
	}

	public void setBinaryStderr(boolean binaryStderr) {
		this.binaryStderr = binaryStderr;
	}

	public void setBinaryStdout(boolean binaryStdout) {
		this.binaryStdout = binaryStdout;
	}

	public void setCmdStats(TaskStats cmdStats) {
		this.cmdStats = cmdStats;
	}

	public void setCommandArgs(String[] commandArgs) {
		this.commandArgs = commandArgs;
	}

	public void setHost(Host host) {
		this.host = host;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public void setQuiet(boolean quietStdout, boolean quietStderr) {
		this.quietStdout = quietStdout;
		this.quietStderr = quietStderr;
	}

	public void setRedirectStderr(String redirectStderr) {
		this.redirectStderr = redirectStderr;
	}

	public void setRedirectStdout(String redirectStdout) {
		this.redirectStdout = redirectStdout;
	}

	public void setResources(HostResources resources) {
		this.resources = resources;
	}

	public void setSaveStd(boolean saveStd) {
		this.saveStd = saveStd;
	}

	public void setShowExceptions(boolean showExceptions) {
		this.showExceptions = showExceptions;
	}

	public void setStdoutAlert(String alert) {
		stdOutGobbler.setAlert(alert);
	}

	public void setStdoutAlertNotify(Object toBeNotified) {
		stdOutGobbler.setAlertNotify(toBeNotified);
	}

	public void setStdOutFilter(LineFilter stdOutFilter) {
		this.stdOutFilter = stdOutFilter;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (String c : commandArgs)
			sb.append(c + " ");
		return sb.toString();
	}

}
