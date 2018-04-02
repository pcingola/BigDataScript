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

	private static final long serialVersionUID = -6159777492966451864L;

	public LiteralBool(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void initialize() {
		super.initialize();
		value = new ValueBool();
	}

	@Override
	protected Boolean parseValue(ParseTree tree) {
		return Gpr.parseBoolSafe(tree.getChild(0).getText());
	}

	@Override
	public String toAsm() {
		if (value == null) return "pushb false\n";
		return "pushb " + value.asBool() + "\n";
	}

}
