package ca.mcgill.mcb.pcingola.bigDataScript.executioner;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;

import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * TaskLogger log stale task processes (PID) and files into a file. 
 * A parent bds-exec process (i.e. the GO program that invokes BigDataScript 
 * Java class) will parse the file and:
 * 		i) Kill remaining processes invoking appropriate commands (kill, qdel, etc.)
 * 		ii) Remove stale file from unfinished tasks
 * 
 * @author pcingola
 */
public class TaskLogger {

	public static final String CMD_REMOVE_FILE = "rm";

	boolean debug = false;
	String pidFile;
	HashSet<String> pids;

	public TaskLogger(String pidFile) {
		this.pidFile = pidFile;
		pids = new HashSet<String>();
		if (debug) Gpr.debug("Creating PID logger " + pidFile);
	}

	/**
	 * Add a task and the corresponding executioner
	 * @param t
	 * @param executioner
	 */
	public synchronized void add(Task t, Executioner executioner) {
		StringBuilder lines = new StringBuilder();

		// Add pid
		String pid = t.getPid();
		pids.add(pid);

		//---
		// Append process PID
		//---

		// Prepare kill command
		StringBuilder cmdsb = new StringBuilder();
		String[] osKillCommand = executioner.osKillCommand(t);
		if (osKillCommand != null) {
			for (String c : executioner.osKillCommand(t))
				cmdsb.append(" " + c);
		}
		String cmd = cmdsb.toString().trim();

		// Append process entry
		lines.append(t.getPid() + "\t+\t" + cmd + "\n");

		//---
		// Append task output files. 
		// Note: If this task does not finish (e.g. Ctrl-C), we have to remove these files.
		//       If the task finished OK, we mark them not to be removed
		//---
		for (String file : t.getOutputFiles())
			lines.append(file + "\t+\t" + CMD_REMOVE_FILE + "\n");

		//---
		// Append all lines to file
		//---
		append(lines.toString());
	}

	/**
	 * Append a string to the pidFile
	 * @param str
	 */
	protected void append(String str) {
		try {
			if (debug) Gpr.debug("Appending string to pidLogger:\tPidFile: '" + pidFile + "'\tString: '" + str + "'");
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(pidFile, true)));
			out.print(str);
			out.close(); // We need to flush this as fast as possible to avoid missing PID values in the file
		} catch (IOException e) {
			throw new RuntimeException("Error appending information to file '" + pidFile + "'\n", e);
		}
	}

	public HashSet<String> getPids() {
		return pids;
	}

	/**
	 * Remove a task
	 * @param t
	 */
	public synchronized void remove(Task t) {
		// Remove PID
		String pid = t.getPid();
		pids.remove(pid);

		StringBuilder lines = new StringBuilder();

		// Append process PID
		lines.append(t.getPid() + "\t-\n");

		// Append task output files. 
		for (String file : t.getOutputFiles())
			lines.append(file + "\t-\n");

		// Append all lines to file
		append(lines.toString());
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

}
