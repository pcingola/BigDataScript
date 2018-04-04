package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * Expression
 *
 * @author pcingola
 */
public class ExpressionAssignmentBitAnd extends ExpressionAssignmentBinary {

	private static final long serialVersionUID = 6485322353980892360L;

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

	@Override
	protected String toAsmOp() {
		return "andi\n";
	}

}
