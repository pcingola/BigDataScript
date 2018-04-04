package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * Expression '/='
 *
 * @author pcingola
 */
public class ExpressionAssignmentDiv extends ExpressionAssignmentBinary {

	private static final long serialVersionUID = -2367714044119782574L;

	public ExpressionAssignmentDiv(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected ExpressionBinary createSubExpression() {
		return new ExpressionDivide(this, null);
	}

	@Override
	protected String op() {
		return "/=";
	}
}
