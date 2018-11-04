package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * try / catch / finally statements
 *
 * @author pcingola
 */
public class Try extends StatementWithScope {

	private static final long serialVersionUID = -2501792284878137707L;

	Statement statement;

	public Try(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		int idx = 0;
		while (!isTerminal(tree, idx, "try"))
			idx++;
		statement = (Statement) factory(tree, ++idx);
	}

	@Override
	public String toAsm() {
		return (statement != null ? statement.toAsm() : "");
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("try {\n");
		if (statement != null) sb.append(statement);
		sb.append("} ");
		return sb.toString();
	}

}