package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;

/**
 * Something the is actually an expression
 * 
 * @author pcingola
 */
public class ExpressionWrapper extends Expression {

	Expression expression;

	public ExpressionWrapper(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public void runStep(BigDataScriptThread bdsThread) {
		expression.run(bdsThread);
	}

	@Override
	protected boolean isReturnTypesNotNull() {
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
	protected void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		expression.typeCheck(scope, compilerMessages);
	}

}
