package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;

/**
 * An "print" statement
 *
 * @author pcingola
 */
public class Print extends Exit {

	public Print(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		// Calculate expression's return type
		if (expr != null) expr.returnType(scope);

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

		if (bdsThread.isCheckpointRecover()) return;
		if (!msg.isEmpty()) System.out.print(msg);
	}

}
