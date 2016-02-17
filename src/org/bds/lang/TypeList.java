package org.bds.lang;

import java.util.ArrayList;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.nativeMethods.MethodNative;
import org.bds.lang.nativeMethods.list.MethodNativeListAdd;
import org.bds.lang.nativeMethods.list.MethodNativeListAddIndex;
import org.bds.lang.nativeMethods.list.MethodNativeListAddList;
import org.bds.lang.nativeMethods.list.MethodNativeListCount;
import org.bds.lang.nativeMethods.list.MethodNativeListFilter;
import org.bds.lang.nativeMethods.list.MethodNativeListForEach;
import org.bds.lang.nativeMethods.list.MethodNativeListHas;
import org.bds.lang.nativeMethods.list.MethodNativeListHead;
import org.bds.lang.nativeMethods.list.MethodNativeListIndexOf;
import org.bds.lang.nativeMethods.list.MethodNativeListIsEmpty;
import org.bds.lang.nativeMethods.list.MethodNativeListJoin;
import org.bds.lang.nativeMethods.list.MethodNativeListJoinStr;
import org.bds.lang.nativeMethods.list.MethodNativeListMap;
import org.bds.lang.nativeMethods.list.MethodNativeListMapToInt;
import org.bds.lang.nativeMethods.list.MethodNativeListMapToReal;
import org.bds.lang.nativeMethods.list.MethodNativeListMapToString;
import org.bds.lang.nativeMethods.list.MethodNativeListPop;
import org.bds.lang.nativeMethods.list.MethodNativeListPush;
import org.bds.lang.nativeMethods.list.MethodNativeListRemove;
import org.bds.lang.nativeMethods.list.MethodNativeListRemoveIdx;
import org.bds.lang.nativeMethods.list.MethodNativeListReverse;
import org.bds.lang.nativeMethods.list.MethodNativeListRm;
import org.bds.lang.nativeMethods.list.MethodNativeListRmOnExit;
import org.bds.lang.nativeMethods.list.MethodNativeListSize;
import org.bds.lang.nativeMethods.list.MethodNativeListSort;
import org.bds.lang.nativeMethods.list.MethodNativeListTail;
import org.bds.util.Gpr;

/**
 * A list/array/stack type
 *
 * @author pcingola
 */
public class TypeList extends Type {

	public static boolean debug = false;

	Type baseType; // Base type for this list

	/**
	 * Get a list type
	 */
	public static TypeList get(Type baseType) {
		// Get type from hash
		String key = PrimitiveType.LIST + ":" + baseType;
		TypeList type = (TypeList) types.get(key);

		// No type available? Create & add
		if (type == null) {
			type = new TypeList(null, null);
			type.primitiveType = PrimitiveType.LIST;
			type.baseType = baseType;
			put(type);

			type.addNativeMethods();
		}

		return type;
	}

	protected static void put(TypeList type) {
		// Get type from hash
		String key = PrimitiveType.LIST + ":" + type.baseType;
		types.put(key, type);
	}

	public TypeList(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Add all library methods here
	 */
	protected void addNativeMethods() {
		try {
			// Add libarary methods
			ArrayList<MethodNative> methods = new ArrayList<MethodNative>();
			methods.add(new MethodNativeListAdd(baseType));
			methods.add(new MethodNativeListAddIndex(baseType));
			methods.add(new MethodNativeListAddList(baseType));
			methods.add(new MethodNativeListCount(baseType));
			methods.add(new MethodNativeListFilter(baseType));
			methods.add(new MethodNativeListForEach(baseType));
			methods.add(new MethodNativeListHas(baseType));
			methods.add(new MethodNativeListHead(baseType));
			methods.add(new MethodNativeListIndexOf(baseType));
			methods.add(new MethodNativeListIsEmpty(baseType));
			methods.add(new MethodNativeListJoin(baseType));
			methods.add(new MethodNativeListJoinStr(baseType));
			methods.add(new MethodNativeListMap(baseType));
			methods.add(new MethodNativeListMapToInt(baseType));
			methods.add(new MethodNativeListMapToReal(baseType));
			methods.add(new MethodNativeListMapToString(baseType));
			methods.add(new MethodNativeListPop(baseType));
			methods.add(new MethodNativeListPush(baseType));
			methods.add(new MethodNativeListSize(baseType));
			methods.add(new MethodNativeListSort(baseType));
			methods.add(new MethodNativeListRemove(baseType));
			methods.add(new MethodNativeListRemoveIdx(baseType));
			methods.add(new MethodNativeListReverse(baseType));
			methods.add(new MethodNativeListRmOnExit(baseType));
			methods.add(new MethodNativeListRm(baseType));
			methods.add(new MethodNativeListTail(baseType));

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

	public boolean canCast(TypeList type) {
		throw new RuntimeException("Unimplemented!");
	}

	@Override
	public int compareTo(Type type) {
		int cmp = primitiveType.ordinal() - type.primitiveType.ordinal();
		if (cmp != 0) return cmp;

		TypeList ltype = (TypeList) type;
		return baseType.compareTo(ltype.baseType);
	}

	@Override
	public boolean equals(Type type) {
		return (primitiveType == type.primitiveType) && (baseType.equals(((TypeList) type).baseType));
	}

	public Type getBaseType() {
		return baseType;
	}

	@Override
	public boolean isList() {
		return true;
	}

	@Override
	public boolean isList(Type baseType) {
		// If baseType is void, then the list must be empty.
		// An empty list complies with all types.
		if (this.baseType.isVoid()) return true;

		return this.baseType.equals(baseType);
	}

	@Override
	public boolean isPrimitiveType() {
		return false;
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
		return baseType + "[]";
	}

	@Override
	public String toStringSerializer() {
		return primitiveType + ":" + baseType.toStringSerializer();
	}

}
