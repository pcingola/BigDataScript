package ca.mcgill.mcb.pcingola.bigDataScript.osCmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.Cluster;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostResources;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * A queue of commands to run.
 * They are run in multiple threads (default number of threads = number of CPUs in the computer)
 * 
 * @author pcingola
 */
public class CmdQueue implements Iterable<CmdRunner> {

	Cluster cluster;
	ArrayList<CmdRunner> tasksToRun;
	HashSet<CmdRunner> tasksDone, tasksRunning;
	HashMap<CmdRunner, String> outputFiles;
	boolean verbose = false;
	boolean debug = false;
	boolean redirectToOutput = true;
	boolean keeptaskDone = true;
	boolean running = true;
	boolean checkResources = true;
	int hostIndex = 0;
	long totalMem = 0;
	int sleepTime = 100; // Default sleep time

	public CmdQueue(Cluster cluster) {
		this.cluster = cluster;
		outputFiles = new HashMap<CmdRunner, String>();
		tasksToRun = new ArrayList<CmdRunner>(); // These must be ordered, so we use a list
		tasksDone = new HashSet<CmdRunner>();
		tasksRunning = new HashSet<CmdRunner>();
	}

	/**
	 * Add task to be executed
	 * @param cmd
	 */
	public synchronized void add(CmdRunner cmd) {
		add(cmd, "");
	}

	/**
	 * Add task to be executed, only if 'outputFile' does not exist
	 * @param cmd
	 * @param outputFile
	 */
	public synchronized void add(CmdRunner cmd, String outputFile) {
		tasksToRun.add(cmd);
		outputFiles.put(cmd, outputFile);
	}

	/**
	 * Can we run the next task?
	 * @return true is there at least one host with enough resources to run the next task
	 */
	synchronized boolean canRunNextCmd() {
		if (!checkResources) return true; // Sometimes we don't need to check resources because the infrastructure takes care of that (e.g. cluster qsub commmands)

		HostResources resources = tasksToRun.get(0).getResources();

		for (Host host : cluster) {
			// Does this host have enough resources? 
			if (host.getResources().compareTo(resources) >= 0) return true;
		}

		return false;
	}

	/**
	 * Wait for task to finish
	 */
	synchronized void donetask() {
		if (tasksRunning.isEmpty()) return;

		// Task to delete
		LinkedList<CmdRunner> toDelete = new LinkedList<CmdRunner>();
		for (CmdRunner cmd : tasksRunning)
			if (cmd.isDone()) toDelete.add(cmd);

		// Move from tasksRunning list to tasksDone
		for (CmdRunner cmd : toDelete) {
			if (verbose) {
				if (cmd.getExitValue() > 0) Timer.showStdErr("Error executing task '" + cmd.getCmdId() + "', exit code " + cmd.getExitValue());
				else Timer.showStdErr("Finished task: " + cmd);
			}
			tasksRunning.remove(cmd);

			// Keep track of finished task? 
			if (keeptaskDone) tasksDone.add(cmd);
		}
	}

	/**
	 * Get hosts in round-robin 
	 * @return
	 */
	public synchronized Host getNextHost() {
		List<Host> hosts = cluster.getHosts();
		int size = hosts.size();
		if (size <= 0) return null;
		return hosts.get((hostIndex++) % size);
	}

	public synchronized boolean hasTaskToRun() {
		return !tasksToRun.isEmpty() || !tasksRunning.isEmpty();
	}

	public synchronized boolean isRunning() {
		return running;
	}

	@Override
	public Iterator<CmdRunner> iterator() {
		return tasksToRun.iterator();
	}

	/** 
	 * Kill all task
	 */
	public synchronized void kill() {
		running = false;
		kill(null);

		tasksToRun = new ArrayList<CmdRunner>();
		tasksRunning = new HashSet<CmdRunner>();
	}

	/**
	 * Kill a specific jobId
	 * @param taskId
	 */
	public synchronized void kill(String taskId) {

		ArrayList<CmdRunner> cmdsToKill = new ArrayList<CmdRunner>();

		// Any to kill from 'running' task?
		for (CmdRunner cmd : tasksRunning)
			if ((taskId == null) || taskId.equals(cmd.getCmdId())) cmdsToKill.add(cmd);

		// Any to remove from task to run?
		for (CmdRunner cmd : tasksToRun)
			if ((taskId == null) || taskId.equals(cmd.getCmdId())) cmdsToKill.add(cmd);

		//---
		// Kill and remove
		//---
		for (CmdRunner cmd : cmdsToKill) {
			if (verbose) Timer.showStdErr(" Killing task: " + cmd);
			cmd.kill();
			tasksRunning.remove(cmd);
			tasksToRun.remove(cmd);
			if (keeptaskDone) tasksDone.add(cmd);
		}
	}

