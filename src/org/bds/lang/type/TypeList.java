package org.bds.lang.type;

import java.util.ArrayList;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.lang.BdsNode;
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
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueList;
import org.bds.util.Gpr;

/**
 * A list/array/stack type
 *
 * @author pcingola
 */
public class TypeList extends Type {

	public static boolean debug = false;

	Type elementType; // Type of elements in the list

	/**
	 * Get a (cached) list type
	 */
	public static TypeList get(Type elementType) {
		// Get type from hash
		String key = typeKey(elementType);
		TypeList type = (TypeList) Types.get(key);

		// No type cached? Create & add
		if (type == null) {
			type = new TypeList(elementType);
			Types.put(type);
			type.addNativeMethods();
		}

		return type;
	}

	public static String typeKey(Type elementType) {
		return elementType + "[]";
	}

	private TypeList(BdsNode parent, ParseTree tree, Type baseType) {
		super(parent, tree);
		primitiveType = PrimitiveType.LIST;
		elementType = baseType;
		defaultValue = new ValueList(baseType);
	}

	private TypeList(Type baseType) {
		super(PrimitiveType.LIST);
		elementType = baseType;
		defaultValue = new ValueList(baseType);
	}

	/**
	 * Add all library methods here
	 */
	protected void addNativeMethods() {
		try {
			// Add libarary methods
			ArrayList<MethodNative> methods = new ArrayList<>();
			methods.add(new MethodNativeListAdd(elementType));
			methods.add(new MethodNativeListAddIndex(elementType));
			methods.add(new MethodNativeListAddList(elementType));
			methods.add(new MethodNativeListCount(elementType));
			methods.add(new MethodNativeListFilter(elementType));
			methods.add(new MethodNativeListForEach(elementType));
			methods.add(new MethodNativeListHas(elementType));
			methods.add(new MethodNativeListHead(elementType));
			methods.add(new MethodNativeListIndexOf(elementType));
			methods.add(new MethodNativeListIsEmpty(elementType));
			methods.add(new MethodNativeListJoin(elementType));
			methods.add(new MethodNativeListJoinStr(elementType));
			methods.add(new MethodNativeListMap(elementType));
			methods.add(new MethodNativeListMapToInt(elementType));
			methods.add(new MethodNativeListMapToReal(elementType));
			methods.add(new MethodNativeListMapToString(elementType));
			methods.add(new MethodNativeListPop(elementType));
			methods.add(new MethodNativeListPush(elementType));
			methods.add(new MethodNativeListSize(elementType));
			methods.add(new MethodNativeListSort(elementType));
			methods.add(new MethodNativeListRemove(elementType));
			methods.add(new MethodNativeListRemoveIdx(elementType));
			methods.add(new MethodNativeListReverse(elementType));
			methods.add(new MethodNativeListRmOnExit(elementType));
			methods.add(new MethodNativeListRm(elementType));
			methods.add(new MethodNativeListTail(elementType));

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
		// Compare type
		int cmp = super.compareTo(type);
		if (cmp != 0) return cmp;

		// Compare element type
		TypeList ltype = (TypeList) type;
		return elementType.compareTo(ltype.elementType);
	}

	public Type getElementType() {
		return elementType;
	}

	//	public boolean canCast(TypeList type) {
	//		throw new RuntimeException("Unimplemented!");
	//	}

	@Override
	public boolean isList() {
		return true;
	}

	@Override
	public boolean isList(Type baseType) {
		// If elementType is void, then the list must be empty.
		// An empty list complies with all types.
		if (elementType.isVoid() || elementType.isAny() || baseType.isAny()) return true;
		return elementType.equals(baseType);
	}

	@Override
	public Value newValue() {
		return new ValueList(elementType);
	}

	@Override
	protected void parse(ParseTree tree) {
		// TODO: We are only allowing to build lists of primitive types
		String listTypeName = tree.getChild(0).getChild(0).getText();
		primitiveType = PrimitiveType.LIST;
		elementType = Types.get(listTypeName.toUpperCase());
		Types.put(this);
		addNativeMethods();
	}

	@Override
	public String toString() {
		return typeKey(elementType);
	}

	@Override
	public String toStringSerializer() {
		return primitiveType + ":" + elementType.toStringSerializer();
	}

}
