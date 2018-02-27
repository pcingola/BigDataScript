package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.expression.ExpressionList;
import org.bds.run.BdsThread;

/**
 * for( ForInit ; ForCondition ; ForEnd ) Statements
 *
 * @author pcingola
 */
public class ForEnd extends ExpressionList {

	public ForEnd(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public boolean isStopDebug() {
		return true;
	}

	@Override
	public void runStep(BdsThread bdsThread) {
		for (Expression expr : expressions) {
			bdsThread.run(expr);
			bdsThread.pop(); // Remove from stack, nobody is reading the results
		}
	}

}
