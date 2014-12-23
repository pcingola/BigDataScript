package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * An "warning" statement (quit the program immediately)
 *
 * @author pcingola
 */
public class Warning extends Error {

	public Warning(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Run the program
	 */
	@Override
	protected void runStep(BigDataScriptThread bdsThread) {
		String msg = "";
		if (expr != null) {
			// Evaluate expression to show
			expr.run(bdsThread);
			msg = popString(bdsThread);
		}
		Timer.showStdErr("Warning" + (!msg.isEmpty() ? ": " + msg : ""));
	}

	@Override
	public String toString() {
		return "warning " + expr + "\n";
	}

}
