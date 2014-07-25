package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

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
		Gpr.debug("DEP: " + task);

		for (String in : task.getInputFiles())
			Gpr.debug("IN: " + in);

		for (String out : task.getOutputFiles())
			Gpr.debug("OUT: " + out);
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public Object eval(BigDataScriptThread bdsThread) {
		taskOptions.setEvalAll(true); // Force all task options to be evaluated
		return super.eval(bdsThread);
	}

	@Override
	public String toString() {
		return "dep " + taskOptions + " " + statement;
	}

}
