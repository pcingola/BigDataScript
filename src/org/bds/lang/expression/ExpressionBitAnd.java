package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * A bitwise AND
 *
 * @author pcingola
 */
public class ExpressionBitAnd extends ExpressionBit {

	private static final long serialVersionUID = -3739986052298094584L;

	public ExpressionBitAnd(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected String op() {
		return "&";
	}

	@Override
	public String toAsm() {
		return super.toAsm() + "andi\n";
	}
}
