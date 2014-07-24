package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;

/**
 * Logic negation
 *
 * @author pcingola
 */
public class ExpressionLogicNot extends ExpressionUnary {

	public ExpressionLogicNot(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
		op = "!";
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public Object eval(BigDataScriptThread csThread) {
		return !expr.evalBool(csThread);
	}

	@Override
	public void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		// Can we transform to bool?
		expr.checkCanCastBool(compilerMessages);
	}

}
