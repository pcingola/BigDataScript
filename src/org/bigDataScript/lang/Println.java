package org.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bigDataScript.run.BdsThread;

/**
 * An "print" statement
 *
 * @author pcingola
 */
public class Println extends Print {

	public Println(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Run the program
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		String msg = "";
		if (expr != null) {
			bdsThread.run(expr); // Evaluate expression to show
			if (bdsThread.isCheckpointRecover()) return;
			msg = popString(bdsThread);
		}

		if (bdsThread.isCheckpointRecover()) return;
		System.out.println(!msg.isEmpty() ? msg : "");
	}

}
