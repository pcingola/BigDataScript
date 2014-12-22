package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

/**
 * Boolean AND
 *
 * @author pcingola
 */
public class ExpressionLogicAnd extends ExpressionLogic {

	public ExpressionLogicAnd(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void eval(BigDataScriptThread bdsThread) {
		bdsThread.push(left.evalBool(bdsThread) && right.evalBool(bdsThread));
	}

	@Override
	protected String op() {
		return "&&";
	}

}
