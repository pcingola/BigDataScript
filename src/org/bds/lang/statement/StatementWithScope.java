package org.bds.lang.statement;

import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.BdsNodeWalker;
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

	/**
	 * Add local symbols to SymbolTable
	 * The idea is that you should be able to refer to functions
	 * and classes defined within the same scope, which may be defined
	 * after the current statement, e.g.:
	 *   i := f(42)    // Function 'f' is not defined yet
	 *   int f(int x) { return 2*x }
	 */
	public void addLocalSymbols(SymbolTable symtab) {
		// Add all functions
		List<BdsNode> fdecls = BdsNodeWalker.findNodes(this, StatementFunctionDeclaration.class, false, true);
		for (BdsNode n : fdecls)
			symtab.addFunction((FunctionDeclaration) n);
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
		addLocalSymbols(symtab);

		// Calculate return type
		returnType = returnType(symtab);

		// Are return types non-null?
		// Note: null returnTypes happen if variables are missing.
		if (isReturnTypesNotNull()) typeCheckNotNull(symtab, compilerMessages);
	}

}
