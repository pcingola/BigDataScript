package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.symbol.SymbolTable;

/**
 * Logic negation
 *
 * @author pcingola
 */
public class ExpressionLogicNot extends ExpressionUnary {

	private static final long serialVersionUID = 4334042905605735832L;

	public ExpressionLogicNot(BdsNode parent, ParseTree tree) {
		super(parent, tree);
		op = "!";
	}

	@Override
	public String toAsm() {
		return expr.toAsm() + "notb\n";
	}

	@Override
	public void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Can we transform to bool?
		expr.checkCanCastToBool(compilerMessages);
	}

}
