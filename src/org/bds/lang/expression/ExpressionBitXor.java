package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * A bitwise XOR
 *
 * @author pcingola
 */
public class ExpressionBitXor extends ExpressionBit {

	private static final long serialVersionUID = -2473522262501255653L;

	public ExpressionBitXor(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected String op() {
		return "^";
	}

	@Override
	public String toAsm() {
		return super.toAsm() + "xori\n";
	}

}
