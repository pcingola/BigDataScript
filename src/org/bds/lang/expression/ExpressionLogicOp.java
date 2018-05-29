package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * A binary expression: logic operations (and, or)
 *
 * @author pcingola
 */
public class ExpressionLogicOp extends ExpressionDelegateBinary {

	private static final long serialVersionUID = 1589639689434269454L;

	public ExpressionLogicOp(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		String op = tree.getChild(1).getText();
		switch (op) {
		case "&&":
			expr = new ExpressionLogicAnd(this, tree);
			break;

		case "||":
			expr = new ExpressionLogicOr(this, tree);
			break;

		default:
			throw new RuntimeException("Unsuported operator '" + op + "'. This should never happen!");
		}
	}

}
