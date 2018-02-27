package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.Type;
import org.bds.run.BdsThread;
import org.bds.scope.Scope;

/**
 * Something the is actually an expression
 *
 * @author pcingola
 */
public class ExpressionWrapper extends Expression {

	protected Expression expression;

	public ExpressionWrapper(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	public Expression getExpression() {
		return expression;
	}

	@Override
	public boolean isReturnTypesNotNull() {
		return expression.getReturnType() != null;
	}

	@Override
	protected void parse(ParseTree tree) {
		expression = (Expression) factory(tree, 0);
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		returnType = expression.returnType(scope);
		return returnType;
	}

	@Override
	public void runStep(BdsThread bdsThread) {
		bdsThread.run(expression);
	}

	@Override
	public String toString() {
		return (expression != null ? expression.toString() : getClass().getSimpleName() + ":NULL");
	}

	@Override
	protected void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		expression.typeCheck(scope, compilerMessages);
	}

}
