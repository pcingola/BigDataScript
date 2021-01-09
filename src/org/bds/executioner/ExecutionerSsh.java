package org.bds.executioner;

import org.bds.Config;
import org.bds.cluster.ClusterSsh;
import org.bds.cluster.host.HostSsh;
import org.bds.osCmd.Cmd;
import org.bds.osCmd.CmdSsh;
import org.bds.task.Task;

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
public class ExecutionerSsh extends ExecutionerFileSystem {

	protected ExecutionerSsh(Config config) {
		super(config);
		createCluster();
	}

	protected void createCluster() {
		// Create a cluster
		system = new ClusterSsh();

		// TODO: Add global variable, nodes (list tab/colon separated list)

		// Add nodes from config file
		String nodes = config.getString(Config.CLUSTER_SSH_NODES, "");
		if (config.isDebug()) System.err.println("Ssh nodes string (config): '" + nodes + "'");
		String sshNodes[] = nodes.split(",");
		for (String sshNode : sshNodes) {
			if (config.isDebug()) System.err.println("\tAdding ssh node : '" + sshNode + "'");
			system.add(new HostSsh(system, sshNode.trim()));
		}
	}

	@Override
	public Cmd createRunCmd(Task task) {
		task.createProgramFile(); // We must create a program file

		// Create command line
		String[] args = createBdsExecCmdArgs(task);

		String cmdStr = "";
		for (String arg : args)
			cmdStr += arg + " ";

		// Run command
		debug("Running command: " + cmdStr);
		CmdSsh cmd = new CmdSsh(task.getId(), args);
		return cmd;
	}

	@Override
	protected void follow(Task task) {
		if (taskLogger != null) taskLogger.add(task, this); // Log PID (if any)
		if (monitorTask != null) monitorTask.remove(task);
	}

	@Override
	public synchronized void kill() {
		((ClusterSsh) system).stopHostInfoUpdaters();
		super.kill();
	}

	@Override
	public String[] osKillCommand(Task task) {
		return Cmd.ARGS_ARRAY_TYPE;
	}

	@Override
	public void run() {
		((ClusterSsh) system).startHostInfoUpdaters();
		super.run();
		((ClusterSsh) system).stopHostInfoUpdaters();
	}

}
