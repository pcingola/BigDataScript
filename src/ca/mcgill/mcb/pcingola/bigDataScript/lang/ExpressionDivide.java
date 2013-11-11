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

	/**
	 * Evaluate an expression
	 */
	@Override
	public Object eval(BigDataScriptThread csThread) {
		if (isInt()) return left.evalInt(csThread) / right.evalInt(csThread);
		if (isReal()) return left.evalReal(csThread) / right.evalReal(csThread);
		throw new RuntimeException("Unknown return type " + returnType + " for expression " + getClass().getSimpleName());
	}

	@Override
	public void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		left.checkCanCastIntOrReal(compilerMessages);
		right.checkCanCastIntOrReal(compilerMessages);
	}

	@Override
	protected String op() {
		return "/";
	}

}
