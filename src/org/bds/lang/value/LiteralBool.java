package org.bds.lang.value;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.util.Gpr;

/**
 * A boolean literal
 *
 * @author pcingola
 */
public class LiteralBool extends Literal {

	public LiteralBool(BdsNode parent, ParseTree tree) {
		super(parent, tree);
		value = new ValueBool();
	}

	@Override
	protected Boolean parseValue(ParseTree tree) {
		return Gpr.parseBoolSafe(tree.getChild(0).getText());
	}
}
