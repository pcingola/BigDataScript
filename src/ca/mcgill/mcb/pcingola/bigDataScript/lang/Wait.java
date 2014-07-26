package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.RunState;

/**
 * A "wait" statement
 *
 * @author pcingola
 */
public class Wait extends Statement {

	Expression taskId;

	public Wait(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		// child[0] = 'wait'
		if (tree.getChildCount() > 1) taskId = (Expression) factory(tree, 1);
	}

	/**
	 * Run the program
	 */
	@Override
	protected RunState runStep(BigDataScriptThread bdsThread) {
		if (bdsThread.getRunState() == RunState.WAIT_RECOVER) return runStepWaitRecover(bdsThread);
		return runStepOk(bdsThread);
	}

	/**
	 * Run in 'OK' state
	 */
	@SuppressWarnings("rawtypes")
	protected RunState runStepOk(BigDataScriptThread bdsThread) {
		boolean ok = false;
		String type = "Task";

		// No arguments? Wait for all tasks
		if (taskId == null) {
			ok = bdsThread.waitAll();
		} else {
			// Wait for a specific task or list of tasks
			Object val = taskId.eval(bdsThread);

			// Are we waiting for a single task/thread or a list?
			if (val instanceof List) ok = bdsThread.wait((List) val);
			else {
				if (bdsThread.getThread(val.toString()) != null) type = "Thread";
				ok = bdsThread.wait(val.toString());
			}
		}

		// Any task/thread failed?
		if (!ok) {
			// Create a checkpoint
			bdsThread.fatalError(this, type + "/s failed.");
			return RunState.FATAL_ERROR;
		}

		return RunState.OK;
	}

	/**
	 * Run in 'WAIT_RECOVER' state.
	 * This happens when recovering from a checkpoint.
	 */
	protected RunState runStepWaitRecover(BigDataScriptThread bdsThread) {
		return runStepOk(bdsThread);
	}
}
