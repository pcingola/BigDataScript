package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.RunState;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * An "error" statement (quit the program immediately)
 *
 * @author pcingola
 */
public class Error extends Exit {

	public Error(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		// Calculate expression's return type
		if (expr != null) expr.returnType(scope);

		// Program's return type is 'int' (exit code)
		returnType = Type.STRING;

		return returnType;
	}

	/**
	 * Run the program
	 */
	@Override
	public void runStep(BigDataScriptThread bdsThread) {
		String msg = "";
		if (expr != null) {
			// Evaluate expression to show
			bdsThread.run(expr);
			if (bdsThread.isCheckpointRecover()) return;

			msg = popString(bdsThread);
		}

		Timer.showStdErr("Error" + (!msg.isEmpty() ? ": " + msg : ""));

		bdsThread.setExitValue(1L); // Set exit value
		bdsThread.setRunState(RunState.EXIT);
	}

	@Override
	public String toString() {
		return "error " + expr + "\n";
	}

	@Override
	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		returnType(scope);
		if ((expr.getReturnType() != null) && (!expr.getReturnType().canCast(Type.STRING))) compilerMessages.add(this, "Cannot cast " + expr.getReturnType() + " to " + Type.STRING, MessageType.ERROR);
	}

}
