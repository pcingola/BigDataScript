package org.bds.lang.value;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
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
	public Type getReturnType() {
		return Types.REAL;
	}

	@Override
	protected void initialize() {
		super.initialize();
		value = new ValueReal();
	}

	@Override
	protected ValueReal parseValue(ParseTree tree) {
		return new ValueReal(Gpr.parseDoubleSafe(tree.getChild(0).getText()));
	}

	@Override
	public String toAsm() {
		return toAsm(false);
	}

	public String toAsm(boolean minus) {
		if (value == null) return "pushr 0.0\n";
		return "pushr " + (minus ? "-" : "") + value.asReal() + "\n";
	}

}
