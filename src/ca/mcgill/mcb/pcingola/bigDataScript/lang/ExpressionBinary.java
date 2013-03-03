package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;

/**
 * A binary expression
 * 
 * @author pcingola
 */
public class ExpressionBinary extends Expression {

	Expression left;
	Expression right;

	public ExpressionBinary(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected boolean isReturnTypesNotNull() {
		if (right == null) return (left.getReturnType() != null);
		return (left.getReturnType() != null) && (right.getReturnType() != null);
	}

	@Override
	protected void parse(ParseTree tree) {
		left = (Expression) factory(tree, 0);
		right = (Expression) factory(tree, 2); // Child 1 has the operator, we use child 2 here
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		returnType = left.returnType(scope);
		if (right != null) right.returnType(scope); // Only assign this to show that calculation was already performed

		return returnType;
	}

}
