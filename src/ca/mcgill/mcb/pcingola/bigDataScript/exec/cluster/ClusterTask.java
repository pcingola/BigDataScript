package ca.mcgill.mcb.pcingola.bigDataScript.exec.cluster;

import ca.mcgill.mcb.pcingola.bigDataScript.exec.Executioners.ExecutionerType;
import ca.mcgill.mcb.pcingola.bigDataScript.exec.local.ShellTask;

/**
 * A shell script task to be executed in a cluster
 * 
 * @author pcingola
 */
public class ClusterTask extends ShellTask {

	public static final String QUEUE_COMMAND = "qsub";
	public static final String QUEUE_COMMAND_OPTIONS[] = { "-I", "-N", "TASK_ID", "-x" };
	public static final int QUEUE_COMMAND_OPTIONS_TASK_ID_NUM = 2;

	//	public static final String QUEUE_COMMAND_OPTIONS[] = { "-I", "-N", "TASK_ID", "-o", "stdout", "-e", "stderr", "-x" };
	//	public static final int QUEUE_COMMAND_OPTIONS_STDOUT = 4;
	//	public static final int QUEUE_COMMAND_OPTIONS_STDERR = 6;

	public ClusterTask(String id, String programFileName, String programTxt) {
		super(id, programFileName, programTxt);

		// Invoke script using "qsub" command (instead of "/bin/sh")
		invokeCmd = QUEUE_COMMAND;
		invokeCmdOptions = QUEUE_COMMAND_OPTIONS;
	}

	@Override
	public ExecutionerType getExecutionerType() {
		return ExecutionerType.CLUSTER;
	}

	@Override
	public String[] getInvokeCmdOptions() {
		String opts[] = QUEUE_COMMAND_OPTIONS.clone();
		opts[QUEUE_COMMAND_OPTIONS_TASK_ID_NUM] = id;

		return opts;
	}

}
