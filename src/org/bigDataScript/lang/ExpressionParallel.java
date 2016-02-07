package org.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bigDataScript.compile.CompilerMessages;
import org.bigDataScript.run.BdsThread;
import org.bigDataScript.run.FunctionCallThread;

/**
 * A 'par' expression
 *
 * @author pcingola
 */
public class ExpressionParallel extends ExpressionTask {

	public ExpressionParallel(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Create a new BdsThread that runs in parallel
	 */
	BdsThread createParallel(BdsThread bdsThread) {
		BdsThread bdsNewThread = new BdsThread(statement, bdsThread);
		bdsNewThread.start();
		return bdsNewThread;
	}

	/**
	 * Create a new BdsThread that runs a function call in parallel
	 */
	FunctionCallThread createParallelFunctionCall(BdsThread bdsThread, Object arguments[]) {
		FunctionCallThread bdsNewThread = new FunctionCallThread(this, getFunctionCall(), bdsThread, arguments);
		bdsNewThread.start();
		return bdsNewThread;
	}

	/**
	 * Extract a functionCall (if any)
	 */
	FunctionCall getFunctionCall() {
		if (statement instanceof FunctionCall) return (FunctionCall) statement;

		// May be it's a statementExpr that contains a function call
		if (statement instanceof StatementExpr) {
			Expression expr = ((StatementExpr) statement).getExpression();
			if (expr instanceof FunctionCall) return (FunctionCall) expr;
		}

		return null;
	}

	@Override
	protected void parse(ParseTree tree) {
		int idx = 0;
		idx++; // 'task' keyword

		// Do we have any task options?
		if (tree.getChild(idx).getText().equals("(")) {
			int lastIdx = indexOf(tree, ")");

			options = new ExpressionTaskOptions(this, null);
			options.parse(tree, ++idx, lastIdx);
			idx = lastIdx + 1; // Skip last ')'
		}

		statement = (Statement) factory(tree, idx++); // Parse statement
	}

	/**
	 * Evaluate 'par' expression
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		// Execute options assignments
		if (options != null) {
			bdsThread.run(options);

			if (!bdsThread.isCheckpointRecover()) {
				boolean ok = popBool(bdsThread);
				if (bdsThread.isDebug()) log("task-options check " + ok);
				if (!ok) {
					// Options clause not satisfied. Do not execute 'parallel'
					bdsThread.push("");
					return;
				}
			}
		}

		// Create thread and execute statements
		BdsThread bdsNewThread = null;
		FunctionCall functionCall = getFunctionCall();
		if (functionCall != null) {
			// If the statement is a function call, we run it slightly differently:
			// We first compute the function's arguments (in the current thread), to
			// avoid race conditions. Then we create a thread and call the function

			// Evaluate function arguments in current thread
			functionCall.evalFunctionArguments(bdsThread);
			Object arguments[] = (Object[]) bdsThread.pop();

			if (!bdsThread.isCheckpointRecover()) {
				// Create and run new thread that runs the function call in parallel
				bdsNewThread = createParallelFunctionCall(bdsThread, arguments);
			} else {
				// When recovering from checkpoints, serialization mechanism takes care
				// of creating new threads, so we don't need to do it again here.
				bdsNewThread = bdsThread;
				getFunctionCall().runStep(bdsThread);
			}
		} else {
			if (!bdsThread.isCheckpointRecover()) {
				// Create and run new bds thread
				bdsNewThread = createParallel(bdsThread);
			} else {
				// When recovering from checkpoints, serialization mechanism takes care
				// of creating new threads, so we don't need to do it again here.
				bdsNewThread = bdsThread;
				bdsThread.run(statement);
			}
		}

		// Thread created. Return thread ID (so that we can 'wait' on it)
		if (!bdsThread.isCheckpointRecover()) bdsThread.push(bdsNewThread.getBdsThreadId());
	}

	@Override
	protected void sanityCheck(CompilerMessages compilerMessages) {
		if (options != null) options.sanityCheck(compilerMessages);
	}

	@Override
	public String toString() {
		return "par" //
				+ (options != null ? options : "") //
				+ " " //
				+ toStringStatement() //
				;
	}
}
