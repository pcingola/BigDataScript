package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

/**
 * Boolean OR
 *
 * @author pcingola
 */
public class ExpressionLogicOr extends ExpressionLogic {

	public ExpressionLogicOr(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void eval(BigDataScriptThread bdsThread) {
		left.eval(bdsThread);
		if ((Boolean) bdsThread.peek()) return; // Already true? No need to evaluate the other expression
		right.eval(bdsThread); // The 'OR' only depends on this value (left was false)
	}

	@Override
	protected String op() {
		return "||";
	}

}
