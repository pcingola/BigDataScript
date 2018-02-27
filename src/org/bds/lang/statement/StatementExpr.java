package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.ExpressionWrapper;
import org.bds.run.BdsThread;

/**
 * A statement that is actually an expression.
 * E.g.:
 *        i++   # This statement is a line of code with only one expression ('i++')
 *
 * @author pcingola
 */
public class StatementExpr extends ExpressionWrapper {

	public StatementExpr(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public boolean isStopDebug() {
		return true;
	}

	@Override
	public void runStep(BdsThread bdsThread) {
		bdsThread.run(expression);

		// This is an expression in form of a statement, so
		// we remove the result from the stack (to avoid filling up
		// the stack)
		bdsThread.pop();
	}

}
