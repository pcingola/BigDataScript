package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.symbol.SymbolTable;

/**
 * Boolean expression
 *
 * @author pcingola
 */
public class ExpressionLogic extends ExpressionBinary {

	private static final long serialVersionUID = -1919645541823823005L;

	public ExpressionLogic(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;

		super.returnType(symtab);
		returnType = Types.BOOL;

		return returnType;

	}

	@Override
	public void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Can we transform 'left' into an int?
		left.checkCanCastToBool(compilerMessages);
		right.checkCanCastToBool(compilerMessages);
	}

}
