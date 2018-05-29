package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * A binary expression: compare operators (<, >, <=, >=, ==, !=)
 *
 * @author pcingola
 */
public class ExpressionComp extends ExpressionDelegateBinary {

	private static final long serialVersionUID = 1589639689434269454L;

	public ExpressionComp(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		String op = tree.getChild(1).getText();
		switch (op) {
		case "<":
			expr = new ExpressionLt(this, tree);
			break;

		case "<=":
			expr = new ExpressionLe(this, tree);
			break;

		case "==":
			expr = new ExpressionEq(this, tree);
			break;

		case "!=":
			expr = new ExpressionNe(this, tree);
			break;

		case ">=":
			expr = new ExpressionGe(this, tree);
			break;

		case ">":
			expr = new ExpressionGt(this, tree);
			break;

		default:
			throw new RuntimeException("Unsuported operator '" + op + "'. This should never happen!");
		}
	}

}
