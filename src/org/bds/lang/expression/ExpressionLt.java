package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * A comparison expression (<)
 *
 * @author pcingola
 */
public class ExpressionLt extends ExpressionCompare {

	private static final long serialVersionUID = -4628853302241456425L;

	public ExpressionLt(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected String op() {
		return "<";
	}

	@Override
	public String toAsmOp() {
		return "lt";
	}

}
