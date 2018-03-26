package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * A comparison expression (!=)
 *
 * @author pcingola
 */
public class ExpressionNe extends ExpressionCompare {

	public ExpressionNe(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected boolean cmp(boolean a, boolean b) {
		return a != b;
	}

	@Override
	protected boolean cmp(double a, double b) {
		return a != b;
	}

	@Override
	protected boolean cmp(long a, long b) {
		return a != b;
	}

	@Override
	protected boolean cmp(String a, String b) {
		return !a.equals(b);
	}

	@Override
	protected String op() {
		return "!=";
	}

}
