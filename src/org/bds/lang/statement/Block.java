package org.bds.lang.statement;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * A block of statements
 *
 * @author pcingola
 */
public class Block extends StatementWithScope {

	private static final long serialVersionUID = -8981215874906264612L;
	protected Statement statements[];

	public Block(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	public Statement[] getStatements() {
		return statements;
	}

	@Override
	protected void parse(ParseTree tree) {
		parse(tree, 0);
	}

	protected void parse(ParseTree tree, int startIdx) {
		List<Statement> stats = new ArrayList<>();
		for (int i = startIdx; i < tree.getChildCount(); i++) {
			BdsNode node = factory(tree, i);
			if (node != null) stats.add((Statement) node);
		}

		// Create an array
		statements = stats.toArray(new Statement[0]);
	}

	public void setStatements(Statement[] statements) {
		this.statements = statements;
	}

	@Override
	public String toAsm() {
		StringBuilder sb = new StringBuilder();

		if (isNeedsScope()) {
			sb.append("node " + id + "\n");
			sb.append("scopepush\n");
		}

		for (Statement s : statements)
			sb.append(s.toAsm());

		if (isNeedsScope()) sb.append("scopepop\n");

		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (statements != null) {
			for (int i = 0; i < statements.length; i++)
				sb.append(statements[i] + "\n");
		}
		return sb.toString();
	}

}
