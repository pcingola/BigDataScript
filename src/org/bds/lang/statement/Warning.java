package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.run.BdsThread;
import org.bds.util.Timer;

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
			msg = bdsThread.popString();
		}

		if (bdsThread.isCheckpointRecover()) return;

		String filePos = bdsThread.getFileLinePos(this);
		Timer.showStdErr("Warning" //
				+ (filePos.isEmpty() ? "" : " (" + filePos + ")") //
				+ (!msg.isEmpty() ? ": " + msg : "") //
		);
	}

}
