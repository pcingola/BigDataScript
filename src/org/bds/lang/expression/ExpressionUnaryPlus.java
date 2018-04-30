package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * A arithmetic 'plus'
 *
 * @author pcingola
 */
public class ExpressionUnaryPlus extends ExpressionUnaryMinus {

	private static final long serialVersionUID = 4577333129590654063L;

	public ExpressionUnaryPlus(BdsNode parent, ParseTree tree) {
		super(parent, tree);
		op = "+";
	}

	@Override
	public String toAsm() {
		return expr.toAsm();
	}

}
