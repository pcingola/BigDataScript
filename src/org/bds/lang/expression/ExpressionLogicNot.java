package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.run.BdsThread;
import org.bds.scope.Scope;

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
		bdsThread.push(!bdsThread.popBool());
	}

	@Override
	public void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		// Can we transform to bool?
		expr.checkCanCastBool(compilerMessages);
	}

}
