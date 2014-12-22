package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.FunctionCallThread;

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
	 * Create a new BdsThread that runs a function call in parallel
	 */
	FunctionCallThread createParallelFunctionCall(BigDataScriptThread bdsThread, Object arguments[]) {
		FunctionCallThread bdsNewThread = new FunctionCallThread(this, (FunctionCall) statement, bdsThread, arguments);
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
			if (!ok) return ""; // Options clause not satisfied. Do not execute 'parallel'
		}

		// Create thread and execute statements
		BigDataScriptThread bdsNewThread = null;
		if (statement instanceof FunctionCall) {
			// If the statement is a function call, we run it slightly differently:
			// We first compute the function's arguments (in the current thread), to
			// avoid race conditions. Then we create a thread and call the function

			// Evaluate function arguments in current thread
			FunctionCall functionCall = (FunctionCall) statement;
			Object arguments[] = functionCall.evalFunctionArguments(bdsThread);

			// Create and run new thread that runs the function call in parallel
			bdsNewThread = createParallelFunctionCall(bdsThread, arguments);
		} else {
			// Create and run new bds thread
			bdsNewThread = createParallel(bdsThread);
		}

		return bdsNewThread.getBdsThreadId(); // Return thread ID (so that we can 'wait' on it)
	}

	@Override
	protected void parse(ParseTree tree) {
		int idx = 0;
		idx++; // 'task' keyword

		// Do we have any task options?
		if (tree.getChild(idx).getText().equals("(")) {
			int lastIdx = indexOf(tree, ")");

			taskOptions = new ExpressionTaskOptions(this, null);
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
