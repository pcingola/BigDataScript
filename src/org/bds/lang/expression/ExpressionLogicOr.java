package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * Boolean OR
 *
 * @author pcingola
 */
public class ExpressionLogicOr extends ExpressionLogic {

	private static final long serialVersionUID = -9149820473234256403L;

	public ExpressionLogicOr(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected String op() {
		return "||";
	}

	@Override
	public String toAsm() {
		return super.toAsm() + "orb\n";
	}

}
