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
import org.bds.lang.type.Types;
import org.bds.symbol.SymbolTable;

/**
 * Variable declaration
 *
 * @author pcingola
 */
public class ClassDeclaration extends Block {

	private static final long serialVersionUID = -8891327817053470787L;

	public static final String VAR_SUPER = "super";
	public static final String VAR_THIS = "this";

	protected String className;
	protected String classNameParent;
	protected ClassDeclaration classParent;
	protected TypeClass classType;
	protected TypeClass classTypeParent;
	protected FieldDeclaration fieldDecl[];
	protected MethodDeclaration methodDecl[];

	public ClassDeclaration(BdsNode parent, ParseTree tree) {
		super(parent, tree);
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
	 * Add type to 'Types' and 'this'
	 */
	protected void addType(SymbolTable symtab) {
		// Add type for 'this' object in current table
		TypeClass t = getType();
		symtab.addVariable(VAR_THIS, t);
		symtab.addVariable(VAR_SUPER, t);
		t.addType(); // Add to Types if needed
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

	public TypeClass getClassTypeParent() {
		return classTypeParent;
	}

	public String getExtendsName() {
		return classNameParent;
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
		if (classType == null) {
			classType = new TypeClass(this);
		}
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

	public boolean isSubClass() {
		return classNameParent != null;
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
			classNameParent = tree.getChild(idx++).getText();
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

	/**
	 * Parse class declaration and sort statements (variables, methods and other statements)
	 */
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

		if (classNameParent != null) {
			classTypeParent = (TypeClass) Types.get(classNameParent);
			if (classTypeParent != null) classParent = classTypeParent.getClassDeclaration();
		}

		for (VarDeclaration vd : fieldDecl)
			vd.returnType(symtab);

		for (FunctionDeclaration fd : methodDecl)
			fd.returnType(symtab);

		if (statements != null) {
			for (Statement s : statements)
				s.returnType(symtab);
		}

		returnType = getType();
		return returnType;
	}

	@Override
	public String toAsm() {
		StringBuilder sb = new StringBuilder();

		String labelClassEnd = baseLabelName() + "end";

		sb.append(toAsmNode());
		sb.append("jmp " + labelClassEnd + "\n"); // Jump to end of class (in case of runaway code

		// Compile non-native methods
		for (FunctionDeclaration fd : methodDecl)
			if (!fd.isNative()) sb.append(fd.toAsm());

		for (Statement s : statements)
			sb.append(s.toAsm());

		sb.append(labelClassEnd + ":\n");
		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class " + className);
		if (classNameParent != null) sb.append(" extends " + classNameParent);

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
				sb.append("\t" + methodDecl[i].signature() + "\n");
			sb.append("\n");
		}

		sb.append("}\n");
		return sb.toString();
	}

	@Override
	public void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Class name collides with other names?
		if (symtab.getVariableTypeLocal(className) != null) {
			compilerMessages.add(this, "Duplicate local name " + className, MessageType.ERROR);
		} else if ((className != null) && (getType() != null)) {
			// Add to symbol table
			addType(symtab);
		}

		// Check parent class
		if (classNameParent != null && classTypeParent == null) {
			compilerMessages.add(this, "Class '" + classNameParent + "' not found", MessageType.ERROR);
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
