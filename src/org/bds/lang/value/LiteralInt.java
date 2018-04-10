package org.bds.lang.value;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.util.Gpr;

/**
 * An int literal
 *
 * @author pcingola
 */
public class LiteralInt extends Literal {

	private static final long serialVersionUID = -2949196571488456087L;

	public LiteralInt(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void initialize() {
		super.initialize();
		value = new ValueInt();
	}

	@Override
	protected ValueInt parseValue(ParseTree tree) {
		String intStr = tree.getChild(0).getText().toLowerCase();
		long l;
		if (intStr.startsWith("0x")) l = Long.parseLong(intStr.substring(2), 16);
		else l = Gpr.parseLongSafe(intStr);
		return new ValueInt(l);
	}

	@Override
	public String toAsm() {
		if (value == null) return "pushi 0\n";
		return "pushi " + value.asInt() + "\n";
	}

}
