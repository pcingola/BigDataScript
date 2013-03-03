package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

/**
 * A comparison expression (==)
 * 
 * @author pcingola
 */
public class ExpressionEq extends ExpressionCompare {

	public ExpressionEq(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	boolean cmp(double a, double b) {
		return a == b;
	}

	@Override
	boolean cmp(long a, long b) {
		return a == b;
	}

	@Override
	boolean cmp(String a, String b) {
		return a.equals(b);
	}

}
