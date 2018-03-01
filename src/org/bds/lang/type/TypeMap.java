package org.bds.lang.type;

import java.util.ArrayList;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
import org.bds.lang.nativeMethods.MethodNative;
import org.bds.lang.value.Value;
import org.bds.util.Gpr;

/**
 * A hash /  map/ dictionary
 *
 * @author pcingola
 */
public class TypeMap extends Type {

	public static boolean debug = false;

	protected Type keyType; // Type for 'key' elements
	protected Type valueType; // Type for 'value' elements

	/**
	 * Get a list type
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

	public TypeMap(BdsNode parent, ParseTree tree) {
		super(parent, tree);
		primitiveType = PrimitiveType.MAP;
	}

	private TypeMap(Type keyType, Type valueType) {
		super(PrimitiveType.MAP);
		this.keyType = keyType;
		this.valueType = valueType;
	}

	/**
	 * Add all library methods here
	 */
	protected void addNativeMethods() {
		try {
			// Add libarary methods
			ArrayList<MethodNative> methods = new ArrayList<>();
			//!!! TODO: UNCOMENT
			//			methods.add(new MethodNativeMapKeys(this));
			//			methods.add(new MethodNativeMapValues(this));
			//			methods.add(new MethodNativeMapSize(this));
			//			methods.add(new MethodNativeMapHasKey(this));
			//			methods.add(new MethodNativeMapHasValue(this));
			//			methods.add(new MethodNativeMapRemove(this));

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

	@Override
	public boolean equals(Type type) {
		TypeMap mtype = (TypeMap) type;
		return (primitiveType == type.primitiveType) //
				&& keyType.equals(mtype.keyType) //
				&& valueType.equals(mtype.valueType) //
		;
	}

	public Type getKeyType() {
		return keyType;
	}

	public Type getValueType() {
		return valueType;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void parse(ParseTree tree) {
		// !!! TODO: We are only allowing to build lists of primitive types. We should change this!
		String valueTypeName = tree.getChild(0).getChild(0).getText();
		primitiveType = PrimitiveType.MAP;
		keyType = Types.get(valueTypeName.toUpperCase());
		Types.put(this);
		addNativeMethods();
	}

	// !!! TODO: FIX
	//	@Override
	//	public String toStringSerializer() {
	//		return primitiveType + ":" + keyType.toStringSerializer();
	//	}

	@Override
	public String toString() {
		return typeKey(keyType, valueType);
	}

}
