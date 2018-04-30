package org.bds.lang.value;

import org.bds.lang.type.Type;
import org.bds.lang.type.Types;

public class ValueInt extends ValuePrimitive {

	private static final long serialVersionUID = -2870336402330076093L;

	long value;

	public ValueInt() {
		super();
		set(0L);
	}

	public ValueInt(Long v) {
		super();
		set(v);
	}

	@Override
	public boolean asBool() {
		return value != 0L;
	}

	@Override
	public long asInt() {
		return value;
	}

	@Override
	public double asReal() {
		return value;
	}

	@Override
	public String asString() {
		return Long.toString(value);
	}

	@Override
	public ValueInt clone() {
		return new ValueInt(asInt());
	}

	@Override
	public int compareTo(Value v) {
		if (v instanceof ValueInt) return (int) (value - ((ValueInt) v).value);
		return super.compareTo(v);
	}

	@Override
	public boolean equals(Object v) {
		if (v instanceof ValueInt) return value == ((ValueInt) v).value;
		return false;
	}

	@Override
	public Type getType() {
		return Types.INT;
	}

	@Override
	public int hashCode() {
		return Long.hashCode(value);
	}

	@Override
	public void parse(String str) {
		value = Long.parseLong(str);
	}

	public void set(long value) {
		this.value = value;
	}

	@Override
	public void setValue(Value v) {
		value = v.asInt();
	}

	@Override
	public String toString() {
		return Long.toString(value);
	}
}
