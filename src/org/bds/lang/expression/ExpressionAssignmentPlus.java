package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * Expression '+='
 *
 * @author pcingola
 */
public class ExpressionAssignmentPlus extends ExpressionAssignmentBinary {

	public ExpressionAssignmentPlus(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected ExpressionBinary createSubExpression() {
		return new ExpressionPlus(this, null);
	}

	@Override
	protected String op() {
		return "+=";
	}
}
