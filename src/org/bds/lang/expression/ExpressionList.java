package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.symbol.SymbolTable;

/**
 * A list of expression
 *
 * An expression list returns the type of the last expression
 *
 * @author pcingola
 */
public class ExpressionList extends Expression {

	private static final long serialVersionUID = -7620532827417450679L;

	protected Expression expressions[];

	public ExpressionList(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public boolean isReturnTypesNotNull() {
		for (Expression expr : expressions)
			if (expr.getReturnType() == null) return false;
		return true;
	}

	@Override
	protected void parse(ParseTree tree) {
		parse(tree, 0, tree.getChildCount());
	}

	/**
	 * Parse child nodes between child[offset] and child[max]
	 * Note:  child[max] is NOT parsed
	 */
	protected void parse(ParseTree tree, int offset, int max) {
		int num = (max - offset + 1) / 2; // Comma separated list of expressions
		expressions = new Expression[num];

		// Note, we add 2 to skip the next ',' separator
		for (int i = offset, j = 0; i < max; j++, i += 2) {
			if (lineNum <= 0) lineAndPos(tree.getChild(i)); // Set line number if not already done
			expressions[j] = (Expression) factory(tree, i);
		}
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;

		Type type = null;
		for (Expression expr : expressions)
			type = expr.returnType(symtab);

		returnType = type;
		return returnType;
	}

	@Override
	public String toAsm() {
		if (expressions.length <= 0) return "";
		if (expressions.length == 1) return expressions[0].toAsm();

		// Run all expression, but keep only the last one in the stack
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < expressions.length; i++) {
			sb.append(expressions[i].toAsm());
			if (i < expressions.length - 1) sb.append("pop\n");
		}

		return sb.toString();
	}

	@Override
	public String toString() {
		// Special cases: Empty or only one element
		if (expressions.length <= 0) return "";
		if (expressions.length == 1) return expressions[0].toString();

		// List
		StringBuilder sb = new StringBuilder();

		sb.append("( ");
		for (int i = 0; i < expressions.length; i++) {
			sb.append(expressions[i]);
			if (i < expressions.length) sb.append(", ");
		}
		sb.append(" )");

		return sb.toString();
	}

	@Override
	protected void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		for (Expression expr : expressions)
			expr.typeCheck(symtab, compilerMessages);
	}
}
