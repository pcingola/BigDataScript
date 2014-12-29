package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

/**
 * Post increment / decrement operator
 *
 * E.g. :  i++ or i--
 *
 * @author pcingola
 */
public class Post extends Pre {

	public Post(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		BigDataScriptNode node = factory(tree, 0);
		if (node instanceof Reference) expr = (Expression) node;

		operation = PrePostOperation.parse(tree.getChild(1).getText());
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public void runStep(BigDataScriptThread bdsThread) {
		Reference ref = (Reference) expr;
		bdsThread.run(ref);
		
		if (bdsThread.isCheckpointRecover()) return;

		long value = popInt(bdsThread);
		if (operation == PrePostOperation.INCREMENT) ref.setValue(bdsThread, value + 1);
		else if (operation == PrePostOperation.DECREMENT) ref.setValue(bdsThread, value - 1);
		else throw new RuntimeException("Unknown operator " + operation);

		bdsThread.push(value);
	}

	@Override
	public String toString() {
		return operation.toStringCode() + expr.toString();
	}

}
