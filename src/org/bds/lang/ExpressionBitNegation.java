package org.bds.lang;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessages;
import org.bds.run.BdsThread;
import org.bds.scope.Scope;

/**
 * A bitwise negation
 *
 * @author pcingola
 */
public class ExpressionBitNegation extends ExpressionUnary {

	public ExpressionBitNegation(BdsNode parent, ParseTree tree) {
		super(parent, tree);
		op = "~";
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		bdsThread.run(expr);
		if (bdsThread.isCheckpointRecover()) return;
		bdsThread.push(~popInt(bdsThread));
	}

	@Override
	public void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		// Can we transform to an int?
		expr.checkCanCastInt(compilerMessages);
	}

}
