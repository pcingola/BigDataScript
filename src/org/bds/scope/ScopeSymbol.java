package org.bds.scope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bds.lang.Type;
import org.bds.serialize.BdsSerialize;
import org.bds.serialize.BdsSerializer;
import org.bds.util.Gpr;
import org.bds.util.GprString;

/**
 * A symbol in the scope
 *
 * @author pcingola
 */
public class ScopeSymbol implements BdsSerialize, Comparable<ScopeSymbol> {

	// Internal variables use this symbol at the beginning to make sure programmers don't collide with their names
	// Important: This must be an invalid symbol in variable names
	public static final String INTERNAL_SYMBOL_START = "$";
	public static boolean debug = false;

	private static int scopeSymbolNum = 0;

	int id;
	Type type;
	String name;
	Object value;
	boolean constant = false;

	protected static int nextId() {
		return ++scopeSymbolNum;
	}

	public ScopeSymbol() {
		id = nextId();
	}

	public ScopeSymbol(String name, Type type) {
		this.name = name;
		this.type = type;
		id = nextId();
		value = type.defaultValue();
	}

	public ScopeSymbol(String name, Type type, Object value) {
		this.name = name;
		this.type = type;
		this.value = value;
		id = nextId();
	}

	@Override
	public int compareTo(ScopeSymbol ss) {
		return getName().toLowerCase().compareTo(ss.getName().toLowerCase());
	}

	public String getName() {
		return name;
	}

	@Override
	public String getNodeId() {
		return getClass().getSimpleName() + ":" + id;
	}

	public Type getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

	public boolean isConstant() {
		return constant;
	}

	public boolean isFunction() {
		return type.isFunction();
	}

	@Override
	public void serializeParse(BdsSerializer serializer) {
		// Parse type
		name = serializer.getNextFieldString();
		type = serializer.getNextFieldType();

		// Parse value
		value = serializer.getNextField(type);
	}

	@Override
	public String serializeSave(BdsSerializer serializer) {
		return getClass().getSimpleName() //
				+ "\t" + serializer.serializeSaveValue(name) //
				+ "\t" + BdsSerializer.TYPE_IDENTIFIER + type.toStringSerializer() //
				+ "\t" + serializer.serializeSaveValue(value) //
				+ "\n";
	}

	public void setConstant(boolean constant) {
		this.constant = constant;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setValue(Object value) {
		if (debug) Gpr.debug("Setting value:\t" + name + " = " + value);

		if (type.isList()) {
			// Assign the whole list? => Create a new copy
			this.value = new ArrayList<>();
			((List) this.value).addAll((List) value);
		} else if (type.isMap()) {
			// Assign the whole map? => Create a new copy
			this.value = new HashMap();
			Map mapNew = ((Map) this.value);
			Map mapOri = ((Map) value);
			for (Object key : mapOri.keySet()) {
				mapNew.put(key, mapOri.get(key));
			}
		} else {
			// Assign value
			this.value = value;
		}
	}

	@Override
	public String toString() {
		String valStr = "null";

		if (type != null && value != null) {
			if (type.isString()) valStr = "\"" + GprString.escape(value.toString()) + "\"";
			else if (type.isFunction()) return name + " : " + type;
			else valStr = "" + value;
		}

		return type //
				+ " " + name //
				+ " = " + valStr;
	}

}
