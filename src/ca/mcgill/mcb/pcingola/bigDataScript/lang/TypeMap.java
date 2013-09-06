package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.util.ArrayList;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.MethodNative;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.list.MethodNativeListAdd;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.list.MethodNativeListAddList;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.list.MethodNativeListHead;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.list.MethodNativeListIsEmpty;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.list.MethodNativeListJoin;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.list.MethodNativeListJoinStr;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.list.MethodNativeListPop;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.list.MethodNativeListPush;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.list.MethodNativeListSize;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.list.MethodNativeListSort;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.list.MethodNativeListTail;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * A hash
 * 
 * @author pcingola
 */
public class TypeMap extends TypeList {

	public static boolean debug = false;

	/**
	 * Get a list type
	 * @param primitiveType
	 * @param typeName
	 * @return
	 */
	public static TypeMap get(Type baseType) {
		// Get type from hash
		String key = PrimitiveType.LIST + ":" + baseType;
		TypeMap type = (TypeMap) types.get(key);

		// No type available? Create & add
		if (type == null) {
			type = new TypeMap(null, null);
			type.primitiveType = PrimitiveType.LIST;
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

	public TypeMap(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Add all library methods here
	 */
	protected void addNativeMethods() {
		if (classScope == null) classScope = new Scope(null);

		// Add libarary methods
		ArrayList<MethodNative> methods = new ArrayList<MethodNative>();
		methods.add(new MethodNativeListAdd(baseType));
		methods.add(new MethodNativeListAddList(baseType));
		methods.add(new MethodNativeListHead(baseType));
		methods.add(new MethodNativeListIsEmpty(baseType));
		methods.add(new MethodNativeListJoin(baseType));
		methods.add(new MethodNativeListJoinStr(baseType));
		methods.add(new MethodNativeListPop(baseType));
		methods.add(new MethodNativeListPush(baseType));
		methods.add(new MethodNativeListSize(baseType));
		methods.add(new MethodNativeListSort(baseType));
		methods.add(new MethodNativeListTail(baseType));

		// Show
		if (debug) {
			Gpr.debug("Type " + this + ", library methods added: ");
			for (MethodNative method : methods)
				Gpr.debug("\t" + method.signature());
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
	public boolean isList(Type baseType) {
		return this.baseType.equals(baseType);
	}

	@Override
	protected void parse(ParseTree tree) {
		// TODO: We are only allowing to build lists of primitive types. We should change this!
		String listTypeName = tree.getChild(0).getChild(0).getText();
		primitiveType = PrimitiveType.LIST;
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