	/**
	 * Get next task to be executed
	 * @return
	 */
	synchronized CmdRunner nextCmd() {
		return tasksToRun.remove(0);
	}

	/**
	 * Run task
	 */
	public void run() {
		if (debug) Timer.showStdErr("Starting " + this);

		// Nothing to run?
		if (!hasTaskToRun()) {
			running = false;
			return;
		}

		try {
			running = true;

			// Are there any more task to run?
			while (hasTaskToRun()) {
				// Can we run the next task?
				if (!tasksToRun.isEmpty()) {
					if (!canRunNextCmd()) throw new RuntimeException("Cannot execute next task. Not enough resources.\n\tResources requested: " + tasksToRun.get(0).getResources());

					// Select an appropriate host
					Host host = selectNextHost();

					// Any host available?
					if (host != null) {
						// Get next task and run it
						CmdRunner cmdNext = nextCmd();
						run(cmdNext, host);
					}
				}

				donetask(); // Have task finished?
				sleep();

				if (debug) Timer.showStdErr("Queue tasks:\tPending : " + tasksToRun.size() + "\tRunning: " + tasksRunning.size() + "\tDone: " + tasksDone.size());
			}

		} catch (Throwable t) {
			running = false;
			// Kill all task
			kill();
			throw new RuntimeException("Queue aborted due to exception.", t);
		}

		running = false;
	}

	/**
	 * Run a command
	 */
	synchronized void run(CmdRunner cmd, Host host) {
		// Run only if outFile exists?
		String outputFile = outputFiles.get(cmd);

		cmd.setHost(host);
		if (verbose) Timer.showStdErr("Running task: '" + cmd + "' on " + host);

		if ((outputFile == null) || outputFile.isEmpty()) {
			// No 'outFile'? => Always run
			tasksRunning.add(cmd); // Add to task running
			cmd.start(); // Start thread
		} else {
			// Start cmd only if 'outputFile' does not exist
			if (!Gpr.exists(outputFile)) {
				tasksRunning.add(cmd);

				throw new RuntimeException("Unimplemented!!!");
				//				if (redirectToOutput) cmd.setRedirectStdout(outputFile);
				//
				//				cmd.start();
			} else tasksDone.add(cmd); // Otherwise, we are already done
		}
	}

	/**
	 * Next host to execute this task
	 * @return
	 */
	synchronized Host selectNextHost() {
		HashMap<Host, HostResources> resourcesUsed = new HashMap<Host, HostResources>();

		// Calculate used resources for each host
		for (CmdRunner task : tasksRunning) {
			Host host = task.getHost();
			HostResources hr = resourcesUsed.get(host);

			// No 'used resources'? create a new one
			if (hr == null) {
				hr = new HostResources(host.getResources());
				resourcesUsed.put(host, hr);
			}

			// Account for this taks' resources
			hr.consume(task.getResources());
		}

		// Which host can we use?
		int size = cluster.size();
		CmdRunner nextTask = tasksToRun.get(0);
		for (int i = 0; i < size; i++) {
			Host host = getNextHost();
			if (host.getHealth().isAlive()) {
				HostResources hr = resourcesUsed.get(host);

				// No resources used? then all resources are available
				if (hr == null) hr = host.getResources();

				// Enough resources? 
				if (hr.compareTo(nextTask.getResources()) >= 0) return host;
			}
		}

		return null;
	}

	public void setCheckResources(boolean checkResources) {
		this.checkResources = checkResources;
	}

	public void setRedirectToOutput(boolean redirectToOutput) {
		this.redirectToOutput = redirectToOutput;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public int size() {
		return tasksToRun.size();
	}

	/**
	 * Sleep some time
	 */
	void sleep() {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Queue:\n");
		sb.append("\tSize       : " + tasksToRun.size() + "\n");
		sb.append("\tExecuting  : " + tasksRunning.size() + "\n");
		if (keeptaskDone) sb.append("\tDone       : " + tasksDone.size() + "\n");
		sb.append("\tSleep time : " + sleepTime + "\n");

		for (CmdRunner cmd : tasksToRun) {
			String status = "Not started";
			if (cmd.isDone()) status = "Done";
			else if (cmd.isExecuting()) status = "Executing";

			sb.append(String.format("\t[%12s]\t%s\n", status, cmd));
		}

		return sb.toString();
	}

}
