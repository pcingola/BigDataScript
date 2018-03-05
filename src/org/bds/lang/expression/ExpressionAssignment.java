package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Reference;
import org.bds.lang.type.Type;
import org.bds.lang.value.Value;
import org.bds.run.BdsThread;
import org.bds.scope.Scope;

/**
 * Assign one variable to another one
 *
 * Example:
 *     a = b
 *
 * @author pcingola
 */
public class ExpressionAssignment extends ExpressionBinary {

	public ExpressionAssignment(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected String op() {
		return "=";
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
		// Get map
		bdsThread.run(right);
		Value value = bdsThread.pop();

		if (left instanceof Reference) ((Reference) left).setValue(bdsThread, value);
		else throw new RuntimeException("Unimplemented assignment evaluation for type " + left.getReturnType());

		bdsThread.push(value);
	}

	@Override
	public void sanityCheck(CompilerMessages compilerMessages) {
		// Is 'left' a variable?
		if (left == null) compilerMessages.add(this, "Cannot parse left expresison.", MessageType.ERROR);
		else if (!(left instanceof Reference)) compilerMessages.add(this, "Assignment to non variable ('" + left + "')", MessageType.ERROR);
	}

	@Override
	public void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		// Trying to assign to a constant?
		Reference rleft = ((Reference) left);
		if (rleft.isConstant(scope)) compilerMessages.add(this, "Cannot assign to constant '" + left + "'", MessageType.ERROR);
		if (!rleft.isVariable(scope)) compilerMessages.add(this, "Cannot assign to non-variable '" + left + "'", MessageType.ERROR);

		// Can we cast 'right type' into 'left type'?
		if (!right.canCastTo(left)) {
			compilerMessages.add(this, "Cannot cast " + right.getReturnType() + " to " + left.getReturnType(), MessageType.ERROR);
		}
	}

}
