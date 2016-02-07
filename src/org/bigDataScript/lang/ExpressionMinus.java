package org.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bigDataScript.compile.CompilerMessages;
import org.bigDataScript.run.BdsThread;
import org.bigDataScript.scope.Scope;

/**
 * A subtraction
 *
 * @author pcingola
 */
public class ExpressionMinus extends ExpressionMath {

	public ExpressionMinus(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected String op() {
		return "-";
	}

	@Override
	public void runStep(BdsThread bdsThread) {
		if (right == null) {
			// Unary minus operator
			bdsThread.run(left);
			if (bdsThread.isCheckpointRecover()) return;

			// This should be an unary expression!
			if (isInt()) {
				bdsThread.push(-popInt(bdsThread));
				return;
			}

			if (isReal()) {
				bdsThread.push(-popReal(bdsThread));
				return;
			}
		} else {
			// Binary minus operator: Subtraction
			bdsThread.run(left);
			bdsThread.run(right);
			if (bdsThread.isCheckpointRecover()) return;

			Object rval = bdsThread.pop();
			Object lval = bdsThread.pop();

			if (isInt()) {
				bdsThread.push(((long) lval) - ((long) rval));
				return;
			} else if (isReal()) {
				bdsThread.push(((double) lval) - ((double) rval));
				return;
			}

		}

		throw new RuntimeException("Unknown return type " + returnType + " for expression " + getClass().getSimpleName());
	}

	@Override
	public void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		left.checkCanCastIntOrReal(compilerMessages);
		if (right != null) right.checkCanCastIntOrReal(compilerMessages);
	}

}
