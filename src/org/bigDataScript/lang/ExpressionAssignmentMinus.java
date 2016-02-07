package org.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Expression
 * 
 * @author pcingola
 */
public class ExpressionAssignmentMinus extends ExpressionAssignmentBinary {

	public ExpressionAssignmentMinus(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected ExpressionBinary createSubExpression() {
		return new ExpressionMinus(this, null);
	}

	@Override
	protected String op() {
		return "-=";
	}

}
