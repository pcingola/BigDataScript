package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.lang.value.LiteralInt;
import org.bds.lang.value.LiteralReal;
import org.bds.symbol.SymbolTable;

/**
 * An unary expression
 *
 * @author pcingola
 */
public class ExpressionUnary extends Expression {

	private static final long serialVersionUID = 2061510479540249610L;

	protected Expression expr;
	protected String op;

	public ExpressionUnary(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	boolean isLiteralInt() {
		return expr instanceof LiteralInt;
	}

	boolean isLiteralReal() {
		return expr instanceof LiteralReal;
	}

	@Override
	public boolean isReturnTypesNotNull() {
		return returnType != null && expr.getReturnType() != null;
	}

	@Override
	protected void parse(ParseTree tree) {
		expr = (Expression) factory(tree, 1);
	}

	/**
	 * Which type does this expression return?
	 */
	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;

		returnType = expr.returnType(symtab);
		return returnType;
	}

	@Override
	public String toString() {
		return op + " " + expr.toString();
	}

	@Override
	protected void typeCheckNotNull(SymbolTable scope, CompilerMessages compilerMessages) {
		expr.typeCheck(scope, compilerMessages);
	}

}
