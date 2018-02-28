package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.scope.Scope;

/**
 * An unary expression
 *
 * @author pcingola
 */
public class ExpressionUnary extends Expression {

	protected Expression expr;
	protected String op;

	public ExpressionUnary(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public boolean isReturnTypesNotNull() {
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
