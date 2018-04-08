package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * A comparison expression (!=)
 *
 * @author pcingola
 */
public class ExpressionNe extends ExpressionCompare {

	private static final long serialVersionUID = 680361809643102250L;

	public ExpressionNe(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected String op() {
		return "!=";
	}

	@Override
	public String toAsmOp() {
		return "ne";
	}

}
