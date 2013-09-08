package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.RunState;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;

/**
 * A "checkpoint" statement
 * 
 * @author pcingola
 */
public class Checkpoint extends Statement {

	Expression expr;

	public Checkpoint(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		// child[0] = 'checkpoint'
		if (tree.getChildCount() > 1) expr = (Expression) factory(tree, 1);
	}

	/**
	 * Run the program
	 */
	@Override
	protected RunState runStep(BigDataScriptThread csThread) {
		// Get filename
		String file = null;
		if (expr != null) file = expr.evalString(csThread);

		// Save checkpoint
		csThread.checkpoint(file);

		return RunState.OK;
	}

	@Override
	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		if (expr != null) {
			expr.returnType(scope);
			if (!expr.getReturnType().isString()) compilerMessages.add(this, "Checkpoint argument should be a string (file name)", MessageType.ERROR);
		}
	}
}
