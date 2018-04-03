package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Reference;
import org.bds.lang.type.Type;
import org.bds.lang.value.Value;
import org.bds.run.BdsThread;
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

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;

		super.returnType(symtab);
		returnType = left.getReturnType();

		return returnType;
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		// Get value
		bdsThread.run(right);
		Value value = bdsThread.peek();

		// Assign variable
		if (left instanceof Reference) ((Reference) left).setValue(bdsThread, value);
		else throw new RuntimeException("Unimplemented assignment evaluation for type " + left.getReturnType());
	}

	@Override
	public String toAsm() {
		StringBuilder sb = new StringBuilder();
		
	!!!!!!!!!!	
			return super.toAsm() //
					+ right.toAsm() //
					+ ((Reference) left).toAsmSet() //
			;

		//		// Get value
		//		bdsThread.run(right);
		//		Value value = bdsThread.peek();
		//
		//		// Assign variable
		//		if (left instanceof Reference) ((Reference) left).setValue(bdsThread, value);
		//		else throw new RuntimeException("Unimplemented assignment evaluation for type " + left.getReturnType());
		return sb.toString();
	}

}
