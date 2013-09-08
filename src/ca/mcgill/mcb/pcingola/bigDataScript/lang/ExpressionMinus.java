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
	public Object eval(BigDataScriptThread csThread) {
		if (right == null) {
			// This should be an unary expression!
			if (isInt()) return -left.evalInt(csThread);
			if (isReal()) return -left.evalReal(csThread);
		} else {
			if (isInt()) return left.evalInt(csThread) - right.evalInt(csThread);
			if (isReal()) return left.evalReal(csThread) - right.evalReal(csThread);
		}

		throw new RuntimeException("Unknown return type " + returnType + " for expression " + getClass().getSimpleName());
	}

	@Override
	public void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		left.checkCanCastIntOrReal(compilerMessages);
		if (right != null) right.checkCanCastIntOrReal(compilerMessages);
	}

}
