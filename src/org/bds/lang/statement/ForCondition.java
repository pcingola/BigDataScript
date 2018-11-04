package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.ExpressionWrapper;
import org.bds.symbol.SymbolTable;

/**
 * for( ForInit ; ForCondition ; ForEnd ) Statements
 *
 * @author pcingola
 */
public class ForCondition extends ExpressionWrapper {

	private static final long serialVersionUID = -3735136770121564146L;

	public ForCondition(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	//	@Override
	//	public boolean isStopDebug() {
	//		return true;
	//	}

	@Override
	public String toAsm() {
		return expression.toAsm();
	}

	@Override
	public void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		if (!isBool()) compilerMessages.add(this, "For loop condition must be a bool expression", MessageType.ERROR);
	}

}
