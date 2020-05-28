package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.symbol.SymbolTable;

/**
 * A "checkpoint" statement
 *
 * @author pcingola
 */
public class Checkpoint extends Statement {

	private static final long serialVersionUID = 6044895488148887001L;

	Expression expr;

	public Checkpoint(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		int idx = 0;
		if (isTerminal(tree, idx, "checkpoint")) idx++; // 'checkpoint'
		if (tree.getChildCount() > idx) expr = (Expression) factory(tree, idx);
	}

	@Override
	public String toAsm() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toAsm());

		if (expr != null) {
			// Use expression as filename
			sb.append(expr.toAsm());
		} else {
			// Empty file name. Checkpoint will pick a name
			sb.append("pushs ''\n");
		}
		sb.append("checkpoint\n");
		return sb.toString();
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName().toLowerCase() + " " + expr;
	}

	@Override
	public void typeCheck(SymbolTable symtab, CompilerMessages compilerMessages) {
		if (expr != null) {
			expr.returnType(symtab);
			if (!expr.getReturnType().isString()) compilerMessages.add(this, "Checkpoint argument should be a string (file name)", MessageType.ERROR);
		}
	}
}
