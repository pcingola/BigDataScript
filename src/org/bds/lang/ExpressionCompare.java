package org.bds.lang;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessages;
import org.bds.run.BdsThread;
import org.bds.scope.Scope;

/**
 * A comparison expression
 *
 * @author pcingola
 */
public abstract class ExpressionCompare extends ExpressionBinary {

	public ExpressionCompare(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	protected abstract boolean cmp(boolean a, boolean b);

	protected abstract boolean cmp(double a, double b);

	protected abstract boolean cmp(long a, long b);

	protected abstract boolean cmp(String a, String b);

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		super.returnType(scope);
		returnType = Type.BOOL;

		return returnType;

	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		bdsThread.run(left);
		bdsThread.run(right);

		if (bdsThread.isCheckpointRecover()) return;

		Object rval = bdsThread.pop();
		Object lval = bdsThread.pop();

		if (left.isNumeric() && right.isNumeric()) {

			// Both are numeric types
			if (left.isReal() || right.isReal()) bdsThread.push(cmp((double) Type.REAL.cast(lval), (double) Type.REAL.cast(rval)));
			else if (left.isInt() || right.isInt()) bdsThread.push(cmp((long) Type.INT.cast(lval), (long) Type.INT.cast(rval)));
			else if (left.isBool() || right.isBool()) bdsThread.push(cmp((boolean) lval, (boolean) rval));
			else throw new RuntimeException("Unknown return type " + returnType + " for expression " + getClass().getSimpleName() + "( " + lval + " , " + rval + " )");

		} else if (left.isString() || right.isString()) bdsThread.push(cmp(lval.toString(), rval.toString()));
		else throw new RuntimeException("Unknown return type " + returnType + " for expression " + getClass().getSimpleName());
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
