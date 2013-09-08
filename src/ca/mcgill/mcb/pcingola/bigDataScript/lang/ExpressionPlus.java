package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;

/**
 * A sum of two expressions
 * 
 * @author pcingola
 */
public class ExpressionPlus extends ExpressionMath {

	public ExpressionPlus(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public Object eval(BigDataScriptThread csThread) {
		if (isInt()) return left.evalInt(csThread) + right.evalInt(csThread);
		if (isReal()) return left.evalReal(csThread) + right.evalReal(csThread);
		if (isString()) return left.evalString(csThread) + right.evalString(csThread);
		throw new RuntimeException("Unknown return type " + returnType + " for expression " + getClass().getSimpleName());
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		super.returnType(scope);

		if (left.isString() || right.isString()) returnType = Type.STRING;
		else if (left.canCastInt() && right.canCastInt()) returnType = Type.INT;
		else if (left.canCastReal() && right.canCastReal()) returnType = Type.REAL;

		return returnType;
	}

	@Override
	public void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		// Either side is a string? => String plus String
		if (left.isString() || right.isString()) {
			// OK 
		} else {
			// Normal 'math'
			left.checkCanCastIntOrReal(compilerMessages);
			right.checkCanCastIntOrReal(compilerMessages);
		}
	}

}
