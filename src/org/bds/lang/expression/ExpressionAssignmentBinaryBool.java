package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.symbol.SymbolTable;

/**
 * Expression where there is a binary operator and an assignment for 'bool' values
 *
 * Examples
 * 		a &= b
 * 		a |= b
 *
 * @author pcingola
 */
public abstract class ExpressionAssignmentBinaryBool extends ExpressionAssignmentBinary {

	private static final long serialVersionUID = 7222418595388572233L;

	public ExpressionAssignmentBinaryBool(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Create sub-expression for a specific return type
	 */
	protected abstract ExpressionBinary createSubExpressionBool(Type leftType);

	/**
	 * We need to replace the sub-expression after we know the left-hand side type
	 */
	@Override
	protected void replaceSubExpression(SymbolTable symtab) {
		if (!left.isBool()) return;

		// Get original left and right expressions
		ExpressionBinary eb = (ExpressionBinary) right;
		Expression l = eb.getLeft();
		Expression r = eb.getRight();

		ExpressionBinary reb = createSubExpressionBool(left.getReturnType());
		reb.setLeft(l);
		reb.setRight(r);
		reb.returnType(symtab);
		right = reb;
	}
}
