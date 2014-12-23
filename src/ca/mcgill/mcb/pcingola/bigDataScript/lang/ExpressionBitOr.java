package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

/**
 * A bitwise OR
 *
 * @author pcingola
 */
public class ExpressionBitOr extends ExpressionBit {

	public ExpressionBitOr(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void eval(BigDataScriptThread bdsThread) {
		left.eval(bdsThread);
		right.eval(bdsThread);
		bdsThread.push(left.popInt(bdsThread) | right.popInt(bdsThread));
	}

	@Override
	protected String op() {
		return "|";
	}

}
