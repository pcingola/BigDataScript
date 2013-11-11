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
	public Object eval(BigDataScriptThread csThread) {
		return left.evalBool(csThread) || right.evalBool(csThread);
	}

	@Override
	protected String op() {
		return "||";
	}

}
