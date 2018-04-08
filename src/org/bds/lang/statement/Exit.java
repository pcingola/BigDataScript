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
 * An "exit" statement (quit the program immediately)
 *
 * @author pcingola
 */
public class Exit extends Statement {

	private static final long serialVersionUID = 1968968982219240881L;

	Expression expr;

	public Exit(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		// child[0] = 'exit'
		if (tree.getChildCount() > 1) expr = (Expression) factory(tree, 1);
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;

		// Calculate expression's return type
		if (expr != null) expr.returnType(symtab);

		// Program's return type is 'int' (exit code)
		returnType = Types.INT;

		return returnType;
	}

	@Override
	public String toAsm() {
		return super.toAsm() //
				+ (expr != null ? expr.toAsm() : "") //
				+ "halt\n" //
		;
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

		if ((expr != null) //
				&& (expr.getReturnType() != null) //
				&& (!expr.getReturnType().canCastTo(returnType)) //
		) {
			compilerMessages.add(this, "Cannot cast " + expr.getReturnType() + " to " + returnType, MessageType.ERROR);
		}
	}

}
