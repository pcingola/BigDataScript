package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

/**
 * Expression '/='
 * 
 * @author pcingola
 */
public class ExpressionAssignmentDiv extends ExpressionAssignmentBinary {

	public ExpressionAssignmentDiv(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected ExpressionBinary createSubExpression() {
		return new ExpressionDivide(this, null);
	}

	@Override
	protected String op() {
		return "/=";
	}

}
