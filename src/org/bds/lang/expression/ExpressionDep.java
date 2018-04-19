package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * A 'dep' expression
 *
 * @author pcingola
 */
public class ExpressionDep extends ExpressionTask {

	private static final long serialVersionUID = -5966021318461797289L;

	public ExpressionDep(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Commands (i.e. task)
	 */
	@Override
	protected String toAsmCmd(String labelEnd) {
		StringBuilder sb = new StringBuilder();
		// Statements (e.g.: sys commands)
		sb.append(toAsmStatements());
		sb.append("taskdep\n");
		sb.append("jmp " + labelEnd + "\n"); // Go to the end
		return sb.toString();
	}

	@Override
	public String toString() {
		return "dep" //
				+ (options != null ? options : "") //
				+ " " //
				+ toStringStatement() //
		;
	}

}
