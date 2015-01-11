package ca.mcgill.mcb.pcingola.bigDataScript.executioner;

import java.util.ArrayList;

import ca.mcgill.mcb.pcingola.bigDataScript.Config;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostResources;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.Cmd;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.CmdCluster;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

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
	protected Cmd createRunCmd(Task task) {
		task.createProgramFile(); // We must create a program file

		if (debug) Timer.showStdErr("Executioner: " + this.getClass().getSimpleName() + ", task " + task.getId());

		// Create command line
		ArrayList<String> args = new ArrayList<String>();

		// Append command line arguments
		for (String arg : getCommandRun())
			args.add(arg);

		// Add resources request
		HostResources res = task.getResources();
		args.add("" + res.getTimeout());
		args.add("" + res.getCpus());
		args.add("" + res.getMem());
		args.add(task.getQueue() != null ? task.getQueue() : "");
		args.add(clusterStdFile(task.getStdoutFile()));
		args.add(clusterStdFile(task.getStderrFile()));

		// Create command to run (it feeds parameters to qsub via stdin)
		String bdsExecCmd = bdsCommand(task);
		for (String arg : bdsExecCmd.split("\\s+"))
			args.add(arg);

		// Convert to string[]
		String argv[] = args.toArray(Cmd.ARGS_ARRAY_TYPE);
		if (debug) {
			Timer.showStdErr("Executioner: " + this.getClass().getSimpleName() + ", custom script command line arguments:");
			for (int i = 0; i < argv.length; i++) {
				System.err.println("\t\t" + i + "\t" + argv[i]);
			}
		}

		// Create command
		CmdCluster cmd = new CmdCluster(task.getId(), argv);
		cmd.setReadPid(true); // We execute using a custom made script that is required to output jobID in the first line
		if (debug) Timer.showStdErr("Command (CmdCluster): " + cmd);
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
