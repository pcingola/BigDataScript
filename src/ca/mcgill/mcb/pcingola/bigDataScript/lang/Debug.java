package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

/**
 * An "debug" statement
 *
 * @author pcingola
 */
public class Debug extends Print {

	public Debug(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Run the program
	 */
	@Override
	public void runStep(BigDataScriptThread bdsThread) {
		// Only show message if we are in debug mode
		// Otherwise, the statement is ignored
		if (bdsThread.getDebugMode() != null) {
			String msg = "";
			if (expr != null) {
				// Evaluate expression to show
				bdsThread.run(expr);
				if (bdsThread.isCheckpointRecover()) return;
				msg = popString(bdsThread);
			}

			if (bdsThread.isCheckpointRecover()) return;
			System.err.print("Debug " + getFileName() + ", line " + getLineNum() + (!msg.isEmpty() ? ": " + msg : ""));
		}
	}

}
