package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.RunState;

/**
 * A "break" statement
 * 
 * @author pcingola
 */
public class Break extends Statement {

	public Break(BigDataScriptNode parent, ParseTree tree) {
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
	public void runStep(BigDataScriptThread bdsThread) {
		bdsThread.setRunState(RunState.BREAK);
	}

	@Override
	public String toString() {
		return "break\n";
	}
}
