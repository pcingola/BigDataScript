package org.bds.lang.value;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;
import org.bds.symbol.SymbolTable;

/**
 * Expression 'Literal'
 *
 * @author pcingola
 */
public class LiteralNull extends Literal {

	public LiteralNull(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		// Nothing to do
	}

	@Override
	protected Object parseValue(ParseTree tree) {
		return null;
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		return Types.NULL;
	}

	@Override
	public void runStep(BdsThread bdsThread) {
		bdsThread.push(Value.NULL); // Push 'null'
	}

	@Override
	public String toString() {
		return "null";
	}

}
