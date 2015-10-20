package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThread;

/**
 * A bitwise AND
 *
 * @author pcingola
 */
public class ExpressionBitAnd extends ExpressionBit {

	public ExpressionBitAnd(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected String op() {
		return "&";
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		bdsThread.run(left);
		bdsThread.run(right);
		if (bdsThread.isCheckpointRecover()) return;
		bdsThread.push(popInt(bdsThread) & popInt(bdsThread));
	}

}
