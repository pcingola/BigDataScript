package org.bds.lang.statement;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.nativeMethods.MethodDefaultConstructor;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeClass;
import org.bds.run.BdsThread;
import org.bds.symbol.SymbolTable;

/**
 * Variable declaration
 *
 * @author pcingola
 */
public class ClassDeclaration extends Block {

	public static final String THIS = "this";

	String className, extendsName;
	protected FieldDeclaration fieldDecl[];
	protected MethodDeclaration methodDecl[];
	protected ClassDeclaration classParent;
	protected TypeClass classType;
	protected TypeClass extendsClass;

	public ClassDeclaration(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Add symbols to symbol table
	 */
	protected void addSymTab(SymbolTable symtab) {
		// Add type for 'this' object in current table
		symtab.add(THIS, getType());

		// Add to parent symbol table, because the current
		// symbol table is for the class' body
		SymbolTable stparen = symtab.getParent();
		stparen.add(className, getType());
	}

	/**
	 * Add 'this' argument to all method declarations
	 */
	protected void addThisArgToMethods() {
		for (MethodDeclaration md : methodDecl) {
			md.addThisArg(getType());
		}
	}

	/**
	 * Default constructor (if none is provided in the program)
	 */
	protected MethodDeclaration defaultConstructor() {
		return new MethodDefaultConstructor(getType());
	}

	public String getClassName() {
		return className;
	}

	public ClassDeclaration getClassParent() {
		return classParent;
	}

	public String getExtendsName() {
		return extendsName;
	}

	public FieldDeclaration[] getFieldDecl() {
		return fieldDecl;
	}

	public MethodDeclaration[] getMethodDecl() {
		return methodDecl;
	}

	/**
	 * Get this class type
	 * Note: We use 'returnType' for storing the
	 */
	public TypeClass getType() {
		if (classType == null) classType = new TypeClass(this);
		return classType;
	}

	/**
	 * Any (declared) constructors?
	 */
	protected boolean hasConstructor(List<MethodDeclaration> lmd) {
		for (MethodDeclaration md : lmd)
			if (isConstructor(md)) return true;
		return false;
	}

	/**
	 * Is it a constructor method?
	 */
	protected boolean isConstructor(FunctionDeclaration fd) {
		return fd.getFunctionName().equals(className);
	}

	@Override
	public boolean isStopDebug() {
		return false;
	}

	@Override
	protected void parse(ParseTree tree) {
		tree = tree.getChild(0);
		int idx = 0;

		// Class name
		if (isTerminal(tree, idx, "class")) idx++; // 'class'
		className = tree.getChild(idx++).getText();

		// Extends?
		if (isTerminal(tree, idx, "extends")) {
			idx++; // 'extends'
			extendsName = tree.getChild(idx++).getText();
		}

		// Class body
		if (isTerminal(tree, idx, "{")) {
			parse(tree, ++idx);
			parseSortStatements();
		}

		// Set VarInit as 'field' initialization
		for (FieldDeclaration fd : fieldDecl)
			for (VariableInit vi : fd.getVarInit())
				vi.setFieldInit(true);
	}

	protected void parseSortStatements() {
		List<VarDeclaration> lvd = new ArrayList<>();
		List<MethodDeclaration> lmd = new ArrayList<>();
		List<Statement> ls = new ArrayList<>();

		// Sift statements
		for (Statement s : statements) {
			if (s instanceof VarDeclaration) lvd.add((VarDeclaration) s);
			else if (s instanceof FunctionDeclaration) lmd.add((MethodDeclaration) s);
			else ls.add(s);
		}

		// Should we add a default constructor?
		if (!hasConstructor(lmd)) lmd.add(0, defaultConstructor());

		// Convert to arrays
		fieldDecl = lvd.toArray(new FieldDeclaration[0]);
		methodDecl = lmd.toArray(new MethodDeclaration[0]);
		statements = ls.toArray(new Statement[0]);

		// Add 'this' argument to all method declarations
		addThisArgToMethods();

		// Add all methods to TypeClass' symbol table
		getType().addToSymbolTable();
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;

		if (extendsName != null) extendsClass = (TypeClass) symtab.getType(extendsName);

		for (VarDeclaration vd : fieldDecl)
			vd.returnType(symtab);

		for (FunctionDeclaration fd : methodDecl)
			fd.returnType(symtab);

		for (Statement s : statements)
			s.returnType(symtab);

		returnType = getType();
		return returnType;
	}

	/**
	 * Run
	 */
	@Override
	public void runStep(BdsThread bdsThread) {
		// Nothing to do (it's just a declaration)
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class " + className);
		if (extendsName != null) sb.append(" extends " + extendsName);

		sb.append(" {\n");

		if (fieldDecl != null && fieldDecl.length > 0) {
			sb.append("\t# Variables\n");
			for (int i = 0; i < fieldDecl.length; i++)
				sb.append("\t" + fieldDecl[i] + "\n");
			sb.append("\n");
		}

		if (methodDecl != null && methodDecl.length > 0) {
			sb.append("\t# Methods\n");
			for (int i = 0; i < methodDecl.length; i++)
				sb.append("\t" + methodDecl[i].signatureWithName() + "\n");
			sb.append("\n");
		}

		sb.append("}\n");
		return sb.toString();
	}

	@Override
	public void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Class name collides with other names?
		if (symtab.getTypeLocal(className) != null) {
			compilerMessages.add(this, "Duplicate local name " + className, MessageType.ERROR);
		} else if ((className != null) && (getType() != null)) {
			// Add to symbol table
			addSymTab(symtab);
		}

		// Check parent class
		if (extendsName != null && extendsClass == null) {
			compilerMessages.add(this, "Class '" + extendsName + "' not found", MessageType.ERROR);
		}

		// Check constructors
		for (MethodDeclaration md : methodDecl) {
			if (isConstructor(md) //
					&& md.getReturnType() != null //
					&& !md.isNative() //
					&& !md.getReturnType().isVoid() //
			) {
				compilerMessages.add(this, "Constructor return type must be 'void': " + className, MessageType.ERROR);
			}
		}
	}

}
