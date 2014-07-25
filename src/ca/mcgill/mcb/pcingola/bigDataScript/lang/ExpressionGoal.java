package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;

/**
 * A 'goal' expression
 *
 * @author pcingola
 */
public class ExpressionGoal extends ExpressionUnary {

	public ExpressionGoal(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
		op = "goal";
	}

	@Override
	public Object eval(BigDataScriptThread bdsThread) {
		Object value = expr.eval(bdsThread);
		return bdsThread.goal(value.toString());
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;
		returnType = TypeList.get(Type.STRING);
		return returnType;
	}

	@Override
	protected void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		super.typeCheckNotNull(scope, compilerMessages);

		if (!expr.getReturnType().isString()) compilerMessages.add(this, "Expression does not return 'string' (" + expr.getReturnType() + ")", MessageType.ERROR);
	}
}
