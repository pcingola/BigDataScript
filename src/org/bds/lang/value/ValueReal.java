package org.bds.lang.value;

import org.bds.lang.type.Type;
import org.bds.lang.type.Types;

public class ValueReal extends ValuePrimitive {

	private static final long serialVersionUID = 6633302344067725505L;

	double value;

	public ValueReal() {
		super();
	}

	public ValueReal(Double v) {
		super();
		value = v;
	}

	@Override
	public boolean asBool() {
		return value != 0.0;
	}

	@Override
	public long asInt() {
		throw new RuntimeException("Cannot convert type '" + getType() + "' to int");
	}

	@Override
	public double asReal() {
		return value;
	}

	@Override
	public String asString() {
		return Double.toString(value);
	}

	@Override
	public ValueReal clone() {
		return new ValueReal(asReal());
	}

	@Override
	public int compareTo(Value v) {
		if (v instanceof ValueReal) return (int) Math.signum(value - ((ValueReal) v).value);
		return super.compareTo(v);
	}

	@Override
	public boolean equals(Object v) {
		if (v instanceof ValueReal) return value == ((ValueReal) v).value;
		return false;
	}

	@Override
	public Type getType() {
		return Types.REAL;
	}

	@Override
	public int hashCode() {
		return Double.hashCode(value);
	}

	@Override
	public void parse(String str) {
		value = Double.parseDouble(str);
	}

	@Override
	public void setValue(Value v) {
		value = v.asReal();
	}

	@Override
	public String toString() {
		return Double.toString(value);
	}

}
