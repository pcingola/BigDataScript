package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;

/**
 * A arithmetic negation
 *
 * @author pcingola
 */
public class ExpressionUnaryMinus extends ExpressionUnary {

	public ExpressionUnaryMinus(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
		op = "-";
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void eval(BigDataScriptThread bdsThread) {
		if (returnType == Type.INT) {
			bdsThread.push(-expr.evalInt(bdsThread));
		} else if (returnType == Type.REAL) {
			bdsThread.push(-expr.evalReal(bdsThread));
		} else throw new RuntimeException("Cannot cast to 'int' or 'real'. This should never happen!");
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

	@Override
	public void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		// Can we transform to an int?
		if (!expr.canCastInt() && !expr.canCastInt()) compilerMessages.add(this, "Cannot cast expression to int or real", MessageType.ERROR);
	}

}
