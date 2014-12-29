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

	@Override
	protected String op() {
		return "||";
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void runStep(BigDataScriptThread bdsThread) {
		bdsThread.run(left);

		if (!bdsThread.isCheckpointRecover()) {
			if ((Boolean) bdsThread.peek()) return; // Already true? No need to evaluate the other expression

			//  'OR' only depends on 'right' value (left was false)
			bdsThread.pop(); // Remove 'left' result from stack
		}

		bdsThread.run(right);
	}

}
