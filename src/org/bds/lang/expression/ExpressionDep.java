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

	@Override
	public String toString() {
		return "dep" //
				+ (options != null ? options : "") //
				+ " " //
				+ toStringStatement() //
		;
	}

}
