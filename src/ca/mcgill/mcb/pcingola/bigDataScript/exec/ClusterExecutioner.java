package ca.mcgill.mcb.pcingola.bigDataScript.exec;

import java.util.ArrayList;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.Cluster;
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

	public static String CLUSTER_EXEC_COMMAND[] = { "qsub" };
	public static String CLUSTER_KILL_COMMAND[] = { "qdel" };

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

		long timeout = task.getResources().getTimeout() > 0 ? task.getResources().getTimeout() : 0;
		args.add(timeout + "");
		args.add(task.getStdoutFile());
		args.add(task.getStderrFile());
		args.add(task.getExitCodeFile());
		args.add(task.getProgramFileName());

		String cmdStr = "";
		for (String arg : args)
			cmdStr += arg + " ";

		// Run command
		if (debug) Timer.showStdErr("Running command: " + cmdStr);
		CmdRunner cmd = new CmdRunner(task.getId(), args.toArray(CmdRunner.ARGS_ARRAY_TYPE));
		cmd.setTask(task);
		cmd.setExecutioner(this);
		cmd.setReadPid(true); // We execute using "bds exec" which prints PID number before executing the sub-process
		cmdById.put(task.getId(), cmd);
		return cmd;
	}

}
