package org.bds.lang.type;

import org.bds.lang.statement.ClassDeclaration;
import org.bds.lang.statement.FunctionDeclaration;
import org.bds.lang.statement.VarDeclaration;
import org.bds.lang.statement.VariableInit;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueClass;
import org.bds.scope.Scope;
import org.bds.scope.ScopeSymbol;

/**
 * Class type
 *
 * @author pcingola
 */
public class TypeClass extends TypeComposite {

	ClassDeclaration classDecl;
	String className;
	Scope classScope;

	public static TypeClass factory(ClassDeclaration classDecl, Scope parentScope) {
		// Get type from hash
		String className = classDecl.getClassName();
		if (Types.get(className) != null) throw new RuntimeException("Class '" + className + "' already exists. This should never happen!");

		TypeClass type = new TypeClass(classDecl, parentScope);
		Types.put(type);

		return type;
	}

	/**
	 * Get a class type
	 */
	public static TypeClass get(String className) {
		// Get type from hash
		String key = className;
		return (TypeClass) Types.get(key);
	}

	//	private TypeClass(BdsNode parent, ParseTree tree) {
	//		super(parent, tree);
	//		primitiveType = PrimitiveType.CLASS;
	//	}

	private TypeClass(ClassDeclaration classDeclaration, Scope parentScope) {
		super(PrimitiveType.CLASS);
		classDecl = classDeclaration;
		className = classDecl.getClassName();
		initClassScope(parentScope);
	}

	@Override
	public int compareTo(Type type) {
		int cmp = primitiveType.ordinal() - type.primitiveType.ordinal();
		if (cmp != 0) return cmp;

		TypeClass typec = (TypeClass) type;
		// Both type names are null? => Equal
		if ((className == null) && (typec.className == null)) return 0;

		// Any null is 'first'
		if ((className != null) && (typec.className == null)) return 1;
		if ((className == null) && (typec.className != null)) return -1;

		// Compare names
		return className.compareTo(typec.className);
	}

	public Scope getClassScope() {
		return classScope;
	}

	/**
	 * Initialize class scope
	 */
	protected void initClassScope(Scope parentScope) {
		classScope = new Scope(parentScope, this);

		// Add all variables
		for (VarDeclaration vd : classDecl.getVarDecl()) {
			Type type = vd.getType();
			for (VariableInit vi : vd.getVarInit()) {
				String name = vi.getVarName();
				classScope.add(new ScopeSymbol(name, type));
			}
		}

		// Add all methods
		for (FunctionDeclaration fd : classDecl.getFuncDecl()) {
			Type type = fd.getType();
			String name = fd.getFunctionName();
			classScope.add(new ScopeSymbol(name, type));
		}
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
	public String toString() {
		return "class " + className;
	}

	@Override
	public String toStringSerializer() {
		return primitiveType.toString() + "\t" + className;
	}

}
