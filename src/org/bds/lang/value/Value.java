package org.bds.lang.value;

import org.bds.lang.type.Type;
import org.bds.lang.type.Types;

/**
 * Define a value
 * @author pcingola
 */
public abstract class Value implements Cloneable {

	public static final ValueUnique ANY = ValueUnique.get(Types.ANY);
	public static final ValueUnique FAKE = ValueUnique.get(Types.FAKE);
	public static final ValueUnique NULL = ValueUnique.get(Types.NULL);
	public static final ValueUnique VOID = ValueUnique.get(Types.VOID);

	public static Value factory(Object v) {
		if (v instanceof Boolean) return new ValueBool((Boolean) v);
		if (v instanceof Long) return new ValueInt((Long) v);
		if (v instanceof Double) return new ValueReal((Double) v);
		if (v instanceof String) return new ValueString((String) v);
		if (v instanceof Integer) return new ValueInt(((Integer) v).longValue());
		if (v instanceof Float) return new ValueReal(((Float) v).doubleValue());
		if (v == null) return Value.NULL;
		throw new RuntimeException("Cannot create Value from object class " + v.getClass().getCanonicalName());
	}

	public Value() {
		init();
	}

	/**
	 * Convert map to 'bool'
	 */
	public boolean asBool() {
		return (boolean) Types.BOOL.cast(this).get();
	}

	/**
	 * Convert map to 'int'
	 */
	public long asInt() {
		return (long) Types.INT.cast(this).get();
	}

	/**
	 * Convert map to 'real'
	 */
	public double asReal() {
		return (double) Types.REAL.cast(this).get();
	}

	/**
	 * Convert map to 'string'
	 */
	public String asString() {
		return (String) Types.STRING.cast(this).get();
	}

	@Override
	public abstract Value clone();

	/**
	 * Get native object (raw data)
	 */
	public abstract Object get();

	/**
	 * Get value type
	 */
	public abstract Type getType();

	/**
	 * Initialize value
	 */
	protected void init() {
	}

	/**
	 * Parse value from string
	 */
	public void parse(String str) {
		throw new RuntimeException("String parsing unimplemented for type '" + this + "'");
	}

	/**
	 * Set value's native object
	 */
	public abstract void set(Object v);

	public void setValue(Value v) {
		set(v.get());
	}

	@Override
	public String toString() {
		return get() != null ? get().toString() : "null";
	}

}
