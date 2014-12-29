package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

/**
 * A bitwise AND
 *
 * @author pcingola
 */
public class ExpressionBitAnd extends ExpressionBit {

	public ExpressionBitAnd(BigDataScriptNode parent, ParseTree tree) {
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
	public void runStep(BigDataScriptThread bdsThread) {
		bdsThread.run(left);
		bdsThread.run(right);
		if (bdsThread.isCheckpointRecover()) return;
		bdsThread.push(popInt(bdsThread) & popInt(bdsThread));
	}

}
