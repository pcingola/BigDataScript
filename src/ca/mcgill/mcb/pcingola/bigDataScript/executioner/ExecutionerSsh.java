package ca.mcgill.mcb.pcingola.bigDataScript.executioner;

import java.util.ArrayList;

import ca.mcgill.mcb.pcingola.bigDataScript.Config;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.Cluster;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.Cmd;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.CmdSsh;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * Execute tasks in a remote computer, using ssh
 *
 * Note: Even though the command is executed via ssh, it
 *       is assumed that the underlying file system is visible
 *       across all codes.
 *       Otherwise it would bee to time consuming to synchronize
 *       tasks and files.
 *       This is a 'common' set up, typically in a university network
 *       where the user has a shared home directory across all
 *       computer, but there is no "cluster" software to coordinate
 *       jobs
 *
 * @author pcingola
 */
public class ExecutionerSsh extends Executioner {

	public static final String CONFIG_SSH_NODES = "ssh.nodes";
	public static String SSH_EXEC_COMMAND[] = { "/bin/bash", "-e" };

	protected ExecutionerSsh(Config config) {
		super(config);
		createCluster();
	}

	protected void createCluster() {
		// Create a cluster
		cluster = new Cluster();

		// Add nodes from config file
		String nodes = config.getString(CONFIG_SSH_NODES, "");
		if (config.isDebug()) System.err.println("Ssh nodes string (config): '" + nodes + "'");
		String sshNodes[] = nodes.split(",");
		for (String sshNode : sshNodes) {
			if (config.isDebug()) System.err.println("\tAdding ssh node : '" + sshNode + "'");
			cluster.add(new Host(cluster, sshNode.trim()));
		}
	}

	@Override
	protected Cmd createRunCmd(Task task) {
		task.createProgramFile(); // We must create a program file

		// Create command line
		ArrayList<String> args = new ArrayList<String>();
		for (String arg : ExecutionerLocal.LOCAL_EXEC_COMMAND)
			args.add(arg);
		long timeout = task.getResources().getTimeout() > 0 ? task.getResources().getTimeout() : 0;

		// Add command line parameters for "bds exec"
		args.add(timeout + ""); // Enforce timeout
		args.add(task.getStdoutFile()); // Redirect STDOUT to this file
		args.add(task.getStderrFile()); // Redirect STDERR to this file
		args.add("-"); // No need to create exitCode file in local execution
		args.add(task.getProgramFileName()); // Program to execute

		String cmdStr = "";
		for (String arg : args)
			cmdStr += arg + " ";

		// Run command
		if (debug) Timer.showStdErr("Running command: " + cmdStr);
		CmdSsh cmd = new CmdSsh(task.getId(), args.toArray(Cmd.ARGS_ARRAY_TYPE));
		return cmd;
	}

	@Override
	protected void follow(Task task) {
		if (taskLogger != null) taskLogger.add(task, this); // Log PID (if any)

		// No need to 'tail', Ssh class show output to StdOut directly
		//		tail.add(task.getStdoutFile(), null, false);
		//		tail.add(task.getStderrFile(), null, true);

		if (monitorTask != null) monitorTask.remove(task);
	}

	@Override
	public synchronized void kill() {
		cluster.stopHostInfoUpdaters();
		super.kill();
	}

	@Override
	public String[] osKillCommand(Task task) {
		return Cmd.ARGS_ARRAY_TYPE;
	}

	@Override
	public void run() {
		cluster.startHostInfoUpdaters();
		super.run();
		cluster.stopHostInfoUpdaters();
	}

}
