package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;

/**
 * Conditional expression
 *
 * 		expr ? exprTrue : exprFalse
 *
 * @author pcingola
 */
public class ExpressionCond extends Expression {

	Expression expr;
	Expression exprTrue;
	Expression exprFalse;

	public ExpressionCond(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected boolean isReturnTypesNotNull() {
		if (expr == null || expr.getReturnType() == null) return false;
		if (exprTrue == null || exprTrue.getReturnType() == null) return false;
		if (exprFalse == null || exprFalse.getReturnType() == null) return false;
		return true;
	}

	@Override
	protected void parse(ParseTree tree) {
		expr = (Expression) factory(tree, 0);
		exprTrue = (Expression) factory(tree, 2); // Child 1 is '?'
		exprFalse = (Expression) factory(tree, 4); // Child 3 is ':'
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		expr.returnType(scope);
		returnType = exprTrue.returnType(scope);
		exprFalse.returnType(scope);

		return returnType;
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void runStep(BigDataScriptThread bdsThread) {
		bdsThread.run(expr);
		if (popBool(bdsThread)) bdsThread.run(exprTrue);
		else bdsThread.run(exprFalse);
	}

	@Override
	public String toString() {
		return expr.toString() + " ? " + exprTrue + " : " + exprFalse;
	}

	@Override
	public void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		if (expr != null) expr.checkCanCastBool(compilerMessages);

		if (exprTrue != null //
				&& exprFalse != null //
				&& !exprTrue.getReturnType().canCast(exprFalse.getReturnType()) //
				) compilerMessages.add(this, "Both expressions must be the same type. Expression for 'true': " + exprTrue.getReturnType() + ", expression for 'false' " + exprFalse.getReturnType(), MessageType.ERROR);
	}
}
