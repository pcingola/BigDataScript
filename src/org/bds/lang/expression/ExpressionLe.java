package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * A comparison expression (<)
 *
 * @author pcingola
 */
public class ExpressionLe extends ExpressionCompare {

	private static final long serialVersionUID = -9094126571337959268L;

	public ExpressionLe(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected String op() {
		return "<=";
	}

	@Override
	public String toAsmOp() {
		return "le";
	}

}
