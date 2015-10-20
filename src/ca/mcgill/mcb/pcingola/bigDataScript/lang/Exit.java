package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.RunState;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;

/**
 * An "exit" statement (quit the program immediately)
 *
 * @author pcingola
 */
public class Exit extends Statement {

	Expression expr;

	public Exit(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		// child[0] = 'exit'
		if (tree.getChildCount() > 1) expr = (Expression) factory(tree, 1);
	}

	@Override
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
	public void runStep(BdsThread bdsThread) {
		if (expr != null) {
			bdsThread.run(expr);
			if (bdsThread.isCheckpointRecover()) return;
			bdsThread.setExitValue(popInt(bdsThread)); // Set return value to scope
		} else {
			if (bdsThread.isCheckpointRecover()) return;
			bdsThread.setExitValue(0L); // Default is the same as 'exit 0'
		}

		bdsThread.setRunState(RunState.EXIT);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName().toLowerCase() //
				+ (expr != null ? " " + expr : "") //
		;
	}

	@Override
	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		returnType(scope);

		if ((expr != null) //
				&& (expr.getReturnType() != null) //
				&& (!expr.getReturnType().canCast(returnType)) //
		) {
			compilerMessages.add(this, "Cannot cast " + expr.getReturnType() + " to " + returnType, MessageType.ERROR);
		}
	}

}
