package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

/**
 * A arithmetic 'plus'
 *
 * @author pcingola
 */
public class ExpressionUnaryPlus extends ExpressionUnaryMinus {

	public ExpressionUnaryPlus(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
		op = "+";
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public Object eval(BigDataScriptThread csThread) {
		if (returnType == Type.INT) return expr.evalInt(csThread);
		else if (returnType == Type.REAL) return expr.evalReal(csThread);
		else throw new RuntimeException("Cannot cast to 'int' or 'real'. This should never happen!");
	}

}
