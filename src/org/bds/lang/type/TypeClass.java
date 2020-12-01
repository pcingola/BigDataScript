package org.bds.lang.type;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.statement.ClassDeclaration;
import org.bds.lang.statement.FieldDeclaration;
import org.bds.lang.statement.FunctionDeclaration;
import org.bds.lang.statement.MethodDeclaration;
import org.bds.lang.statement.VariableInit;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueClass;

/**
 * Class type
 * Can create native classes by creating a TypeClass with a ClassDeclaration
 *
 * @author pcingola
 */
public class TypeClass extends TypeComposite {

	private static final long serialVersionUID = -6173442643563941413L;

	protected String className;
	protected ClassDeclaration classDecl;

	/**
	 * This constructor creates a "stub" TypeClass
	 * (it does NOT have classDeclaration information)
	 */
	public TypeClass(BdsNode parent, ParseTree tree) {
		super(parent, tree);
		primitiveType = PrimitiveType.CLASS;
	}

	/**
	 * This constructor creates a "real" TypeClass
	 * (has classDeclaration information)
	 */
	public TypeClass(ClassDeclaration classDeclaration) {
		super(PrimitiveType.CLASS);
		classDecl = classDeclaration;
		className = classDecl != null ? classDecl.getClassName() : "null";
		Types.add(this);
	}

	/**
	 * Add all methods to symbol table
	 */
	public void addToSymbolTable() {
		// Add all fields
		for (FieldDeclaration fd : classDecl.getFieldDecl()) {
			Type type = fd.getType();
			for (VariableInit vi : fd.getVarInit()) {
				symbolTable.addVariable(vi.getVarName(), type);
			}
		}

		// Add methods
		for (MethodDeclaration md : classDecl.getMethodDecl()) {
			if (!md.isNative()) { // Add declared method (native methods are added to symbol table during initialization, do not add again)
				symbolTable.addFunction(md);
			}
		}
	}

	/**
	 * Add to Types or replace stub (if needed)
	 */
	public void addType() {
		// Add to Types if needed
		TypeClass t = (TypeClass) Types.get(getCanonicalName());
		if (t == null) {
			// No TypeClass? Add it
			Types.add(this);
		} else if (classDecl != null && t.isStub()) {
			// Do we have a 'stub' class definition in Types? Set it properly
			t.set(this);
		}
	}

	@Override
	public boolean canCastTo(Type type) {
		if (!type.isClass()) return false;
		TypeClass typeClass = (TypeClass) type;
		return getCanonicalName().equals(typeClass.getCanonicalName()) // Same class?
				|| isSubClassOf(typeClass) // Can cast if 'type' is a superclass
				|| type.isAny() // Cast to 'any'
		;
	}

	@Override
	public int compareTo(Type type) {
		int cmp = primitiveType.ordinal() - type.primitiveType.ordinal();
		if (cmp != 0) return cmp;

		String tcn = ((TypeClass) type).getClassName();

		// Compare if null
		if ((className == null) && (tcn == null)) return 0;
		if ((className != null) && (tcn == null)) return 1;
		if ((className == null) && (tcn != null)) return -1;

		// Compare names
		return className.compareTo(tcn);
	}

	@Override
	public String getCanonicalName() {
		return className; // We should implement a 'real' canonical name
	}

	public ClassDeclaration getClassDeclaration() {
		setStub();
		return classDecl;
	}

	public String getClassName() {
		return className;
	}

	/**
	 * Get parent class type
	 */
	public TypeClass getParentTypeClass() {
		ClassDeclaration cdecl = getClassDeclaration();
		if (cdecl == null) return null; // No class declaration
		if (!cdecl.isSubClass()) return null; // Not a sub-class

		// Try to find parent TypeClass
		TypeClass tc = cdecl.getClassTypeParent();
		if (tc != null) return tc; // Found parent TypeClass

		// TypeClass not found. Probably we have not performed type checking for the sub class yet.
		// Try to find a stub using a canonical name
		String tcName = cdecl.getExtendsName();
		tc = (TypeClass) Types.get(tcName);

		return tc;
	}

	public boolean hasClassDeclaration() {
		setStub();
		return classDecl != null;
	}

	@Override
	public boolean isClass() {
		return true;
	}

	public boolean isStub() {
		return classDecl == null;
	}

	/**
	 * Is 'this' a sub-class of 'type'?
	 */
	public boolean isSubClassOf(TypeClass type) {
		// Go up until we find the parent
		String typeCan = type.getCanonicalName();
		for (TypeClass tchild = this; tchild != null; tchild = tchild.getParentTypeClass()) {
			String tchildCan = tchild.getCanonicalName();
			if (tchildCan.equals(typeCan)) return true;
		}
		return false;
	}

	@Override
	public Value newDefaultValue() {
		return new ValueClass(this); // New object, no fields initializes (null fields)
	}

	@Override
	public Value newValue() {
		ValueClass nv = new ValueClass(this); // New object.
		nv.initializeFields();
		return nv;
	}

	@Override
	protected void parse(ParseTree tree) {
		className = tree.getChild(0).getText();
	}

	/**
	 * Resolve method call having the same name and arguments & types (except 'this' argument)
	 */
	@Override
	public FunctionDeclaration resolve(FunctionDeclaration fdecl) {
		setStub();
		if (!fdecl.isMethod()) return fdecl;
		FunctionDeclaration fd = symbolTable.findMethod(fdecl);
		if (fd != null) return fd;
		return classDecl != null && classDecl.getClassParent() != null ? classDecl.getClassTypeParent().resolve(fdecl) : fdecl;
	}

	/**
	 * Get type for field 'name'
	 */
	public Type resolve(String name) {
		setStub();
		Type t = symbolTable.resolveLocal(name);
		if (t != null) return t;
		return classDecl != null && classDecl.getClassParent() != null ? classDecl.getClassTypeParent().resolve(name) : null;
	}

	/**
	 * Set parameters from another class definition
	 */
	void set(TypeClass t) {
		classDecl = t.classDecl;
		symbolTable = t.symbolTable;
	}

	public void setClassDeclaration(ClassDeclaration cd) {
		classDecl = cd;
	}

	/**
	 *  Try to find class declaration from 'Types' and set declaration
	 */
	void setStub() {
		if (isStub()) {
			TypeClass t = (TypeClass) Types.get(getCanonicalName());
			if (t != null && !t.isStub()) set(t);
		}
	}

	@Override
	public String toString() {
		return className;
	}

}
