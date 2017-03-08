package org.bds.lang;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.run.BdsThread;
import org.bds.scope.Scope;

/**
 * Pre increment / decrement operator
 *
 * E.g. :  --i or ++i
 *
 * @author pcingola
 */
public class Pre extends ExpressionUnary {

	PrePostOperation operation;

	public Pre(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		operation = PrePostOperation.parse(tree.getChild(0).getText());

		BdsNode node = factory(tree, 1);
		if ((node instanceof Reference)) expr = (Expression) node;
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
		if (operation == PrePostOperation.INCREMENT) value++;
		else if (operation == PrePostOperation.DECREMENT) value--;
		else throw new RuntimeException("Unknown operator " + operation);

		ref.setValue(bdsThread, value);
		bdsThread.push(value);
	}

	@Override
	protected void sanityCheck(CompilerMessages compilerMessages) {
		if (!(expr instanceof Reference)) compilerMessages.add(this, "Only variable reference can be used with ++ or -- operators", MessageType.ERROR);
	}

	@Override
	public String toString() {
		return expr.toString() + operation.toStringCode();
	}

	@Override
	protected void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		if (!expr.isInt()) compilerMessages.add(this, "Only int variables can be used with ++ or -- operators", MessageType.ERROR);
	}

}
