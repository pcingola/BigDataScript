package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.symbol.SymbolTable;

/**
 * An "print" statement
 *
 * @author pcingola
 */
public class Print extends Statement {

	private static final long serialVersionUID = 7817162862107991819L;

	Expression expr;

	public Print(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		// child[0] = 'print'
		if (tree.getChildCount() > 1) expr = (Expression) factory(tree, 1);
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;

		// Calculate expression's return type
		if (expr != null) expr.returnType(symtab);

		returnType = Types.STRING;
		return returnType;
	}

	@Override
	public String toAsm() {
		return super.toAsm() //
				+ (expr != null ? expr.toAsm() : "pushs ''\n") // String to print
				+ "print\n";
	}

	@Override
	public String toString() {
		return getClass().getSimpleName().toLowerCase() //
				+ (expr != null ? " " + expr : "") //
		;
	}

	@Override
	public void typeCheck(SymbolTable symtab, CompilerMessages compilerMessages) {
		returnType(symtab);

		if (expr != null && !expr.canCastToString()) {
			compilerMessages.add(this, "Cannot cast " + expr.getReturnType() + " to " + returnType, MessageType.ERROR);
		}
	}

}
