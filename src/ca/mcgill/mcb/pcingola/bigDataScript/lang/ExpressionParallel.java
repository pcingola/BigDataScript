package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

/**
 * A 'par' expression
 *
 * @author pcingola
 */
public class ExpressionParallel extends ExpressionTask {

	public ExpressionParallel(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Create a new BdsThread that runs in parallel
	 */
	BigDataScriptThread createParallel(BigDataScriptThread bdsThread) {
		BigDataScriptThread bdsNewThread = new BigDataScriptThread(statement, bdsThread);
		bdsNewThread.start();
		return bdsNewThread;
	}

	/**
	 * Evaluate 'par' expression
	 */
	@Override
	public Object eval(BigDataScriptThread bdsThread) {
		// Execute options assignments
		if (taskOptions != null) {
			boolean ok = (Boolean) taskOptions.eval(bdsThread);
			if (bdsThread.isDebug()) log("task-options check " + ok);
			if (!ok) return execId; // Task options clause not satisfied. Do not execute task
		}

		// Create new bds thread
		BigDataScriptThread bdsNewThread = createParallel(bdsThread);
		return bdsNewThread.getBigDataScriptThreadId(); // Return thread ID (so that we can 'wait' on it)
	}

	@Override
	protected void parse(ParseTree tree) {
		int idx = 0;
		idx++; // 'task' keyword

		// Do we have any task options?
		if (tree.getChild(idx).getText().equals("(")) {
			int lastIdx = indexOf(tree, ")");

			taskOptions = new TaskOptions(this, null);
			taskOptions.parse(tree, ++idx, lastIdx);
			idx = lastIdx + 1; // Skip last ')'
		}

		statement = (Statement) factory(tree, idx++); // Parse statement
	}

	@Override
	protected void sanityCheck(CompilerMessages compilerMessages) {
		if (taskOptions != null) taskOptions.sanityCheck(compilerMessages);
	}

	@Override
	public String toString() {
		return "par " + (taskOptions != null ? taskOptions : "") + " " + statement;
	}
}
