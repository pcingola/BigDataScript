package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;

/**
 * A 'dep' expression
 *
 * @author pcingola
 */
public class ExpressionDep extends ExpressionTask {

	public ExpressionDep(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	void dispatchTask(BigDataScriptThread bdsThread, Task task) {
		task.setDependency(true); // Mark this as a 'dependency'
		bdsThread.add(task);
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void runStep(BigDataScriptThread bdsThread) {
		taskOptions.setEvalAll(true); // Force all task options to be evaluated
		super.runStep(bdsThread);
	}

	@Override
	public String toString() {
		return "dep" //
				+ (taskOptions != null ? taskOptions : "") //
				+ " " //
				+ toStringStatement() //
				;
	}

}
