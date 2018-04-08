package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * A comparison expression (<)
 *
 * @author pcingola
 */
public class ExpressionGe extends ExpressionCompare {

	private static final long serialVersionUID = 2734424457270910249L;

	public ExpressionGe(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected String op() {
		return ">=";
	}

	@Override
	public String toAsmOp() {
		return "ge";
	}

}
