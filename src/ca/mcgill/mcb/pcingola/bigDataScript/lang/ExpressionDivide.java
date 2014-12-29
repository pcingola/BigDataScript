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

	@Override
	protected String op() {
		return "/";
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void runStep(BigDataScriptThread bdsThread) {
		bdsThread.run(left);
		bdsThread.run(right);
		if (bdsThread.isCheckpointRecover()) return;

		if (isInt()) {
			long den = popInt(bdsThread);
			long num = popInt(bdsThread);
			bdsThread.push(num / den);
			return;
		}

		if (isReal()) {
			double den = popInt(bdsThread);
			double num = popInt(bdsThread);
			bdsThread.push(num / den);
			return;
		}

		throw new RuntimeException("Unknown return type " + returnType + " for expression " + getClass().getSimpleName());
	}

	@Override
	public void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		left.checkCanCastIntOrReal(compilerMessages);
		right.checkCanCastIntOrReal(compilerMessages);
	}

}
