package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;

/**
 * A division
 *
 * @author pcingola
 */
public class ExpressionDivide extends ExpressionMath {

	public ExpressionDivide(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void eval(BigDataScriptThread bdsThread) {
		if (isInt()) {
			bdsThread.push(left.evalInt(bdsThread) / right.evalInt(bdsThread));
			return;
		}

		if (isReal()) {
			bdsThread.push(left.evalReal(bdsThread) / right.evalReal(bdsThread));
			return;
		}

		throw new RuntimeException("Unknown return type " + returnType + " for expression " + getClass().getSimpleName());
	}

	@Override
	protected String op() {
		return "/";
	}

	@Override
	public void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		left.checkCanCastIntOrReal(compilerMessages);
		right.checkCanCastIntOrReal(compilerMessages);
	}

}
