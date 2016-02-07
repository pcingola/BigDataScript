package org.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bigDataScript.run.BdsThread;

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
		if ((returnType == Type.INT) || (returnType == Type.REAL)) bdsThread.run(expr);
		else throw new RuntimeException("Cannot cast to 'int' or 'real'. This should never happen!");
	}
}
