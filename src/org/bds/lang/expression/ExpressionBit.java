package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.symbol.SymbolTable;

/**
 * A bitwise expression
 *
 * @author pcingola
 */
public class ExpressionBit extends ExpressionBinary {

	private static final long serialVersionUID = 3166871171374710364L;

	public ExpressionBit(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;

		super.returnType(symtab);
		returnType = Types.INT;

		return returnType;
	}

	@Override
	public void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Can we transform 'left' into an int?
		left.getReturnType().checkCanCast(Types.INT, compilerMessages);
		right.getReturnType().checkCanCast(Types.INT, compilerMessages);
	}

}
