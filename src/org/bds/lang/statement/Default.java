package org.bds.lang.statement;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.util.Gpr;

/**
 * Default statement (in switch condition)
 *
 */
public class Default extends Case {

	private static final long serialVersionUID = 147612756509370665L;

	public Default(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected String label() {
		return baseLabelName() + "default";
	}

	@Override
	protected void parse(ParseTree tree) {
		// Do nothing. The other parse method will be invoked by 'switch' parsing
	}

	@Override
	protected int parse(ParseTree tree, int idx) {
		List<Statement> stats = new ArrayList<>();

		idx = findIndex(tree, "default", idx);
		if (idx < 0) return idx; // No 'default' statement found
		idx++;

		if (isTerminal(tree, idx, ":")) idx++; // ':'

		while (idx < tree.getChildCount()) {
			if (isEndOfStatements(tree, idx)) break;

			Statement stat = (Statement) factory(tree, idx++);
			if (stat != null) stats.add(stat);
		}
		statements = stats.toArray(new Statement[0]);

		return idx;
	}

	@Override
	public String toAsmCondition(String varSwitchExpr) {
		return "";
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("default :\n");
		if (statements != null) {
			for (Statement s : statements)
				sb.append(Gpr.prependEachLine("\t", s.toString()));
		}
		sb.append("\n");

		return sb.toString();
	}

}
