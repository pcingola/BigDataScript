package ca.mcgill.mcb.pcingola.bigDataScript.executioner;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashSet;

import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

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
		if (pidFile == null) throw new RuntimeException("Cannot initialize using a null file!");
		this.pidFile = pidFile;
		pids = new HashSet<String>();
		if (debug) Gpr.debug("Creating PID logger " + pidFile);
	}

	/**
	 * Add a task and the corresponding executioner
	 */
	public synchronized void add(Task task, Executioner executioner) {
		StringBuilder lines = new StringBuilder();

		// Add pid
		String pid = task.getPid();
		pids.add(pid);

		//---
		// Append process PID
		//---

		// Prepare kill command
		StringBuilder cmdsb = new StringBuilder();
		String[] osKillCommand = executioner.osKillCommand(task);
		if (osKillCommand != null) {
			for (String c : executioner.osKillCommand(task))
				cmdsb.append(" " + c);
		}
		String cmd = cmdsb.toString().trim();

		// Append process entry
		lines.append(task.getPid() + "\t+\t" + cmd + "\n");

		//---
		// Append task output files.
		// Note: If this task does not finish (e.g. Ctrl-C), we have to remove these files.
		//       If the task finished OK, we mark them not to be removed
		//---
		if (task.getOutputs() != null) {
			for (String file : task.getOutputs())
				lines.append(file + "\t+\t" + CMD_REMOVE_FILE + "\n");
		}

		//---
		// Append all lines to file
		//---
		append(lines.toString());
	}

	/**
	 * Append a string to the pidFile
	 */
	protected void append(String str) {
		try {
			if (debug) Timer.showStdErr("TaskLogger: Appending to PidFile '" + pidFile + "', lines:\n" + Gpr.prependEachLine("\t\t|", str));
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(pidFile, true)));
			out.print(str);
			out.close(); // We need to flush this as fast as possible to avoid missing PID values in the file
		} catch (Exception e) {
			throw new RuntimeException("Error appending information to file '" + pidFile + "'\n", e);
		}
	}

	public HashSet<String> getPids() {
		return pids;
	}

	/**
	 * Remove a task
	 */
	public synchronized void remove(Task task) {
		// Remove PID
		String pid = task.getPid();
		pids.remove(pid);

		StringBuilder lines = new StringBuilder();

		// Append process PID
		lines.append(task.getPid() + "\t-\n");

		// Append task output files.
		if (task.getOutputs() != null) {
			for (String file : task.getOutputs())
				lines.append(file + "\t-\n");
		}

		// Append all lines to file
		append(lines.toString());
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

}
