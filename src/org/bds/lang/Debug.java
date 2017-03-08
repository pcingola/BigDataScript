package org.bds.lang;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.run.BdsThread;

/**
 * An "debug" statement
 *
 * @author pcingola
 */
public class Debug extends Print {

	public Debug(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Run the program
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		// Only show message if we are in debug mode
		// Otherwise, the statement is ignored
		if (bdsThread.getDebugMode() != null) {
			String msg = "";
			if (expr != null) {
				// Evaluate expression to show
				bdsThread.run(expr);
				if (bdsThread.isCheckpointRecover()) return;
				msg = bdsThread.popString();
			}

			if (bdsThread.isCheckpointRecover()) return;
			System.err.print("Debug " + getFileName() + ", line " + getLineNum() + (!msg.isEmpty() ? ": " + msg : ""));
		}
	}

}
