package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.run.BdsThread;
import org.bds.run.RunState;

/**
 * A "break" statement
 *
 * @author pcingola
 */
public class Break extends Statement {

	public Break(BdsNode parent, ParseTree tree) {
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
		bdsThread.setRunState(RunState.BREAK);
	}

	@Override
	public String toString() {
		return "break";
	}
}
