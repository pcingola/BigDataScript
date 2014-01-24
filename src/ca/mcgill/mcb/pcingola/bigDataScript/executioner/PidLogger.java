package ca.mcgill.mcb.pcingola.bigDataScript.executioner;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;

import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * Log PIDs to a file
 * 
 * @author pcingola
 */
public class PidLogger {

	boolean debug = false;
	String pidFile;
	HashSet<String> pids;

	public PidLogger(String pidFile) {
		this.pidFile = pidFile;
		pids = new HashSet<String>();
		if (debug) Gpr.debug("Creating PID logger " + pidFile);
	}

	/**
	 * Add entry 'pid' to pidFile
	 * @param pid
	 */
	protected void add(String pid, String cmd) {
		if (pid == null) return;
		pids.add(pid);
		append(pid + "\t\t" + cmd);
	}

	/**
	 * Add a task and the corresponding executioner
	 * @param t
	 * @param executioner
	 */
	public synchronized void add(Task t, Executioner executioner) {
		StringBuilder sb = new StringBuilder();

		// Prepare kill command
		String[] osKillCommand = executioner.osKillCommand(t);
		if (osKillCommand != null) {
			for (String c : executioner.osKillCommand(t))
				sb.append(" " + c);
		}

		add(t.getPid(), sb.toString().trim());
	}

	/**
	 * Append a string to the pidFile
	 * @param str
	 */
	protected void append(String str) {
		try {
			if (debug) Gpr.debug("Appending string to pidLogger:\tString: '" + str + "'\t\tPidFile: '" + pidFile + "'");
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(pidFile, true)));
			out.println(str);
			out.close(); // We need to flush this as fast as possible to avoid missing PID values in the file
		} catch (IOException e) {
			throw new RuntimeException("Error appending information to file '" + pidFile + "'\n", e);
		}
	}

	public HashSet<String> getPids() {
		return pids;
	}

	/**
	 * Add 'remove' entry to pidFile
	 * @param pid
	 */
	void remove(String pid) {
		if (pid == null) return;
		pids.remove(pid);
		append(pid + "\t-");
	}

	/**
	 * Remove a task
	 * @param t
	 */
	public synchronized void remove(Task t) {
		remove(t.getPid());
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

}
