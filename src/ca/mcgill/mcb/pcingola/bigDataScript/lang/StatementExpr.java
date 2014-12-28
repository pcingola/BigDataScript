package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

/**
 * A statement that is actually an expression.
 * E.g.:
 *        i++   # This statement is a line of code with only one expression ('i++')
 *
 * @author pcingola
 */
public class StatementExpr extends ExpressionWrapper {

	public StatementExpr(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public void runStep(BigDataScriptThread bdsThread) {
		bdsThread.run(expression);

		// This is an expression in form of a statement, so
		// we remove the result from the stack (to avoid filling up
		// the stack)
		bdsThread.pop();
	}

}
