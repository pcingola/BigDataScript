package org.bds.lang.statement;

import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.value.Value;
import org.bds.run.BdsThread;
import org.bds.run.RunState;

/**
 * A "wait" statement
 *
 * @author pcingola
 */
public class Wait extends Statement {

	Expression taskId;

	public Wait(BdsNode parent, ParseTree tree) {
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
	@SuppressWarnings("rawtypes")
	public void runStep(BdsThread bdsThread) {

		boolean ok = false;
		String type = "Task";

		// No arguments? Wait for all tasks
		if (taskId == null) {
			ok = bdsThread.waitAll();
		} else {
			// Wait for a specific task or list of tasks
			bdsThread.run(taskId);
			Value val = bdsThread.pop();

			// Are we waiting for a single task/thread or a list?
			if (val.getType().isList()) ok = bdsThread.wait((List) val.get());
			else {
				String valStr = val.get().toString();
				if (bdsThread.getThread(valStr) != null) type = "Thread";
				ok = bdsThread.wait(valStr);
			}
		}

		// Any task/thread failed?
		if (!ok) {
			// Create a checkpoint
			bdsThread.fatalError(this, type + "/s failed.");
		}

		// Were we recovering from a 'wait' within a checkpoint?
		if (bdsThread.getRunState() == RunState.WAIT_RECOVER) {
			bdsThread.setRunState(RunState.OK);
		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName().toLowerCase() + (taskId != null ? taskId : "") + "\n";
	}
}
