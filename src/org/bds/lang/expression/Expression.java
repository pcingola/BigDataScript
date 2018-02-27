package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.Config;
import org.bds.lang.BdsNode;
import org.bds.lang.statement.Statement;
import org.bds.run.BdsThread;

/**
 * Expression: A statement that returns a value
 *
 * @author pcingola
 */
public class Expression extends Statement {

	public Expression(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public boolean isStopDebug() {
		return false;
	}

	/**
	 * Run an expression (as a statement): I.e. evaluate the expression
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		try {
			bdsThread.run(this);
			bdsThread.pop();
		} catch (Throwable t) {
			if (Config.get().isDebug()) t.printStackTrace();
			bdsThread.fatalError(this, t);
		}
	}
}
