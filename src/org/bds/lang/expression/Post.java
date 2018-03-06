package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Reference;
import org.bds.lang.value.ValueInt;
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

		ValueInt value = (ValueInt) bdsThread.pop();
		ValueInt newValue;
		if (operation == PrePostOperation.INCREMENT) newValue = new ValueInt(value.get() + 1);
		else if (operation == PrePostOperation.DECREMENT) newValue = new ValueInt(value.get() - 1);
		else throw new RuntimeException("Unknown operator " + operation);

		ref.setValue(bdsThread, newValue);
		bdsThread.push(value);
	}

	@Override
	public String toString() {
		return expr.toString() + operation.toStringCode();
	}

}
