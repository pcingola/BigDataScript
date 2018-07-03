package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.symbol.SymbolTable;

/**
 * Expression
 *
 * @author pcingola
 */
public class ExpressionAssignmentBitOr extends ExpressionAssignmentBinary {

	private static final long serialVersionUID = -2969794879422342835L;

	public ExpressionAssignmentBitOr(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected ExpressionBinary createSubExpression() {
		return new ExpressionBitOr(this, null);
	}

	@Override
	protected ExpressionBinary createSubExpressionBool(Type leftType) {
		return new ExpressionLogicOr(this, null);
	}

	@Override
	protected String op() {
		return "|=";
	}

	@Override
	protected void replaceSubExpression(SymbolTable symtab) {
		if (left.isBool()) replaceSubExpressionBool(symtab);
	}
}
