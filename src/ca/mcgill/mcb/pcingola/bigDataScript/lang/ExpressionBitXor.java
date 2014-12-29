package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

/**
 * A bitwise XOR
 *
 * @author pcingola
 */
public class ExpressionBitXor extends ExpressionBit {

	public ExpressionBitXor(BigDataScriptNode parent, ParseTree tree) {
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
	public void runStep(BigDataScriptThread bdsThread) {
		bdsThread.run(left);
		bdsThread.run(right);
		if (bdsThread.isCheckpointRecover()) return;

		bdsThread.push(left.popInt(bdsThread) ^ right.popInt(bdsThread));
	}

}
