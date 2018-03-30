package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.run.BdsThread;

/**
 * A bitwise OR
 *
 * @author pcingola
 */
public class ExpressionBitOr extends ExpressionBit {

	private static final long serialVersionUID = -4468043225504579475L;

	public ExpressionBitOr(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected String op() {
		return "|";
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		bdsThread.run(left);
		bdsThread.run(right);

		if (bdsThread.isCheckpointRecover()) return;

		bdsThread.push(bdsThread.popInt() | bdsThread.popInt());
	}

	@Override
	public String toAsm() {
		String eb = super.toAsm();
		String op = "ori";
		return eb + op + "\n";
	}

}
