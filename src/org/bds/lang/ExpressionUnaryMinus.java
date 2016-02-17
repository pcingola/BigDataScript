package org.bds.lang;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessages;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.run.BdsThread;
import org.bds.scope.Scope;

/**
 * A arithmetic negation
 *
 * @author pcingola
 */
public class ExpressionUnaryMinus extends ExpressionUnary {

	public ExpressionUnaryMinus(BdsNode parent, ParseTree tree) {
		super(parent, tree);
		op = "-";
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		expr.returnType(scope);

		if (expr.canCastInt()) returnType = Type.INT;
		else if (expr.canCastReal()) returnType = Type.REAL;
		else throw new RuntimeException("Cannot cast to 'int' or 'real'. This should never happen!");

		return returnType;
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		bdsThread.run(expr);
		if (bdsThread.isCheckpointRecover()) return;

		if (returnType == Type.INT) {
			bdsThread.push(-popInt(bdsThread));
		} else if (returnType == Type.REAL) {
			bdsThread.push(-popReal(bdsThread));
		} else throw new RuntimeException("Cannot cast to 'int' or 'real'. This should never happen!");
	}

	@Override
	public void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		// Can we transform to an int?
		if (!expr.canCastInt() && !expr.canCastInt()) compilerMessages.add(this, "Cannot cast expression to int or real", MessageType.ERROR);
	}

}
