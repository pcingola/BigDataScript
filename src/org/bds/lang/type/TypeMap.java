package org.bds.lang.type;

import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.nativeMethods.MethodNative;
import org.bds.lang.nativeMethods.map.MethodNativeMapClone;
import org.bds.lang.nativeMethods.map.MethodNativeMapGet;
import org.bds.lang.nativeMethods.map.MethodNativeMapHasKey;
import org.bds.lang.nativeMethods.map.MethodNativeMapHasValue;
import org.bds.lang.nativeMethods.map.MethodNativeMapHashCode;
import org.bds.lang.nativeMethods.map.MethodNativeMapKeys;
import org.bds.lang.nativeMethods.map.MethodNativeMapRemove;
import org.bds.lang.nativeMethods.map.MethodNativeMapSize;
import org.bds.lang.nativeMethods.map.MethodNativeMapValues;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueMap;

/**
 * A hash /  map/ dictionary
 *
 * @author pcingola
 */
public class TypeMap extends TypeComposite {

	private static final long serialVersionUID = 3321302248243052342L;

	public static boolean debug = false;

	public static final TypeMap MAP_ANY_ANY = new TypeMap(Types.ANY, Types.ANY);

	protected Type keyType; // Type for 'key' elements
	protected Type valueType; // Type for 'value' elements

	public static TypeMap factory(BdsNode parent, ParseTree tree) {
		TypeMap typeMap = new TypeMap(parent, tree);
		return get(typeMap.getKeyType(), typeMap.getValueType());
	}

	/**
	 * Get a map type (used cached version if found)
	 */
	public static TypeMap get(Type keyType, Type valueType) {
		// Get type from hash
		String key = typeKey(keyType, valueType);
		TypeMap type = (TypeMap) Types.get(key);

		// No type cached? Create & add
		if (type == null) {
			type = new TypeMap(keyType, valueType);
			Types.add(type);
			type.addNativeMethods();
		}

		return type;
	}

	public static String typeKey(Type keyType, Type valueType) {
		return valueType + "{" + keyType + "}";
	}

	private TypeMap(BdsNode parent, ParseTree tree) {
		super(parent, tree);
		primitiveType = PrimitiveType.MAP;
	}

	private TypeMap(Type keyType, Type valueType) {
		super(PrimitiveType.MAP);
		this.keyType = keyType;
		this.valueType = valueType;
	}

	/**
	 * Can this type be casted to 'type'?
	 */
	@Override
	public boolean canCastTo(Type type) {
		return equals(type) // Same type
				|| (isEmptyMap() && type.isMap()) // Empty maps can be converted in assignment. e.g.: " map = {} "
				|| type.isBool() // Convert to boolean value
				|| type.isAny() // Cast to 'any'
		;
	}

	@Override
	public int compareTo(Type type) {
		// Compare type
		int cmp = super.compareTo(type);
		if (cmp != 0) return cmp;

		// Compare key type
		TypeMap mtype = (TypeMap) type;
		cmp = keyType.compareTo(mtype.keyType);
		if (cmp != 0) return cmp;

		// Compare value type
		return valueType.compareTo(mtype.valueType);
	}

	/**
	 * Declare all native methods for this class
	 */
	@Override
	protected List<MethodNative> declateNativeMethods() {
		List<MethodNative> methods = super.declateNativeMethods();
		try {
			methods.add(new MethodNativeMapClone(this));
			methods.add(new MethodNativeMapGet(this));
			methods.add(new MethodNativeMapKeys(this));
			methods.add(new MethodNativeMapHasKey(this));
			methods.add(new MethodNativeMapHasValue(this));
			methods.add(new MethodNativeMapHashCode(this));
			methods.add(new MethodNativeMapRemove(this));
			methods.add(new MethodNativeMapSize(this));
			methods.add(new MethodNativeMapValues(this));

			// Show
			debug("Type " + this + ", library methods added: ");
		} catch (Throwable t) {
			t.printStackTrace();
			throw new RuntimeException("Error while adding native mehods for class '" + this + "'", t);
		}
		return methods;
	}

	@Override
	public boolean equals(Type type) {
		if (!type.isMap()) return false;
		TypeMap mtype = (TypeMap) type;
		return keyType.equals(mtype.keyType) //
				&& valueType.equals(mtype.valueType) //
		;
	}

	public Type getKeyType() {
		return keyType;
	}

	public Type getValueType() {
		return valueType;
	}

	/**
	 * Empty map (e.g. LiteralMapEmpty)
	 * @return
	 */
	boolean isEmptyMap() {
		return getKeyType().isVoid() && getValueType().isVoid();
	}

	@Override
	public boolean isMap() {
		return true;
	}

	@Override
	public boolean isMap(Type kewType, Type valueType) {
		return keyType.equals(keyType) //
				&& valueType.equals(this.valueType) //
		;
	}

	@Override
	public Value newDefaultValue() {
		return new ValueMap(this);
	}

	@Override
	public Value newValue(Object v) {
		throw new RuntimeException("Unimplemented. This method should never be invoked!");
	}

	@Override
	protected void parse(ParseTree tree) {
		// Value type
		primitiveType = PrimitiveType.MAP;
		valueType = (Type) factory(tree, 0);

		// Key type
		if (tree.getChildCount() > 3) {
			keyType = (Type) factory(tree, 2);
		} else {
			// Default key type: string
			keyType = Types.STRING;
		}
	}

	@Override
	public String toString() {
		return typeKey(keyType, valueType);
	}

}
