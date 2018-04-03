package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Reference;
import org.bds.lang.value.ValueInt;
import org.bds.run.BdsThread;
import org.bds.symbol.SymbolTable;

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

		ValueInt value = (ValueInt) bdsThread.pop();
		ValueInt newValue;
		if (operation == PrePostOperation.INCREMENT) newValue = new ValueInt(value.asInt() + 1);
		else if (operation == PrePostOperation.DECREMENT) newValue = new ValueInt(value.asInt() - 1);
		else throw new RuntimeException("Unknown operator " + operation);

		ref.setValue(bdsThread, newValue);
		bdsThread.push(newValue);
	}

	@Override
	public void sanityCheck(CompilerMessages compilerMessages) {
		if (!(expr instanceof Reference)) compilerMessages.add(this, "Only variable reference can be used with ++ or -- operators", MessageType.ERROR);
	}

	@Override
	public String toString() {
		return expr.toString() + operation.toStringCode();
	}

	@Override
	protected void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		if (!expr.isInt()) compilerMessages.add(this, "Only int variables can be used with ++ or -- operators", MessageType.ERROR);
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public String toAsm() {
		//		Reference ref = (Reference) expr;
		//		bdsThread.run(ref);
		//
		//		if (bdsThread.isCheckpointRecover()) return;
		//
		//		ValueInt value = (ValueInt) bdsThread.pop();
		//		ValueInt newValue;
		//		if (operation == PrePostOperation.INCREMENT) newValue = new ValueInt(value.asInt() + 1);
		//		else if (operation == PrePostOperation.DECREMENT) newValue = new ValueInt(value.asInt() - 1);
		//		else throw new RuntimeException("Unknown operator " + operation);
		//
		//		ref.setValue(bdsThread, newValue);
		//		bdsThread.push(newValue);
		return "";
	}

}
