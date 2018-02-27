package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.run.BdsThread;
import org.bds.run.DebugMode;

/**
 * An "breakpoint" statement
 *
 * @author pcingola
 */
public class Breakpoint extends Print {

	public Breakpoint(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Run the program
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		// Switch debug mode to 'step'
		bdsThread.setDebugMode(DebugMode.STEP);

		// Show message
		String msg = "";
		if (expr != null) {
			// Evaluate expression to show
			bdsThread.run(expr);
			if (bdsThread.isCheckpointRecover()) return;
			msg = bdsThread.popString();
		}

		if (bdsThread.isCheckpointRecover()) return;
		System.err.print("Breakpoint " + getFileName() + ", line " + getLineNum() + (!msg.isEmpty() ? ": " + msg : ""));
	}

}
