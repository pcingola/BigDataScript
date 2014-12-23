package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;

/**
 * A subtraction
 *
 * @author pcingola
 */
public class ExpressionMinus extends ExpressionMath {

	public ExpressionMinus(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public void runStep(BigDataScriptThread bdsThread) {
		if (right == null) {
			left.run(bdsThread);

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
			left.run(bdsThread);
			right.run(bdsThread);

			if (isInt()) {
				long tr = popInt(bdsThread);
				long tl = popInt(bdsThread);
				bdsThread.push(tl - tr);
				return;
			}

			if (isReal()) {
				double tr = popInt(bdsThread);
				double tl = popInt(bdsThread);
				bdsThread.push(tl - tr);
				return;
			}
		}

		throw new RuntimeException("Unknown return type " + returnType + " for expression " + getClass().getSimpleName());
	}

	@Override
	protected String op() {
		return "-";
	}

	@Override
	public void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		left.checkCanCastIntOrReal(compilerMessages);
		if (right != null) right.checkCanCastIntOrReal(compilerMessages);
	}

}
