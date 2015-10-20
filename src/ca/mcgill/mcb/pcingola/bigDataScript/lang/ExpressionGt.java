package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

/**
 * A comparison expression (<)
 *
 * @author pcingola
 */
public class ExpressionGt extends ExpressionCompare {

	public ExpressionGt(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected boolean cmp(boolean a, boolean b) {
		return a && (!b); // a > b
	}

	@Override
	protected boolean cmp(double a, double b) {
		return a > b;
	}

	@Override
	protected boolean cmp(long a, long b) {
		return a > b;
	}

	@Override
	protected boolean cmp(String a, String b) {
		return a.compareTo(b) > 0;
	}

	@Override
	protected String op() {
		return ">";
	}

}
