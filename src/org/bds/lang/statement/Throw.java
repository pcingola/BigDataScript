package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;

/**
 * Throw statement
 *
 * @author pcingola
 */
public class Throw extends StatementWithScope {

	private static final long serialVersionUID = -861450592187243401L;

	Expression expr;

	public Throw(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		int idx = 0;
		if (isTerminal(tree, idx, "throw")) idx++;
		expr = (Expression) factory(tree, idx);
	}

	@Override
	public String toAsm() {
		StringBuilder sb = new StringBuilder();
		return sb.toString();
	}

	@Override
	public String toString() {
		return "throw " + (expr != null ? expr.toString() : "");
	}

}
