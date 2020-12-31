package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * Post increment / decrement operator
 *
 * E.g. :  i++ or i--
 *
 * @author pcingola
 */
public class Post extends Pre {

	private static final long serialVersionUID = -7171687135383949830L;

	public Post(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		BdsNode node = factory(tree, 0);
		if (node instanceof Reference) expr = (Expression) node;

		operation = PrePostOperation.parse(tree.getChild(1).getText());
	}

	@Override
	public String toAsm() {
		StringBuilder sb = new StringBuilder();
		Reference ref = (Reference) expr;
		sb.append(ref.toAsm());
		sb.append("dup\n");

		switch (operation) {
		case INCREMENT:
			sb.append("inc\n");
			break;

		case DECREMENT:
			sb.append("dec\n");
			break;

		default:
			throw new RuntimeException("Unknown operator " + operation);
		}

		sb.append(ref.toAsmSet());
		sb.append("pop\n");

		return sb.toString();
	}

	@Override
	public String toString() {
		return "" + expr + operation;
	}

}
