package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * Expression
 *
 * @author pcingola
 */
public class ExpressionAssignmentMinus extends ExpressionAssignmentBinary {

	private static final long serialVersionUID = 7729438200021904075L;

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
