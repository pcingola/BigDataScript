package org.bds.lang.type;

import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.nativeMethods.MethodNative;
import org.bds.lang.nativeMethods.map.MethodNativeMapClone;
import org.bds.lang.nativeMethods.map.MethodNativeMapHasKey;
import org.bds.lang.nativeMethods.map.MethodNativeMapHasValue;
import org.bds.lang.nativeMethods.map.MethodNativeMapKeys;
import org.bds.lang.nativeMethods.map.MethodNativeMapRemove;
import org.bds.lang.nativeMethods.map.MethodNativeMapSize;
import org.bds.lang.nativeMethods.map.MethodNativeMapValues;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueMap;
import org.bds.util.Gpr;

/**
 * A hash /  map/ dictionary
 *
 * @author pcingola
 */
public class TypeMap extends TypeComposite {

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
			Types.put(type);
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
				|| isEmptyMap() // Empty mapscan be converted in assignment. e.g.: " map = {} "
				|| type.isBool() // Convert to boolean value
		;
	}

	@Override
	public Value cast(Value v) {
		if (is(v.getType())) return v; // Same type? No need to cast
		if (v.getType().isMap() && ((TypeMap) v.getType()).isEmptyMap()) return newValue(); // Empty map? Create new value
		throw new RuntimeException("Cannot cast type '" + v.getType() + "' to type '" + this + "'");
	}

	@Override
	public Object castNativeObject(Object o) {
		if (o instanceof Map) return o;
		throw new RuntimeException("Cannot cast native object '" + o.getClass().getCanonicalName() + "' to type '" + this + "'");
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
			methods.add(new MethodNativeMapKeys(this));
			methods.add(new MethodNativeMapHasKey(this));
			methods.add(new MethodNativeMapHasValue(this));
			methods.add(new MethodNativeMapRemove(this));
			methods.add(new MethodNativeMapSize(this));
			methods.add(new MethodNativeMapValues(this));

			// Show
			if (debug) {
				Gpr.debug("Type " + this + ", library methods added: ");
				for (MethodNative method : methods)
					Gpr.debug("\t" + method.signature());
			}
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
	public Value newValue() {
		return new ValueMap(this);
	}

	// !!! TODO: FIX
	//	@Override
	//	public String toStringSerializer() {
	//		return primitiveType + ":" + keyType.toStringSerializer();
	//	}

	@Override
	protected void parse(ParseTree tree) {
		// !!! TODO: We are only allowing to build maps of primitive types!

		// Value type
		String valueTypeName = tree.getChild(0).getChild(0).getText();
		primitiveType = PrimitiveType.MAP;
		valueType = Types.get(valueTypeName.toUpperCase());

		// Key type
		if (tree.getChildCount() > 3) {
			String keyTypeName = tree.getChild(2).getChild(0).getText();
			keyType = Types.get(keyTypeName.toUpperCase());
		} else {
			// Default key is type string
			keyType = Types.STRING;
		}
	}

	@Override
	public String toString() {
		return typeKey(keyType, valueType);
	}

}
