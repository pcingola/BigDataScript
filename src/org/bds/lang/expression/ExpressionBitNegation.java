package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Types;
import org.bds.symbol.SymbolTable;

/**
 * A bitwise negation
 *
 * @author pcingola
 */
public class ExpressionBitNegation extends ExpressionUnary {

	private static final long serialVersionUID = 3145269999331496620L;

	public ExpressionBitNegation(BdsNode parent, ParseTree tree) {
		super(parent, tree);
		op = "~";
	}

	@Override
	public String toAsm() {
		return expr.toAsm() + "noti\n";
	}

	@Override
	public void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Can we transform to an int?
		expr.getReturnType().checkCanCast(Types.INT, compilerMessages);
	}
}
