package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.symbol.SymbolTable;

/**
 * Expression: An expression that requires a new scope
 *
 * @author pcingola
 */
public class ExpressionWithScope extends Expression {

	private static final long serialVersionUID = 7598998758400573217L;

	boolean needsScope; // Do we really need a scope?
	protected SymbolTable symbolTable; // SymbolTable required for this statement

	public ExpressionWithScope(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public SymbolTable getSymbolTable() {
		return symbolTable;
	}

	@Override
	protected void initialize() {
		needsScope = true; // At priory, we think we need a scope. This may be changed at type-checking
	}

	@Override
	public boolean isNeedsScope() {
		return needsScope;
	}

	//	@Override
	//	public boolean isStopDebug() {
	//		return false;
	//	}

	@Override
	public void setNeedsScope(boolean needsScope) {
		this.needsScope = needsScope;
	}

	@Override
	public void setSymbolTable(SymbolTable symtab) {
		symbolTable = symtab;
	}

}
