package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.symbol.SymbolTable;

/**
 * A binary expression
 *
 * @author pcingola
 */
public class ExpressionBinary extends Expression {

	private static final long serialVersionUID = -9057903233688463643L;

	Expression left;
	Expression right;

	public ExpressionBinary(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	public Expression getLeft() {
		return left;
	}

	public Expression getRight() {
		return right;
	}

	@Override
	public boolean isReturnTypesNotNull() {
		if (right == null) return (left.getReturnType() != null);
		return left.isReturnTypesNotNull() && right.isReturnTypesNotNull();
	}

	/**
	 * Operator to show when printing this expression
	 */
	protected String op() {
		return this.getClass().getSimpleName().toLowerCase();
	}

	@Override
	protected void parse(ParseTree tree) {
		left = (Expression) factory(tree, 0);
		right = (Expression) factory(tree, 2); // Child 1 has the operator, we use child 2 here
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;

		returnType = left.returnType(symtab);
		if (right != null) right.returnType(symtab); // Only assign this to show that calculation was already performed

		return returnType;
	}

	public void setLeft(Expression left) {
		this.left = left;
	}

	public void setRight(Expression right) {
		this.right = right;
	}

	@Override
	public String toAsm() {
		return super.toAsm() //
				+ left.toAsm() //
				+ right.toAsm() //
		;
	}

	@Override
	public String toString() {
		return left + " " + op() + " " + right;
	}

}
