package org.bds.lang.value;

import org.bds.lang.type.Type;
import org.bds.lang.type.Types;

/**
 * Define a value
 * @author pcingola
 */
public abstract class Value {

	public static final ValueUnique ANY = ValueUnique.get(Types.ANY);
	public static final ValueUnique FAKE = ValueUnique.get(Types.FAKE);
	public static final ValueUnique NULL = ValueUnique.get(Types.NULL);
	public static final ValueUnique VOID = ValueUnique.get(Types.VOID);

	protected Type type;

	public static Value factory(Object v) {
		if (v instanceof Boolean) return new ValueBool((Boolean) v);
		if (v instanceof Long) return new ValueInt((Long) v);
		if (v instanceof Double) return new ValueReal((Double) v);
		if (v instanceof String) return new ValueString((String) v);
		if (v instanceof Integer) return new ValueInt(((Integer) v).longValue());
		if (v instanceof Float) return new ValueReal(((Float) v).doubleValue());
		throw new RuntimeException("Cannot create map from object class " + v.getClass().getCanonicalName());
	}

	public Value(Type type) {
		this.type = type;
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

	/**
	 * Get native object (raw data)
	 */
	public abstract Object get();

	public Type getType() {
		return type;
	}

	protected void init() {
	}

	public void parse(String str) {
		throw new RuntimeException("String parsing unimplemented for type '" + this + "'");
	}

	public abstract void set(Object v);

}
