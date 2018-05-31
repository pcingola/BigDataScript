package org.bds.lang.value;

import java.io.Serializable;

import org.bds.lang.type.Type;
import org.bds.lang.type.Types;

/**
 * Define a value
 * @author pcingola
 */
public abstract class Value implements Serializable, Cloneable, Comparable<Value> {

	private static final long serialVersionUID = 3481924830790274005L;

	public static final int MAX_TO_STRING_LEN = 10 * 1024;

	public static final ValueUnique ANY = ValueUnique.get(Types.ANY);
	public static final ValueUnique VOID = ValueUnique.get(Types.VOID);

	public static Value factory(Object v) {
		if (v instanceof Boolean) return new ValueBool((Boolean) v);
		if (v instanceof Long) return new ValueInt((Long) v);
		if (v instanceof Double) return new ValueReal((Double) v);
		if (v instanceof String) return new ValueString((String) v);
		if (v instanceof Integer) return new ValueInt(((Integer) v).longValue());
		if (v instanceof Float) return new ValueReal(((Float) v).doubleValue());
		if (v == null) return null;
		throw new RuntimeException("Cannot create Value from object class " + v.getClass().getCanonicalName());
	}

	public Value() {
		init();
	}

	/**
	 * Convert to 'bool'
	 */
	public abstract boolean asBool();

	/**
	 * Convert to 'int'
	 */
	public abstract long asInt();

	/**
	 * Convert to 'real'
	 */
	public abstract double asReal();

	/**
	 * Convert to 'string'
	 */
	public abstract String asString();

	@Override
	public abstract Value clone();

	protected int compareClass(Value v) {
		// Different class? Compare names
		if (!getClass().equals(v.getClass())) return getClass().getSimpleName().compareTo(v.getClass().getSimpleName());
		return 0;
	}

	@Override
	public int compareTo(Value v) {
		int cmp = compareClass(v);
		if (cmp != 0) return cmp;
		return toString().compareTo(v.toString());
	}

	@Override
	public boolean equals(Object v) {
		if (!getClass().equals(v.getClass())) return false;
		return compareTo((Value) v) == 0;
	}

	/**
	 * Get value type
	 */
	public abstract Type getType();

	@Override
	public int hashCode() {
		return asString().hashCode();
	}

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

	public abstract void setValue(Value v);

	protected void toString(StringBuilder sb) {
		sb.append(toString());
	}

}
