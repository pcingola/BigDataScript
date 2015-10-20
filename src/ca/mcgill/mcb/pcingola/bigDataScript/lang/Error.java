package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.RunState;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

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
			msg = popString(bdsThread);
		}

		// Do not show error during checkpoint recovery
		if (bdsThread.isCheckpointRecover()) return;

		// Error
		Timer.showStdErr("Error" + (!msg.isEmpty() ? ": " + msg : ""));
		bdsThread.setExitValue(1L); // Set exit value
		bdsThread.setRunState(RunState.EXIT);
	}
}
