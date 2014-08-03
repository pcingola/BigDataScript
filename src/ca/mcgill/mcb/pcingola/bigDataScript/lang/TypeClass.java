package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;

/**
 * Class type
 *
 * @author pcingola
 */
public class TypeClass extends Type {

	String className;

	/**
	 * Get a class type
	 */
	public static TypeClass get(String className) {
		// Get type from hash
		String key = PrimitiveType.CLASS + ":" + className;
		TypeClass type = (TypeClass) types.get(key);

		// No type available? Create & add
		if (type == null) {
			type = new TypeClass(null, null);
			type.primitiveType = PrimitiveType.CLASS;
			type.className = className;
			types.put(key, type);
		}

		return type;
	}

	private TypeClass(BigDataScriptNode parent, ParseTree tree) {
		super();
		primitiveType = PrimitiveType.CLASS;
	}

	public boolean canCast(TypeClass type) {
		throw new RuntimeException("Unimplemented!");
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

	@Override
	public Scope getClassScope() {
		return classScope;
	}

	@Override
	public boolean isClass() {
		return true;
	}

	@Override
	public boolean isPrimitiveType() {
		return false;
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
