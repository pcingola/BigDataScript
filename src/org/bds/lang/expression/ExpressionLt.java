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
	protected boolean cmp(boolean a, boolean b) {
		return (!a) && b;
	}

	@Override
	protected boolean cmp(double a, double b) {
		return a < b;
	}

	@Override
	protected boolean cmp(long a, long b) {
		return a < b;
	}

	@Override
	protected boolean cmp(String a, String b) {
		return a.compareTo(b) < 0;
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
