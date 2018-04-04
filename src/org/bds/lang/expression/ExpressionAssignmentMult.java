package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * Expression
 *
 * @author pcingola
 */
public class ExpressionAssignmentMult extends ExpressionAssignmentBinary {

	private static final long serialVersionUID = 8347513714266058836L;

	public ExpressionAssignmentMult(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected ExpressionBinary createSubExpression() {
		return new ExpressionTimes(this, null);
	}

	@Override
	protected String op() {
		return "*=";
	}

}
