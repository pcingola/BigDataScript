package org.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bigDataScript.compile.CompilerMessages;
import org.bigDataScript.scope.Scope;

/**
 * An unary expression
 *
 * @author pcingola
 */
public class ExpressionUnary extends Expression {

	Expression expr;
	protected String op;

	public ExpressionUnary(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected boolean isReturnTypesNotNull() {
		return expr.getReturnType() != null;
	}

	@Override
	protected void parse(ParseTree tree) {
		expr = (Expression) factory(tree, 1);
	}

	/**
	 * Which type does this expression return?
	 */
	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		returnType = expr.returnType(scope);
		return returnType;
	}

	@Override
	public String toString() {
		return op + " " + expr.toString();
	}

	@Override
	protected void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		expr.typeCheck(scope, compilerMessages);
	}

}
