package org.bds.lang.type;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.value.Value;
import org.bds.run.BdsThread;
import org.bds.scope.Scope;
import org.bds.symbol.SymbolTable;

/**
 * A reference to any type of variable
 */
public abstract class Reference extends Expression {

	public Reference(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	public abstract Value getValue(Scope scope);

	public abstract String getVariableName();

	public boolean isConstant(SymbolTable symtab) {
		return false;
	}

	public abstract boolean isVariableReference(SymbolTable symtab);

	/**
	 * Parse a string (e.g. an interpolated string)
	 */
	public abstract void parse(String str);

	// public abstract void setValue(BdsThread bdsThread, Object value);

	public abstract void setValue(BdsThread bdsThread, Value value);

}
