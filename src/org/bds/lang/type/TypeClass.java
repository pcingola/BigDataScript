package org.bds.lang.type;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueClass;
import org.bds.scope.Scope;

/**
 * Class type
 *
 * @author pcingola
 */
public class TypeClass extends TypeComposite {

	String className;
	Scope classScope;

	/**
	 * Get a class type
	 */
	public static TypeClass get(String className) {
		// Get type from hash
		String key = className;
		TypeClass type = (TypeClass) Types.get(key);

		// No type available? Create & add
		if (type == null) {
			type = new TypeClass(className);
			Types.put(type);
		}

		return type;
	}

	private TypeClass(BdsNode parent, ParseTree tree) {
		super(parent, tree);
		primitiveType = PrimitiveType.CLASS;
	}

	private TypeClass(String clasName) {
		super(PrimitiveType.CLASS);
		className = clasName;
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
