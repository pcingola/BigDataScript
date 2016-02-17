package org.bds.lang;

import java.util.ArrayList;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.nativeMethods.MethodNative;
import org.bds.lang.nativeMethods.map.MethodNativeMapHasKey;
import org.bds.lang.nativeMethods.map.MethodNativeMapHasValue;
import org.bds.lang.nativeMethods.map.MethodNativeMapKeys;
import org.bds.lang.nativeMethods.map.MethodNativeMapRemove;
import org.bds.lang.nativeMethods.map.MethodNativeMapSize;
import org.bds.lang.nativeMethods.map.MethodNativeMapValues;
import org.bds.util.Gpr;

/**
 * A hash
 *
 * @author pcingola
 */
public class TypeMap extends TypeList {

	public static boolean debug = false;

	/**
	 * Get a list type
	 */
	public static TypeMap get(Type baseType) {
		// Get type from hash
		String key = PrimitiveType.MAP + ":" + baseType;
		TypeMap type = (TypeMap) types.get(key);

		// No type available? Create & add
		if (type == null) {
			type = new TypeMap(null, null);
			type.primitiveType = PrimitiveType.MAP;
			type.baseType = baseType;
			put(type);

			type.addNativeMethods();
		}

		return type;
	}

	protected static void put(TypeMap type) {
		// Get type from hash
		String key = PrimitiveType.MAP + ":" + type.baseType;
		types.put(key, type);
	}

	public TypeMap(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Add all library methods here
	 */
	@Override
	protected void addNativeMethods() {
		try {
			// Add libarary methods
			ArrayList<MethodNative> methods = new ArrayList<MethodNative>();
			methods.add(new MethodNativeMapKeys(baseType));
			methods.add(new MethodNativeMapValues(baseType));
			methods.add(new MethodNativeMapSize(baseType));
			methods.add(new MethodNativeMapHasKey(baseType));
			methods.add(new MethodNativeMapHasValue(baseType));
			methods.add(new MethodNativeMapRemove(baseType));

			// Show
			if (debug) {
				Gpr.debug("Type " + this + ", library methods added: ");
				for (MethodNative method : methods)
					Gpr.debug("\t" + method.signature());
			}
		} catch (Throwable t) {
			t.printStackTrace();
			throw new RuntimeException("Erroe while adding native mehods for class '" + this + "'", t);
		}
	}

	@Override
	public int compareTo(Type type) {
		int cmp = primitiveType.ordinal() - type.primitiveType.ordinal();
		if (cmp != 0) return cmp;

		TypeMap ltype = (TypeMap) type;
		return baseType.compareTo(ltype.baseType);
	}

	@Override
	public boolean equals(Type type) {
		return (primitiveType == type.primitiveType) && (baseType.equals(((TypeMap) type).baseType));
	}

	@Override
	public boolean isList() {
		return false;
	}

	@Override
	public boolean isMap() {
		return true;
	}

	@Override
	public boolean isMap(Type baseType) {
		return this.baseType.equals(baseType);
	}

	@Override
	protected void parse(ParseTree tree) {
		// TODO: We are only allowing to build lists of primitive types. We should change this!
		String listTypeName = tree.getChild(0).getChild(0).getText();
		primitiveType = PrimitiveType.MAP;
		baseType = Type.get(listTypeName.toUpperCase());

		put(this);
		addNativeMethods();
	}

	@Override
	public String toString() {
		return baseType + "{}";
	}

	@Override
	public String toStringSerializer() {
		return primitiveType + ":" + baseType.toStringSerializer();
	}

}
