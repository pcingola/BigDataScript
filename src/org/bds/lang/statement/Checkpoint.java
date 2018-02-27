package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.run.BdsThread;
import org.bds.scope.Scope;

/**
 * A "checkpoint" statement
 *
 * @author pcingola
 */
public class Checkpoint extends Statement {

	Expression expr;

	public Checkpoint(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		int idx = 0;
		if (isTerminal(tree, idx, "checkpoint")) idx++; // 'checkpoint'
		if (tree.getChildCount() > idx) expr = (Expression) factory(tree, idx);
	}

	/**
	 * Run the program
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		// Get filename
		String file = null;
		if (expr != null) {
			bdsThread.run(expr);
			file = bdsThread.popString();
		}

		// Do not create checkpoint file during recovery
		if (bdsThread.isCheckpointRecover()) return;

		if (file != null) bdsThread.checkpoint(file);
		else bdsThread.checkpoint(this);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName().toLowerCase() + expr + "\n";
	}

	@Override
	public void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		if (expr != null) {
			expr.returnType(scope);
			if (!expr.getReturnType().isString()) compilerMessages.add(this, "Checkpoint argument should be a string (file name)", MessageType.ERROR);
		}
	}
}
