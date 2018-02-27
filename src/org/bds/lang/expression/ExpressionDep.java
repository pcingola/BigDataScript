package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.run.BdsThread;
import org.bds.task.Task;

/**
 * A 'dep' expression
 *
 * @author pcingola
 */
public class ExpressionDep extends ExpressionTask {

	public ExpressionDep(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	void dispatchTask(BdsThread bdsThread, Task task) {
		task.setDependency(true); // Mark this as a 'dependency'
		bdsThread.add(task);
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		options.setEvalAll(true); // Force all task options to be evaluated
		super.runStep(bdsThread);
	}

	@Override
	public String toString() {
		return "dep" //
				+ (options != null ? options : "") //
				+ " " //
				+ toStringStatement() //
		;
	}

}
