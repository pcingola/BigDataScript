package org.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Expression
 * 
 * @author pcingola
 */
public class ExpressionAssignmentBitAnd extends ExpressionAssignmentBinary {

	public ExpressionAssignmentBitAnd(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected ExpressionBinary createSubExpression() {
		return new ExpressionBitAnd(this, null);
	}

	@Override
	protected String op() {
		return "&=";
	}

}
