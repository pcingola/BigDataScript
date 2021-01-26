package org.bds.executioner;

import java.util.ArrayList;

import org.bds.Config;
import org.bds.cluster.host.TaskResourcesCluster;
import org.bds.osCmd.Cmd;
import org.bds.osCmd.CmdCluster;
import org.bds.task.Task;
import org.bds.util.Gpr;

/**
 * Execute tasks in a PBS cluster.
 *
 * All commands are run using 'qsub' (or equivalent) commands
 *
 * @author pcingola
 */
public class ExecutionerClusterGeneric extends ExecutionerCluster {

	public ExecutionerClusterGeneric(Config config) {
		super(config);

		// Define commands
		clusterRunCommand = getCommandLine(Config.CLUSTER_GENERIC_RUN);
		clusterKillCommand = getCommandLine(Config.CLUSTER_GENERIC_KILL);
		clusterStatCommand = getCommandLine(Config.CLUSTER_GENERIC_STAT);
		clusterPostMortemInfoCommand = getCommandLine(Config.CLUSTER_GENERIC_POSTMORTEMINFO);

		// Additional arguments are ignored in this cluster type
		clusterRunAdditionalArgs = new String[0];
		clusterKillAdditionalArgs = new String[0];
		clusterStatAdditionalArgs = new String[0];
		clusterPostMortemAdditionalArgs = new String[0];
	}

	@Override
	public Cmd createRunCmd(Task task) {
		task.createProgramFile(); // We must create a program file

		debug("Running task " + task.getId());

		// Create command line
		ArrayList<String> args = new ArrayList<>();

		// Append command line arguments
		for (String arg : getCommandRun())
			args.add(arg);

		// Add resources request
		TaskResourcesCluster res = (TaskResourcesCluster) task.getResources();
		args.add("" + calcTimeOut(res));
		args.add("" + res.getCpus());
		args.add("" + res.getMem());
		args.add(res.getQueue() != null ? res.getQueue() : "");
		args.add(clusterStdFile(task.getStdoutFile()));
		args.add(clusterStdFile(task.getStderrFile()));

		// Create command to run
		for (String arg : createBdsExecCmdArgsList(task))
			args.add(arg);

		// Convert to string[]
		String argv[] = args.toArray(Cmd.ARGS_ARRAY_TYPE);
		if (debug) {
			log("Custom script command line arguments:");
			for (int i = 0; i < argv.length; i++) {
				System.err.println("\t\t" + i + "\t" + argv[i]);
			}
		}

		// Create command
		CmdCluster cmd = new CmdCluster(task.getId(), argv);
		cmd.setReadPid(true); // We execute using a custom made script that is required to output jobID in the first line
		debug("Running task " + task.getId() + ", command:\n\t" + cmd);
		return cmd;
	}

	/**
	 * Parse command line arguments
	 */
	protected String[] getCommandLine(String configParam) {
		String cmd[] = config.getStringArray(configParam, true);

		// Some basic path conversions for the command
		String cmdPath = cmd[0];
		if (cmdPath.startsWith("~/")) cmdPath = Gpr.HOME + "/" + cmdPath.substring(2); // Relative to 'home' dir? (starts with '~/')
		else if (cmdPath.startsWith("~")) cmdPath = Gpr.HOME + "/" + cmdPath.substring(1); // Relative to 'home' dir? (starts with '~')
		else if (!cmdPath.startsWith("/")) cmdPath = config.getConfigDirName() + "/" + cmdPath; // Not an absolute path?
		cmd[0] = cmdPath;

		return cmd;
	}

}
