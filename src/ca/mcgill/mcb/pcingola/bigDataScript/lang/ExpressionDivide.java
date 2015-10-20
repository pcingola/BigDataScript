package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;

/**
 * A division
 *
 * @author pcingola
 */
public class ExpressionDivide extends ExpressionMath {

	public ExpressionDivide(BdsNode parent, ParseTree tree) {
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
	public void runStep(BdsThread bdsThread) {
		bdsThread.run(left);
		bdsThread.run(right);
		if (bdsThread.isCheckpointRecover()) return;

		Object rval = bdsThread.pop();
		Object lval = bdsThread.pop();

		if (isInt()) {
			bdsThread.push(((long) lval) / ((long) rval));
			return;
		} else if (isReal()) {
			bdsThread.push(((double) lval) / ((double) rval));
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
