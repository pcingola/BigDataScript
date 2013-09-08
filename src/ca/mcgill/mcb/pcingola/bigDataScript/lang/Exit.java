package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.RunState;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;

/**
 * An "exit" statement (quit the program immediately)
 * 
 * @author pcingola
 */
public class Exit extends Statement {

	Expression expr;
	Type returnType;

	public Exit(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		// child[0] = 'exit'
		if (tree.getChildCount() > 1) expr = (Expression) factory(tree, 1);
	}

	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		// Calculate expression's return type
		if (expr != null) expr.returnType(scope);

		// Program's return type is 'int' (exit code)
		returnType = Type.INT;

		return returnType;
	}

	/**
	 * Run the program
	 */
	@Override
	protected RunState runStep(BigDataScriptThread csThread) {
		if (expr != null) csThread.setExitValue(expr.evalInt(csThread)); // Set return value to scope
		else csThread.setExitValue(0L); // Default is the same as 'exit 0'
		return RunState.EXIT;
	}

	@Override
	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		returnType(scope);
		if ((expr.getReturnType() != null) && (!expr.getReturnType().canCast(returnType))) compilerMessages.add(this, "Cannot cast " + expr.getReturnType() + " to " + returnType, MessageType.ERROR);
	}
}
