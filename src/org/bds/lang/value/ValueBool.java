package org.bds.lang.value;

import org.bds.lang.type.Type;
import org.bds.lang.type.Types;

public class ValueBool extends ValuePrimitive {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public final static ValueBool TRUE = new ValueBool(true);
	public final static ValueBool FALSE = new ValueBool(false);

	boolean value;

	public ValueBool() {
		super();
	}

	public ValueBool(Boolean v) {
		super();
		set(v);
	}

	@Override
	public boolean asBool() {
		return value;
	}

	@Override
	public long asInt() {
		return value ? 1L : 0L;
	}

	/**
	 * Convert to 'real'
	 */
	@Override
	public double asReal() {
		throw new RuntimeException("Cannot convert type '" + getType() + "' to real");
	}

	@Override
	public String asString() {
		return Boolean.toString(value);
	}

	@Override
	public ValueBool clone() {
		return new ValueBool(asBool());
	}

	@Override
	public boolean equals(Object v) {
		if (v instanceof ValueBool) return value == ((ValueBool) v).value;
		return false;
	}

	@Override
	public Boolean get() {
		return Boolean.valueOf(value);
	}

	@Override
	public Type getType() {
		return Types.BOOL;
	}

	@Override
	public int hashCode() {
		return value ? 1 : 0;
	}

	@Override
	public void parse(String str) {
		value = Boolean.parseBoolean(str);
	}

	@Override
	public void set(Object v) {
		value = (Boolean) v;
	}

}
