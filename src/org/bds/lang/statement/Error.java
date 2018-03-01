package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.run.BdsThread;
import org.bds.run.RunState;
import org.bds.util.Timer;

/**
 * An "error" statement (quit the program immediately)
 *
 * @author pcingola
 */
public class Error extends Print {

	public Error(BdsNode parent, ParseTree tree) {
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
			msg = bdsThread.popString();
		}

		// Do not show error during checkpoint recovery
		if (bdsThread.isCheckpointRecover()) return;

		// Error
		String filePos = bdsThread.getFileLinePos(this);
		Timer.showStdErr("Error" //
				+ (filePos.isEmpty() ? "" : " (" + filePos + ")") //
				+ (!msg.isEmpty() ? ": " + msg : "") //
		);

		bdsThread.setExitValue(1L); // Set exit map
		bdsThread.setRunState(RunState.EXIT);
	}
}
