package ca.mcgill.mcb.pcingola.bigDataScript.exec;

import java.util.ArrayList;

import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.CmdRunner;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * Execute tasks in local computer. 
 * Executed the command immediately without controlling host resources.
 *  
 * @author pcingola
 */
public class LocalExecutioner extends Executioner {

	public static String LOCAL_EXEC_COMMAND[] = { "bds", "exec" };
	private static final String[] ARGS_ARRAY_TYPE = new String[0];

	public LocalExecutioner() {
		super();
	}

	@Override
	public void add(Task task) {
		super.add(task);
		run(task); // Run task right away
	}

	@Override
	protected boolean killTask(Task task) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void run() {
		running = true;
	}

	@Override
	protected boolean runTask(Task task) {
		task.createProgramFile(); // We must create a program file

		ArrayList<String> args = new ArrayList<String>();
		for (String arg : LOCAL_EXEC_COMMAND)
			args.add(arg);

		// Add 'bds exec' parameters
		args.add(task.getResources().getTimeout() + "");
		args.add(task.getStdoutFile());
		args.add(task.getStderrFile());
		args.add(task.getExitCodeFile());
		args.add(task.getProgramFileName());

		String cmdStr = "";
		for (String arg : args)
			cmdStr += arg + " ";
		Gpr.debug("CMD: " + cmdStr);

		CmdRunner cmd = new CmdRunner(task.getId(), args.toArray(ARGS_ARRAY_TYPE));
		cmd.setCmdStats(task);
		cmd.setExecutioner(this);
		cmd.start();

		return true;
	}
}
