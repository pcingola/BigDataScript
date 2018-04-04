package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * Expression '+='
 *
 * @author pcingola
 */
public class ExpressionAssignmentPlus extends ExpressionAssignmentBinary {

	private static final long serialVersionUID = -193493178374997029L;

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

	@Override
	protected String toAsmOp() {
		if (returnType.isInt()) return "addi\n";
		if (returnType.isReal()) return "addr\n";
		if (returnType.isString()) return "adds\n";
		throw new RuntimeException("Unknown operation '" + op() + "' for type " + returnType);
	}

}
