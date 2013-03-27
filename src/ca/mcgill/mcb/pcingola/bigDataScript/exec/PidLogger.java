package ca.mcgill.mcb.pcingola.bigDataScript.exec;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;

import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * Log PIDs to a file
 * 
 * @author pcingola
 */
public class PidLogger {

	public static boolean debug = true;

	String pidFile;
	HashSet<Integer> pids;

	public PidLogger(String pidFile) {
		this.pidFile = pidFile;
		pids = new HashSet<Integer>();
		if (debug) Gpr.debug("Creating PID logger " + pidFile);
	}

	/**
	 * Add entry 'pid' to pidFile
	 * @param pid
	 */
	public void add(int pid) {
		if (pid == 0) return;
		pids.add(pid);
		append(pid + "\t+");
	}

	public void add(Task t) {
		add(t.getPid());
	}

	void append(String str) {
		try {
			if (debug) Gpr.debug("Appending to pidLogger: '" + str + "'");
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(pidFile, true)));
			out.println(str);
			out.close(); // We need to flush this as fast as possible to avoid missing PID values in the file
		} catch (IOException e) {
			throw new RuntimeException("Error appending information to file '" + pidFile + "'\n", e);
		}
	}

	public HashSet<Integer> getPids() {
		return pids;
	}

	/**
	 * Add 'remove' entry to pidFile
	 * @param pid
	 */
	public void remove(int pid) {
		if (pid == 0) return;
		pids.remove(pid);
		append(pid + "\t-");
	}

	public void remove(Task t) {
		remove(t.getPid());
	}

}
