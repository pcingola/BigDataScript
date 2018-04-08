package org.bds.lang.statement;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.symbol.SymbolTable;

/**
 * Variable declaration
 *
 * @author pcingola
 */
public class FieldDeclaration extends VarDeclaration {

	private static final long serialVersionUID = 3290141813931068716L;

	public FieldDeclaration(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Add variable to symbol table
	 */
	@Override
	protected void addVar(SymbolTable symtab, CompilerMessages compilerMessages, String varName) {
		// Fields are added during class parsing. Nothing to do here
	}

	@Override
	protected void parse(ParseTree tree) {
		super.parse(tree.getChild(0));
	}

	@Override
	public void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		// This is checked during ClassDeclaration. Nothing to do here
	}

}
