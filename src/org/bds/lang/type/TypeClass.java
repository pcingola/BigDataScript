package org.bds.lang.type;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.statement.ClassDeclaration;
import org.bds.lang.statement.FieldDeclaration;
import org.bds.lang.statement.MethodDeclaration;
import org.bds.lang.statement.VariableInit;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueClass;

/**
 * Class type
 *
 * @author pcingola
 */
public class TypeClass extends TypeComposite {

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
		className = classDecl.getClassName();
	}

	/**
	 * Add all methods to symbol table
	 */
	public void addToSymbolTable() {
		// Add all fields
		for (FieldDeclaration fd : classDecl.getFieldDecl()) {
			Type type = fd.getType();
			for (VariableInit vi : fd.getVarInit())
				symbolTable.add(vi.getVarName(), type);
		}

		// Add methods
		for (MethodDeclaration md : classDecl.getMethodDecl())
			if (!md.isNative()) { // Native method are added to symbol table during initialization
				symbolTable.add(md.getFunctionName(), md.getType());
			}
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

	public ClassDeclaration getClassDeclaration() {
		return classDecl;
	}

	public String getClassName() {
		return className;
	}

	public Type getType(String name) {
		return symbolTable.getType(name);
	}

	@Override
	public boolean isClass() {
		return true;
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

	@Override
	public String toString() {
		return className;
	}

}
