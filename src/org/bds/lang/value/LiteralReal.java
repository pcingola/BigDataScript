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

	private static final long serialVersionUID = 7932776789019582721L;

	public LiteralReal(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void initialize() {
		super.initialize();
		value = new ValueReal();
	}

	@Override
	protected Double parseValue(ParseTree tree) {
		return Gpr.parseDoubleSafe(tree.getChild(0).getText());
	}

	@Override
	public String toAsm() {
		if (value == null) return "pushr 0.0\n";
		return "pushr " + value.asReal() + "\n";
	}

}
