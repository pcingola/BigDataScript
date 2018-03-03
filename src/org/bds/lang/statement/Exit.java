package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;
import org.bds.run.RunState;
import org.bds.scope.Scope;

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
		returnType = Types.INT;

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
			bdsThread.setExitValue(bdsThread.popInt()); // Set return map to scope
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
	public void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		returnType(scope);

		if ((expr != null) //
				&& (expr.getReturnType() != null) //
				&& (!expr.getReturnType().canCastTo(returnType)) //
		) {
			compilerMessages.add(this, "Cannot cast " + expr.getReturnType() + " to " + returnType, MessageType.ERROR);
		}
	}

}
