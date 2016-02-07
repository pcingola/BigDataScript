package org.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bigDataScript.run.BdsThread;
import org.bigDataScript.util.Timer;

/**
 * An "warning" statement (quit the program immediately)
 *
 * @author pcingola
 */
public class Warning extends Error {

	public Warning(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Run the program
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		String msg = "";
		if (expr != null) {
			// Evaluate expression to show
			bdsThread.run(expr);
			if (bdsThread.isCheckpointRecover()) return;
			msg = popString(bdsThread);
		}

		if (bdsThread.isCheckpointRecover()) return;
		Timer.showStdErr("Warning" + (!msg.isEmpty() ? ": " + msg : ""));
	}

}
