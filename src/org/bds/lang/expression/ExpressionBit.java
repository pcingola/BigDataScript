package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.scope.Scope;

/**
 * A bitwise expression
 * 
 * @author pcingola
 */
public class ExpressionBit extends ExpressionBinary {

	public ExpressionBit(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		super.returnType(scope);
		returnType = Types.INT;

		return returnType;
	}

	@Override
	public void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		// Can we transform 'left' into an int?
		left.getReturnType().checkCanCast(Types.INT, compilerMessages);
		right.getReturnType().checkCanCast(Types.INT, compilerMessages);
	}

}
