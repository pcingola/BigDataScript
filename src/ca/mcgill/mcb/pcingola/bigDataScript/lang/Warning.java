package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.RunState;
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
	protected RunState runStep(BigDataScriptThread csThread) {
		String msg = "";
		if (expr != null) msg = expr.evalString(csThread); // Evaluate expression to show

		Timer.showStdErr("Warning" + (!msg.isEmpty() ? ": " + msg : ""));

		return RunState.OK;
	}

	@Override
	public String toString() {
		return "warning( " + expr + " )\n";
	}

}
