package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;

/**
 * A multiplication
 *
 * @author pcingola
 */
public class ExpressionTimes extends ExpressionMath {

	public ExpressionTimes(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void eval(BigDataScriptThread bdsThread) {
		// Evaliate expressions
		left.eval(bdsThread);
		right.eval(bdsThread);

		// Combine results
		if (isInt()) {
			bdsThread.push(left.popInt(bdsThread) * right.popInt(bdsThread));
			return;
		}

		if (isReal()) {
			bdsThread.push(left.popReal(bdsThread) * right.popReal(bdsThread));
			return;
		}

		if (isString()) {
			// string * int : Get number and string
			String str = "";
			long num = 0;
			if (left.canCastInt()) {
				num = left.popInt(bdsThread);
				str = right.popString(bdsThread);
			} else if (right.canCastInt()) {
				str = left.popString(bdsThread);
				num = right.popInt(bdsThread);
			} else throw new RuntimeException("Neither is an int. This should never happen!");

			// Multiply (append the same string num times
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < num; i++)
				sb.append(str);

			bdsThread.push(sb.toString());
			return;
		}

		throw new RuntimeException("Unknown return type " + returnType + " for expression " + getClass().getSimpleName());
	}

	@Override
	protected String op() {
		return "*";
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		super.returnType(scope);

		if (isReturnTypesNotNull()) {
			if (left.isString() || right.isString()) returnType = Type.STRING;
			else if (left.canCastInt() && right.canCastInt()) returnType = Type.INT;
			else if (left.canCastReal() && right.canCastReal()) returnType = Type.REAL;
		}
		return returnType;
	}

	@Override
	public void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		if (left.isString() && right.canCastInt()) {
			// string * int: OK
		} else if (left.canCastInt() && right.isString()) {
			// int * string : OK
		} else {
			// Normal 'math'
			left.checkCanCastIntOrReal(compilerMessages);
			right.checkCanCastIntOrReal(compilerMessages);
		}
	}

}
