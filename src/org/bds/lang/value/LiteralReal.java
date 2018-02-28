package org.bds.lang.value;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.util.Gpr;

/**
 * A real literal
 *
 * @author pcingola
 */
public class LiteralReal extends Literal {

	public LiteralReal(BdsNode parent, ParseTree tree) {
		super(parent, tree);
		value = new ValueReal();
	}

	@Override
	protected Double parseValue(ParseTree tree) {
		return Gpr.parseDoubleSafe(tree.getChild(0).getText());
	}

}
