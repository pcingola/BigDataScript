package org.bds.scope;

import org.bds.lang.type.PrimitiveType;
import org.bds.lang.type.Type;
import org.bds.lang.value.Value;
import org.bds.serialize.BdsSerialize;
import org.bds.serialize.BdsSerializer;
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
	Value value;
	boolean constant = false;

	protected static int nextId() {
		return ++scopeSymbolNum;
	}

	public ScopeSymbol() {
		id = nextId();
	}

	public ScopeSymbol(String name, Object val) {
		this.name = name;
		value = Value.factory(val);
		type = value.getType();
		id = nextId();
	}

	public ScopeSymbol(String name, Type type) {
		this.name = name;
		this.type = type;
		id = nextId();
		value = type.getDefaultValue();
	}

	public ScopeSymbol(String name, Type type, Object value) {
		this.name = name;
		this.type = type;
		this.value = type.newValue(value);
		id = nextId();
	}

	public ScopeSymbol(String name, Value value) {
		this.name = name;
		this.value = value;
		type = value.getType();
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

	public Value getValue() {
		return value;
	}

	public boolean isConstant() {
		return constant;
	}

	public boolean isFunction() {
		return type.getPrimitiveType() == PrimitiveType.FUNCTION;
	}

	@Override
	public void serializeParse(BdsSerializer serializer) {
		// Parse type
		name = serializer.getNextFieldString();
		type = serializer.getNextFieldType();

		// Parse map
		// !!! TODO:
		// value = serializer.getNextField(type);
		throw new RuntimeException("!!! UNIMPLEMENTED");
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

	public void setValue(Value value) {
		this.value.set(value.get());
	}

	public void setValueNative(Object obj) {
		value.set(obj);
	}

	@Override
	public String toString() {
		String valStr = "null";

		if (type != null && value != null) {
			if (type.isString()) valStr = "\"" + GprString.escape(value.toString()) + "\"";
			else if (isFunction()) return name + " : " + type;
			else valStr = "" + value;
		}

		return type //
				+ " " + name //
				+ " = " + valStr;
	}

}
