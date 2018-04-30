package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;

/**
 * A comparison expression (<)
 *
 * @author pcingola
 */
public class ExpressionGt extends ExpressionCompare {

	private static final long serialVersionUID = 4293517244590722796L;

	public ExpressionGt(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected String op() {
		return ">";
	}

	@Override
	public String toAsmOp() {
		return "gt";
	}

}
