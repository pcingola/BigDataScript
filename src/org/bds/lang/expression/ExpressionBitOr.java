package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * A bitwise OR
 *
 * @author pcingola
 */
public class ExpressionBitOr extends ExpressionBit {

	private static final long serialVersionUID = -4468043225504579475L;

	public ExpressionBitOr(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected String op() {
		return "|";
	}

	@Override
	public String toAsm() {
		return super.toAsm() + "ori\n";
	}

}
