package org.bds.lang.value;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.type.Type;
import org.bds.symbol.SymbolTable;

/**
 * A boolean literal
 *
 * @author pcingola
 */
public abstract class Literal extends Expression {

	private static final long serialVersionUID = 5099898775266921617L;

	protected Value value;

	public Literal(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	abstract public Type getReturnType();

	public Value getValue() {
		return value;
	}

	@Override
	protected void parse(ParseTree tree) {
		setValue(parseValue(tree));
	}

	protected abstract Value parseValue(ParseTree tree);

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;
		returnType = value.getType();
		return returnType;
	}

	public void setValue(Value v) {
		value = v;
	}

	@Override
	public String toString() {
		return value.toString();
	}

	@Override
	protected void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Nothing to do
	}

}
