package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.run.BdsThread;

/**
 * Boolean OR
 *
 * @author pcingola
 */
public class ExpressionLogicOr extends ExpressionLogic {

	public ExpressionLogicOr(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected String op() {
		return "||";
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		bdsThread.run(left);

		if (!bdsThread.isCheckpointRecover()) {
			boolean ok = bdsThread.peek().asBool();
			if (ok) return; // Already true? No need to evaluate the other expression
		}

		//  'OR' only depends on 'right' map (left was false)
		bdsThread.pop(); // Remove 'left' result from stack

		bdsThread.run(right);
	}

	@Override
	public String toAsm() {
		String eb = super.toAsm();
		String op = "orb";
		return eb + op + "\n";
	}

}
