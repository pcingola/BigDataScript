package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.value.Value;
import org.bds.scope.Scope;
import org.bds.symbol.SymbolTable;

/**
 * A reference to any type of variable
 */
public abstract class Reference extends Expression {

	private static final long serialVersionUID = -8617102495338919053L;

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

	/**
	 * Assembly code to set the referenced value
	 */
	public abstract String toAsmSet();

}
