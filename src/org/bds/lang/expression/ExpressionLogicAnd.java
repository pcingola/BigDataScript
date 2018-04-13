package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * Boolean AND
 *
 * @author pcingola
 */
public class ExpressionLogicAnd extends ExpressionLogic {

	private static final long serialVersionUID = 2229407346097707469L;

	public ExpressionLogicAnd(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected String op() {
		return "&&";
	}

	@Override
	public String toAsm() {
		// Logic and has to be implemented using a short-circuit operation
		// I.e: If left expression is false, we do not calculate right
		//      expression because we already know that the result will
		//      be 'false'

		String lableBase = baseLabelName();
		String labelFalse = lableBase + "false";
		String labelEnd = lableBase + "end";

		StringBuilder sb = new StringBuilder();
		sb.append(left.toAsm());
		sb.append("jmpf " + labelFalse + "\n");
		sb.append(right.toAsm());
		sb.append("jmp " + labelEnd + "\n");
		sb.append(labelFalse + ":\n");
		sb.append("pushb false\n");
		sb.append(labelEnd + ":\n");

		return sb.toString();
	}
}
