package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.symbol.SymbolTable;

/**
 * A logical and bitwise XOR
 *
 * @author pcingola
 */
public class ExpressionBitXor extends ExpressionBit {

	private static final long serialVersionUID = -2473522262501255653L;

	public ExpressionBitXor(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected String op() {
		return "^";
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;

		super.returnType(symtab);

		if (left.isBool() && right.isBool()) returnType = Types.BOOL;
		else returnType = Types.INT;

		return returnType;
	}

	@Override
	public String toAsm() {
		if (isBool()) return super.toAsm() + "xorb\n";
		return super.toAsm() + "xori\n";
	}

	@Override
	public void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Check that either both expressions are boolean (i.e. returnType
		// is bool) or both can be converted to int
		if (!isBool()) {
			super.typeCheckNotNull(symtab, compilerMessages);
		}
	}
}
