package org.bds.lang.nativeClasses;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.statement.ClassDeclaration;
import org.bds.lang.statement.FieldDeclaration;
import org.bds.lang.statement.MethodDeclaration;
import org.bds.lang.statement.VariableInit;
import org.bds.lang.type.Type;
import org.bds.symbol.SymbolTable;

/**
 * A native class declaration.
 * A class is declared natively using this object, which is fed to a 'TypeClass'
 */
public abstract class ClassDeclarationNative extends ClassDeclaration {

	private static final long serialVersionUID = -831104482356629903L;

	public ClassDeclarationNative() {
		this(null, null);
	}

	public ClassDeclarationNative(BdsNode parent, ParseTree tree) {
		super(parent, tree);
		initNativeClass();
	}

	/**
	 * Add symbols to type symbol table
	 */
	public void addToTypeSymbolTable() {
		SymbolTable typeSymTab = getType().getSymbolTable();

		// Add all fields
		for (FieldDeclaration fd : getFieldDecl()) {
			Type type = fd.getType();
			for (VariableInit vi : fd.getVarInit()) {
				typeSymTab.addVariable(vi.getVarName(), type);
			}
		}

		// Add methods
		for (MethodDeclaration md : getMethodDecl())
			typeSymTab.addFunction(md);
	}

	/**
	 * Create class fields
	 */
	protected abstract FieldDeclaration[] createFields();

	/**
	 * Create class methods
	 */
	protected abstract MethodDeclaration[] createMethods();

	protected void initNativeClass() {
		classNameParent = null;
		classParent = null;

		fieldDecl = createFields();
		methodDecl = createMethods();

		addThisArgToMethods(); // Add 'this' argument to all method declarations
		addToTypeSymbolTable(); // Add all methods to TypeClass' symbol table
	}

}
