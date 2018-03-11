package org.bds.lang.type;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.statement.ClassDeclaration;
import org.bds.lang.statement.FunctionDeclaration;
import org.bds.lang.statement.VarDeclaration;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueClass;
import org.bds.scope.Scope;

/**
 * Class type
 *
 * @author pcingola
 */
public class TypeClass extends TypeComposite {

	protected String className;
	protected ClassDeclaration classDecl;
	protected Scope classScope;

	public static TypeClass factory(ClassDeclaration classDecl, Scope scope) {
		// Get type from scope
		String className = classDecl.getClassName();
		if (Types.get(className) != null) throw new RuntimeException("Class '" + className + "' already exists. This should never happen!");

		throw new RuntimeException("!!!!!!!!");
		//		TypeClass type = new TypeClass(classDecl, scope);
		//		Types.put(type);
		//
		//		return type;
	}

	//	public TypeClass(BdsNode parent, ParseTree tree) {
	//		super(parent, tree);
	//		primitiveType = PrimitiveType.CLASS;
	//	}
	//

	public TypeClass(ClassDeclaration classDeclaration) {
		super(PrimitiveType.CLASS);
		throw new RuntimeException("!!!!!!!!! UNIMPLEMENTED");
	}

	//	private TypeClass(ClassDeclaration classDeclaration, Scope parentScope) {
	//		super(PrimitiveType.CLASS);
	//		classDecl = classDeclaration;
	//		initClassScope(parentScope);
	//	}

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

	public String getClassName() {
		return className;
	}

	public Scope getClassScope() {
		return classScope;
	}

	public FunctionDeclaration[] getFuncDecl() {
		return classDecl.getFuncDecl();
	}

	public VarDeclaration[] getVarDecl() {
		return classDecl.getVarDecl();
	}

	/**
	 * Initialize class scope
	 */
	protected void initClassScope(Scope parentScope) {
		//		classScope = new Scope(parentScope, this);
		//
		//		// Add all variables
		//		for (VarDeclaration vd : classDecl.getVarDecl()) {
		//			Type type = vd.getType();
		//			for (VariableInit vi : vd.getVarInit()) {
		//				String name = vi.getVarName();
		//				classScope.add(new ScopeSymbol(name, type));
		//			}
		//		}
		//
		//		// Add all methods
		//		for (FunctionDeclaration fd : classDecl.getFuncDecl()) {
		//			Type type = fd.getType();
		//			String name = fd.getFunctionName();
		//			classScope.add(new ScopeSymbol(name, type));
		//		}
	}

	@Override
	public boolean isClass() {
		return true;
	}

	@Override
	public Value newValue() {
		return new ValueClass(this);
	}

	@Override
	protected void parse(ParseTree tree) {
		className = tree.getChild(0).getText();
	}

	@Override
	public String toString() {
		return className;
	}

	@Override
	public String toStringSerializer() {
		return primitiveType.toString() + "\t" + getClassName();
	}

}
