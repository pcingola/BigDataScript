package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;

/**
 * Expression
 *
 * @author pcingola
 */
public abstract class ExpressionAssignmentBinary extends ExpressionAssignment {

	public ExpressionAssignmentBinary(BigDataScriptNode parent, ParseTree tree) {
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
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		super.returnType(scope);
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
		Object value = bdsThread.peek();

		// Assign value
		if (left instanceof Reference) ((Reference) left).setValue(bdsThread, value);
		else throw new RuntimeException("Unimplemented assignment evaluation for type " + left.getReturnType());
	}

	@Override
	protected void sanityCheck(CompilerMessages compilerMessages) {
		// Is 'left' a variable?
		if (!(left instanceof Reference)) compilerMessages.add(this, "Assignment to non variable", MessageType.ERROR);
	}

	@Override
	public void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		// Trying to assign to a constant?
		if (((Reference) left).isConstant(scope)) compilerMessages.add(this, "Cannot assign to constant '" + left + "'", MessageType.ERROR);

		// Can we cast 'right type' into 'left type'?
		if (left.isList() && right.isList() && right instanceof LiteralListEmpty) {
			// OK, empty list can be assigned to any list
		} else if (left.isMap() && right.isMap() && right instanceof LiteralMapEmpty) {
			// OK, empty map can be assigned to any map
		} else if (!right.getReturnType().canCast(left.getReturnType())) {
			compilerMessages.add(this, "Cannot cast " + right.getReturnType() + " to " + left.getReturnType(), MessageType.ERROR);
		}
	}

}
