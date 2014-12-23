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
	public void runStep(BigDataScriptThread bdsThread) {
		left.run(bdsThread);
		if ((Boolean) bdsThread.peek()) return; // Already true? No need to evaluate the other expression

		//  'OR' only depends on 'right' value (left was false)
		bdsThread.pop(); // Remove 'left' result from stack
		right.run(bdsThread);
	}

	@Override
	protected String op() {
		return "||";
	}

}
