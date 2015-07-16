package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;

/**
 * A mathematical expression (actual '+" can be used for other strings and other things)
 *
 * @author pcingola
 */
public class ExpressionMath extends ExpressionBinary {

	public ExpressionMath(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		super.returnType(scope);

		if (right == null) {
			if (left.canCastInt()) returnType = Type.INT;
			else if (left.canCastReal()) returnType = Type.REAL;
		} else {
			if (left.canCastInt() && right.canCastInt()) returnType = Type.INT;
			else if (left.canCastReal() && right.canCastReal()) returnType = Type.REAL;
		}

		return returnType;

	}
}
