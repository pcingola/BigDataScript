package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.scope.Scope;

/**
 * A mathematical expression (actual '+" can be used for other strings and other things)
 *
 * @author pcingola
 */
public class ExpressionMath extends ExpressionBinary {

	public ExpressionMath(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		super.returnType(scope);

		if (right == null) {
			if (left.canCastToInt()) returnType = Types.INT;
			else if (left.canCastToReal()) returnType = Types.REAL;
		} else {
			if (left.canCastToInt() && right.canCastToInt()) returnType = Types.INT;
			else if (left.canCastToReal() && right.canCastToReal()) returnType = Types.REAL;
		}

		return returnType;

	}
}
