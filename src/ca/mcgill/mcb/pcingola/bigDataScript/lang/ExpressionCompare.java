package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;

/**
 * A comparison expression
 *
 * @author pcingola
 */
public class ExpressionCompare extends ExpressionBinary {

	public ExpressionCompare(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	boolean cmp(double a, double b) {
		throw new RuntimeException("This method should never be invoked!");
	}

	boolean cmp(long a, long b) {
		throw new RuntimeException("This method should never be invoked!");
	}

	boolean cmp(String a, String b) {
		throw new RuntimeException("This method should never be invoked!");
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void runStep(BigDataScriptThread bdsThread) {
		left.run(bdsThread);
		right.run(bdsThread);

		Object rval = bdsThread.pop();
		Object lval = bdsThread.pop();

		if (left.isInt() && right.isInt()) bdsThread.push(cmp((long) lval, (long) rval));
		else if (left.isReal() || right.isReal()) bdsThread.push(cmp((double) lval, (double) rval));
		else if (left.isString() || right.isString()) bdsThread.push(cmp(lval.toString(), rval.toString()));
		else throw new RuntimeException("Unknown return type " + returnType + " for expression " + getClass().getSimpleName());
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		super.returnType(scope);
		returnType = Type.BOOL;

		return returnType;

	}

	@Override
	public void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		// Either side is a string? => String plus String
		if (left.isString() || right.isString()) {
			// OK, convert to string
		} else {
			// Numbers
			left.checkCanCastIntOrReal(compilerMessages);
			right.checkCanCastIntOrReal(compilerMessages);
		}
	}
}
