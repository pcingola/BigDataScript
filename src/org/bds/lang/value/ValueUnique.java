package org.bds.lang.value;

import java.util.HashMap;
import java.util.Map;

import org.bds.lang.type.Type;

/**
 * Unique values, such as 'null' and 'void'
 *
 * @author pcingola
 */
public class ValueUnique extends ValuePrimitive {

	private static Map<Type, ValueUnique> valueUniqueByType = new HashMap<>();

	protected Type type;

	public static ValueUnique get(Type type) {
		if (!valueUniqueByType.containsKey(type)) valueUniqueByType.put(type, new ValueUnique(type));
		return valueUniqueByType.get(type);
	}

	private ValueUnique(Type type) {
		super();
		this.type = type;
	}

	@Override
	public boolean asBool() {
		throw new RuntimeException("Cannot convert type '" + getType() + "' to bool");
	}

	@Override
	public long asInt() {
		throw new RuntimeException("Cannot convert type '" + getType() + "' to int");
	}

	@Override
	public double asReal() {
		throw new RuntimeException("Cannot convert type '" + getType() + "' to real");
	}

	@Override
	public String asString() {
		return toString();
	}

	@Override
	public Value clone() {
		return this; // Note that unique values are not cloned
	}

	@Override
	public Object get() {
		return null;
	}

	@Override
	public Type getType() {
		return type;
	}

	@Override
	public void set(Object v) {
		// This value cannot be set. Nothing to do
	}

}
