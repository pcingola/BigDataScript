package org.bds.lang;

import java.util.ArrayList;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.run.BdsThread;
import org.bds.serialize.BdsSerializer;
import org.bds.util.Gpr;

/**
 * Default statement (in switch condition)
 *
 */
public class Default extends Case {

	public Default(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		// Do nothing. The other parse method will be invoked by 'switch' parsing
	}

	@Override
	protected int parse(ParseTree tree, int idx) {
		statements = new ArrayList<>();

		idx = findIndex(tree, "default", idx);
		if (idx < 0) return idx; // No 'default' statement found
		idx++;

		if (isTerminal(tree, idx, ":")) idx++; // ':'

		while (idx < tree.getChildCount()) {
			if (isEndOfStatements(tree, idx)) break;

			Statement stat = (Statement) factory(tree, idx++);
			if (stat != null) statements.add(stat);
		}

		return idx;
	}

	/**
	 * Run the program
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		runStatements(bdsThread);
	}

	@Override
	public String serializeSave(BdsSerializer serializer) {
		String ret = super.serializeSave(serializer);
		Gpr.debug("Serialize: Default. Ret:" + ret);
		return ret;
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
