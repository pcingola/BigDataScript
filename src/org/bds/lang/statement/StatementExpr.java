package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.ExpressionWrapper;

/**
 * A statement that is actually an expression.
 * E.g.:
 *        i++   # This statement is a line of code with only one expression ('i++')
 *
 * @author pcingola
 */
public class StatementExpr extends ExpressionWrapper {

	private static final long serialVersionUID = 2132767867913705734L;

	public StatementExpr(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public boolean isStopDebug() {
		return true;
	}

}
