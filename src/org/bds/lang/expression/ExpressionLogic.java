package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.PrimitiveType;
import org.bds.lang.Type;
import org.bds.scope.Scope;

/**
 * Boolean expression
 * 
 * @author pcingola
 */
public class ExpressionLogic extends ExpressionBinary {

	public ExpressionLogic(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		super.returnType(scope);
		returnType = Type.get(PrimitiveType.BOOL);

		return returnType;

	}

	@Override
	public void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		// Can we transform 'left' into an int?
		left.checkCanCastBool(compilerMessages);
		right.checkCanCastBool(compilerMessages);
	}

}
