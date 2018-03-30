package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.run.BdsThread;

/**
 * Boolean AND
 *
 * @author pcingola
 */
public class ExpressionLogicAnd extends ExpressionLogic {

	private static final long serialVersionUID = 2229407346097707469L;

	public ExpressionLogicAnd(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected String op() {
		return "&&";
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		bdsThread.run(left);

		if (!bdsThread.isCheckpointRecover()) {
			boolean ok = bdsThread.peek().asBool();

			// Already false? No need to evaluate the other expression
			if (!ok) return;
		}

		// 'AND' only depends on 'right' result (left was true)
		bdsThread.pop(); // Remove 'left' result from stack

		bdsThread.run(right);
	}

	@Override
	public String toAsm() {
		String eb = super.toAsm();
		String op = "andb";
		return eb + op + "\n";
	}
}
