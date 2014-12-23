package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;

/**
 * A module operation
 *
 * @author pcingola
 */
public class ExpressionModulo extends ExpressionMath {

	public ExpressionModulo(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void eval(BigDataScriptThread bdsThread) {
		left.eval(bdsThread);
		right.eval(bdsThread);
		long den = popInt(bdsThread);
		long num = popInt(bdsThread);
		bdsThread.push(num % den);
	}

	@Override
	protected String op() {
		return "%";
	}

	@Override
	public void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		left.checkCanCastInt(compilerMessages);
		right.checkCanCastInt(compilerMessages);
	}

}
