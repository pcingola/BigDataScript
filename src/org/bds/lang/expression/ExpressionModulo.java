package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.symbol.SymbolTable;

/**
 * A module operation
 *
 * @author pcingola
 */
public class ExpressionModulo extends ExpressionMath {

	private static final long serialVersionUID = 8708379553154399665L;

	public ExpressionModulo(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected String op() {
		return "%";
	}

	@Override
	public String toAsm() {
		String eb = super.toAsm();
		return eb + "modi\n";
	}

	@Override
	public void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		left.checkCanCastToInt(compilerMessages);
		right.checkCanCastToInt(compilerMessages);
	}

}
