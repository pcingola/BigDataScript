package org.bds.lang.value;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.symbol.SymbolTable;

/**
 * Expression 'Literal'
 *
 * @author pcingola
 */
public class LiteralNull extends Literal {

	private static final long serialVersionUID = 5488861478790119788L;

	public LiteralNull(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public Type getReturnType() {
		return Types.NULL;
	}

	@Override
	protected void parse(ParseTree tree) {
		// Nothing to do
	}

	@Override
	protected Value parseValue(ParseTree tree) {
		return null;
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		return Types.NULL;
	}

	@Override
	public String toAsm() {
		return "pushnull\n";
	}

	@Override
	public String toString() {
		return "null";
	}

}
