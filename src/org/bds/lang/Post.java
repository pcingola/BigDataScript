package org.bds.lang;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.expression.Expression;
import org.bds.run.BdsThread;

/**
 * Post increment / decrement operator
 *
 * E.g. :  i++ or i--
 *
 * @author pcingola
 */
public class Post extends Pre {

	public Post(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		BdsNode node = factory(tree, 0);
		if (node instanceof Reference) expr = (Expression) node;

		operation = PrePostOperation.parse(tree.getChild(1).getText());
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		Reference ref = (Reference) expr;
		bdsThread.run(ref);

		if (bdsThread.isCheckpointRecover()) return;

		long value = bdsThread.popInt();
		if (operation == PrePostOperation.INCREMENT) ref.setValue(bdsThread, value + 1);
		else if (operation == PrePostOperation.DECREMENT) ref.setValue(bdsThread, value - 1);
		else throw new RuntimeException("Unknown operator " + operation);

		bdsThread.push(value);
	}

	@Override
	public String toString() {
		return expr.toString() + operation.toStringCode();
	}

}
