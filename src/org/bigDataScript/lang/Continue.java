package org.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bigDataScript.compile.CompilerMessages;
import org.bigDataScript.compile.CompilerMessage.MessageType;
import org.bigDataScript.run.BdsThread;
import org.bigDataScript.run.RunState;
import org.bigDataScript.scope.Scope;

/**
 * A "continue" statement
 * 
 * @author pcingola
 */
public class Continue extends Statement {

	public Continue(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		// Nothing to do
	}

	/**
	 * Run the program
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		if (!bdsThread.isCheckpointRecover()) bdsThread.setRunState(RunState.CONTINUE);
	}

	@Override
	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		if ((findParent(ForLoop.class, FunctionDeclaration.class) == null) //
				&& (findParent(ForLoopList.class, FunctionDeclaration.class) == null) //
				&& (findParent(While.class, FunctionDeclaration.class) == null) //
		) compilerMessages.add(this, "continue statement outside loop", MessageType.ERROR);
	}
}
