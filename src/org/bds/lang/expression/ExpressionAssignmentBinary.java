package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.symbol.SymbolTable;

/**
 * Expression where there is a binary operator and an assignment
 *
 * Examples
 * 		a += b
 * 		a -= b
 * 		a *= b
 * 		a /= b
 * 		a &= b
 * 		a |= b
 *
 * @author pcingola
 */
public abstract class ExpressionAssignmentBinary extends ExpressionAssignment {

	private static final long serialVersionUID = 7795747128948650351L;

	public ExpressionAssignmentBinary(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	protected abstract ExpressionBinary createSubExpression();

	/**
	 * Create sub-expression for a specific return type
	 */
	protected ExpressionBinary createSubExpressionBool(Type leftType) {
		throw new RuntimeException("This method should not be invoked!");
	}

	@Override
	protected void parse(ParseTree tree) {
		left = (Expression) factory(tree, 0);
		Expression right = (Expression) factory(tree, 2); // Child 1 has the operator, we use child 2 here

		// Now we create a binary expression using 'left' and 'right'
		ExpressionBinary subExpression = createSubExpression();
		subExpression.setLeft(left);
		subExpression.setRight(right);
		this.right = subExpression;
	}

	/**
	 * In some cases we need to replace the sub-expression after we know the left-hand side type
	 * E.g.: '&=' uses BitAnd for int and LogicAnd for bool
	 */
	protected void replaceSubExpression(SymbolTable symtab) {
		// Nothing to do
	}

	/**
	 * Replace the sub-expression after we know the left-hand side type is 'bool'
	 * E.g.: '&=' uses BitAnd for int and LogicAnd for bool
	 */
	protected void replaceSubExpressionBool(SymbolTable symtab) {
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

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;

		super.returnType(symtab);
		returnType = left.getReturnType();
		replaceSubExpression(symtab);

		return returnType;
	}
}
