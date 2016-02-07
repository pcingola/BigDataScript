package org.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bigDataScript.scope.Scope;

/**
 * A binary expression
 * 
 * @author pcingola
 */
public class ExpressionBinary extends Expression {

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
	protected boolean isReturnTypesNotNull() {
		if (right == null) return (left.getReturnType() != null);
		return (left.getReturnType() != null) && (right.getReturnType() != null);
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
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		returnType = left.returnType(scope);
		if (right != null) right.returnType(scope); // Only assign this to show that calculation was already performed

		return returnType;
	}

	public void setLeft(Expression left) {
		this.left = left;
	}

	public void setRight(Expression right) {
		this.right = right;
	}

	@Override
	public String toString() {
		return left + " " + op() + " " + right;
	}

}
