package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.symbol.SymbolTable;

/**
 * A Statement that requires a new Scope
 *
 * @author pcingola
 */
public class StatementWithScope extends Statement {

	private static final long serialVersionUID = -8182877722963875996L;

	protected SymbolTable symbolTable; // SymbolTable required for this statement
	protected boolean needsScope; // Do we really need a scope? If a scope is requested, but we don't add new symbols, then we don't really need it (e.g. while loop without any new variables)

	public StatementWithScope(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public SymbolTable getSymbolTable() {
		return symbolTable;
	}

	@Override
	protected void initialize() {
		needsScope = true; // A priory, we think we need a scope. This may be changed at type-checking
	}

	@Override
	public boolean isNeedsScope() {
		return needsScope;
	}

	@Override
	public void setNeedsScope(boolean needsScope) {
		this.needsScope = needsScope;
	}

	@Override
	public void setSymbolTable(SymbolTable symtab) {
		symbolTable = symtab;
	}

	@Override
	public void typeCheck(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Calculate return type
		returnType = returnType(symtab);

		// Are return types non-null?
		// Note: null returnTypes happen if variables are missing.
		if (isReturnTypesNotNull()) typeCheckNotNull(symtab, compilerMessages);
	}

}
