package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.run.BdsThread;

/**
 * A bitwise XOR
 *
 * @author pcingola
 */
public class ExpressionBitXor extends ExpressionBit {

	private static final long serialVersionUID = -2473522262501255653L;

	public ExpressionBitXor(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected String op() {
		return "^";
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		bdsThread.run(left);
		bdsThread.run(right);

		if (bdsThread.isCheckpointRecover()) return;

		bdsThread.push(bdsThread.popInt() ^ bdsThread.popInt());
	}

	@Override
	public String toAsm() {
		String eb = super.toAsm();
		String op = "xori";
		return eb + op + "\n";
	}

}
