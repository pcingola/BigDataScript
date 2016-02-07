package org.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bigDataScript.compile.CompilerMessages;
import org.bigDataScript.run.BdsThread;
import org.bigDataScript.scope.Scope;

/**
 * Logic negation
 *
 * @author pcingola
 */
public class ExpressionLogicNot extends ExpressionUnary {

	public ExpressionLogicNot(BdsNode parent, ParseTree tree) {
		super(parent, tree);
		op = "!";
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		bdsThread.run(expr);
		if (bdsThread.isCheckpointRecover()) return;
		bdsThread.push(!popBool(bdsThread));
	}

	@Override
	public void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		// Can we transform to bool?
		expr.checkCanCastBool(compilerMessages);
	}

}
