package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * A binary expression: bit operations (and, or, xor)
 *
 * @author pcingola
 */
public class ExpressionBitOp extends ExpressionDelegateBinary {

	private static final long serialVersionUID = 1589639689434269454L;

	public ExpressionBitOp(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		String op = tree.getChild(1).getText();
		switch (op) {
		case "&":
			expr = new ExpressionBitAnd(this, tree);
			break;

		case "|":
			expr = new ExpressionBitOr(this, tree);
			break;

		case "^":
			expr = new ExpressionBitXor(this, tree);
			break;

		default:
			throw new RuntimeException("Unsuported operator '" + op + "'. This should never happen!");
		}
	}

}
