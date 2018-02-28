package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.run.BdsThread;

/**
 * A arithmetic 'plus'
 *
 * @author pcingola
 */
public class ExpressionUnaryPlus extends ExpressionUnaryMinus {

	public ExpressionUnaryPlus(BdsNode parent, ParseTree tree) {
		super(parent, tree);
		op = "+";
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		if (returnType.isInt() || returnType.isReal()) bdsThread.run(expr);
		else throw new RuntimeException("Cannot cast to 'int' or 'real'. This should never happen!");
	}
}
