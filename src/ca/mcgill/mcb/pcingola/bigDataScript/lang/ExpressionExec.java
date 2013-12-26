package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.lang.ProcessBuilder.Redirect;
import java.util.LinkedList;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.RunState;

/**
 * An 'exec' expression (to execute a command line in a local computer, return STDOUT)
 * 
 * @author pcingola
 */
public class ExpressionExec extends ExpressionSys {

	public static String SHELL_COMMAND[] = { "/bin/bash", "-c" };

	public ExpressionExec(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public Object eval(BigDataScriptThread csThread) {
		// Run like a statement and return task ID
		run(csThread);
		return execId;
	}

	@Override
	protected RunState runStep(BigDataScriptThread csThread) {
		if (csThread.isCheckpointRecover()) return RunState.CHECKPOINT_RECOVER;

		// Get an ID
		execId = execId("exec", csThread);

		// EXEC expressions are always executed locally AND immediately
		LinkedList<String> args = new LinkedList<String>();
		for (String arg : SHELL_COMMAND)
			args.add(arg);
		args.add(commands);

		ProcessBuilder pb = new ProcessBuilder(args);
		pb.redirectError(stderr);
		pb.redirectOutput(stdout);

		//		// Error running the program? 
		//		if (!task.isDoneOk()) {
		//			// Execution failed! Save checkpoint and exit
		//			csThread.checkpoint(null);
		//			csThread.setExitValue(task.getExitValue()); // Set return value and exit
		//			return RunState.EXIT;
		//		}

		return RunState.OK;
	}
}

class RedirectSdt extends Redirect {

	@Override
	public Type type() {
		// TODO Auto-generated method stub
		return null;
	}

}
