package ca.mcgill.mcb.pcingola.bigDataScript.exec;

import java.util.ArrayList;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.Cluster;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostResources;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.CmdRunner;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * Execute tasks in a cluster.
 * 
 * All commands are run using 'qsub' (or equivalent) commands
 * 
 * @author pcingola
 */
public class ClusterExecutioner extends LocalExecutioner {

	public static String FAKEL_CLUSTER = "";
	// public static String FAKEL_CLUSTER = Gpr.HOME + "/workspace/BigDataScript/fakeCluster/";

	public static String CLUSTER_EXEC_COMMAND[] = { FAKEL_CLUSTER + "qsub" };
	public static String CLUSTER_KILL_COMMAND[] = { FAKEL_CLUSTER + "qdel" };
	public static String CLUSTER_BDS_COMMAND = "bds exec 0 - - ";

	public ClusterExecutioner(Cluster cluster) {
		super(null);
	}

	@Override
	CmdRunner createCmdRunner(Task task) {
		task.createProgramFile(); // We must create a program file

		// Create command line
		ArrayList<String> args = new ArrayList<String>();
		for (String arg : CLUSTER_EXEC_COMMAND)
			args.add(arg);

		// Add resources request
		HostResources res = task.getResources();
		StringBuilder resSb = new StringBuilder();
		if (res.getCpus() > 0) resSb.append((resSb.length() > 0 ? ":" : "") + "ppn=" + res.getCpus());
		if (res.getMem() > 0) resSb.append((resSb.length() > 0 ? ":" : "") + "mem=" + res.getMem());
		if (res.getTimeout() > 0) resSb.append((resSb.length() > 0 ? ":" : "") + "walltime=" + res.getTimeout());

		// Any resources requested? Add command line
		if (resSb.length() > 0) {
			args.add("-l");
			args.add(resSb.toString());
		}

		// Stdout 
		args.add("-o");
		args.add(task.getStdoutFile());

		// Stderr 
		args.add("-e");
		args.add(task.getStderrFile());

		// Show command string
		String cmdStr = "";
		for (String arg : args)
			cmdStr += arg + " ";

		// Create command to run (it feed to qsub via stdin)
		StringBuilder cmdStdin = new StringBuilder();
		cmdStdin.append(CLUSTER_BDS_COMMAND);
		cmdStdin.append(task.getExitCodeFile() + " ");
		cmdStdin.append(task.getProgramFileName());

		// Run command
		if (debug) Timer.showStdErr("Running command: echo \"" + cmdStdin + "\" | " + cmdStr);

		CmdRunner cmd = new CmdRunner(task.getId(), args.toArray(CmdRunner.ARGS_ARRAY_TYPE));
		cmd.setTask(task);
		cmd.setExecutioner(this);
		cmd.setStdin(cmdStdin.toString());
		cmd.setReadPid(true); // We execute using "qsub" which prints a jobID to stdout
		cmdById.put(task.getId(), cmd);
		return cmd;
	}
}
