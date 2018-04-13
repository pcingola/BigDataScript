package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * Boolean OR
 *
 * @author pcingola
 */
public class ExpressionLogicOr extends ExpressionLogic {

	private static final long serialVersionUID = -9149820473234256403L;

	public ExpressionLogicOr(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected String op() {
		return "||";
	}

	@Override
	public String toAsm() {
		// Logic and has to be implemented using a short-circuit operation
		// I.e: If left expression is true, we do not calculate right 
		//      expression because we already know that the result will
		//      be 'true'

		String lableBase = baseLabelName();
		String labelTrue = lableBase + "true";
		String labelEnd = lableBase + "end";

		StringBuilder sb = new StringBuilder();
		sb.append(left.toAsm());
		sb.append("jmpt " + labelTrue + "\n");
		sb.append(right.toAsm());
		sb.append("jmp " + labelEnd + "\n");
		sb.append(labelTrue + ":\n");
		sb.append("pushb true\n");
		sb.append(labelEnd + ":\n");

		return sb.toString();
	}

}
