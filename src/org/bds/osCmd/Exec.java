package org.bds.osCmd;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.List;

import org.bds.BdsLog;
import org.bds.Config;
import org.bds.run.BdsThread;
import org.bds.run.BdsThreads;

/**
 * Execute a command, collect stdout, stderr and exitValue
 * and return them in an ExecResult
 *
 * @author pcingola
 */
public class Exec implements BdsLog {

	public static final String USER_DIR = "user.dir";

	boolean quiet;
	boolean saveLinesInMemory;
	int exitValue;
	String stdOutStr;
	String stdErrStr;

	/**
	 * How many running threads do we have?
	 */
	public static int countRunningThreads() {
		return Math.max(java.lang.Thread.activeCount(), ManagementFactory.getThreadMXBean().getThreadCount());
	}

	/**
	 * Execute a program
	 */
	public static ExecResult exec(List<String> args, boolean quiet) {
		Exec exec = new Exec();
		exec.setQuiet(quiet);
		exec.setSaveLinesInMemory(true);
		return exec.run(args);
	}

	protected ExecResult run(List<String> args) {
		Config conf = Config.get();
		log("Executing command. Arguments: " + args);

		// Create a command string
		StringBuilder cmdsb = new StringBuilder();
		for (String arg : args)
			cmdsb.append(" " + arg);
		String commands = cmdsb.toString().trim();

		// Run commands line
		exitValue = -1;
		StreamGobbler stdout = null, stderr = null;
		try {
			ProcessBuilder pb = new ProcessBuilder(args);

			// Make sure we use bdsThread's current directory or startup
			// directory if this Exec is not within a bdsThread
			BdsThread bdsThread = BdsThreads.getInstance().get();
			String currDir = bdsThread != null ? bdsThread.getCurrentDir() : System.getProperty(Exec.USER_DIR);
			pb.directory(new File(currDir));

			// Create a process
			Process process = pb.start();

			// Make sure we read STDOUT and STDERR, so that process does not block
			stdout = new StreamGobbler(process.getInputStream(), false);
			stderr = new StreamGobbler(process.getErrorStream(), true);

			stdout.setSaveLinesInMemory(saveLinesInMemory);
			stderr.setSaveLinesInMemory(saveLinesInMemory);

			if (quiet) {
				stdout.setQuietMode();
				stderr.setQuietMode();
			}
			stdout.start();
			stderr.start();

			// Wait for process to finish
			exitValue = process.waitFor();

			// Wait for Gobblers to finish (otherwise we may have an incomplete stdout/stderr)
			stdout.join();
			stderr.join();

			debug("Exit value: " + exitValue);
		} catch (Exception e) {
			throw new RuntimeException("Cannot execute commnads: '" + commands + "'", e);
		}

		// Collect output
		if (stdout != null) stdOutStr = stdout.getAllLines();
		if (stderr != null) stdErrStr = stderr.getAllLines();

		return new ExecResult(stdOutStr, stdErrStr, exitValue);
	}

	public void setQuiet(boolean quiet) {
		this.quiet = quiet;
	}

	public void setSaveLinesInMemory(boolean saveLinesInMemory) {
		this.saveLinesInMemory = saveLinesInMemory;
	}
}
