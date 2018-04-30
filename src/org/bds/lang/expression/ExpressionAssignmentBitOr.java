package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * Expression
 *
 * @author pcingola
 */
public class ExpressionAssignmentBitOr extends ExpressionAssignmentBinary {

	/**
	 *
	 */
	private static final long serialVersionUID = -2969794879422342835L;

	public ExpressionAssignmentBitOr(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected ExpressionBinary createSubExpression() {
		return new ExpressionBitOr(this, null);
	}

	@Override
	protected String op() {
		return "|=";
	}
}
