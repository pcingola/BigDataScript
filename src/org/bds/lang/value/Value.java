package org.bds.lang.value;

import org.bds.lang.type.Type;
import org.bds.lang.type.Types;

/**
 * Define a value
 * @author pcingola
 */
public abstract class Value {

	protected Type type;

	public static Value factory(Object v) {
		if (v instanceof Boolean) return new ValueBool((Boolean) v);
		if (v instanceof Long) return new ValueInt((Long) v);
		if (v instanceof Double) return new ValueReal((Double) v);
		if (v instanceof String) return new ValueString((String) v);
		if (v instanceof Integer) return new ValueInt(((Integer) v).longValue());
		if (v instanceof Float) return new ValueReal(((Float) v).doubleValue());
		throw new RuntimeException("Cannot create value from object class " + v.getClass().getCanonicalName());
	}

	public Value(Type type) {
		this.type = type;
		init();
	}

	/**
	 * Convert value to 'bool'
	 */
	public boolean asBool() {
		return (boolean) Types.BOOL.cast(this).get();
	}

	/**
	 * Convert value to 'int'
	 */
	public long asInt() {
		return (long) Types.INT.cast(this).get();
	}

	/**
	 * Convert value to 'real'
	 */
	public double asReal() {
		return (double) Types.REAL.cast(this).get();
	}

	/**
	 * Convert value to 'string'
	 */
	public String asString() {
		return (String) Types.STRING.cast(this).get();
	}

	public abstract Object get();

	public Type getType() {
		return type;
	}

	protected void init() {
		set(type.getDefaultValue());
	}

	public abstract void set(Object v);

}
