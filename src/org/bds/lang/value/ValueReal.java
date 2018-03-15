package org.bds.lang.value;

import org.bds.lang.type.Type;
import org.bds.lang.type.Types;

public class ValueReal extends ValuePrimitive {

	double value;

	public ValueReal() {
		super();
	}

	public ValueReal(Double v) {
		super();
		set(v);
	}

	@Override
	public boolean asBool() {
		return value != 0.0;
	}

	@Override
	public long asInt() {
		throw new RuntimeException("Cannot convert type '" + getType() + "' to real");
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
	public Double get() {
		return Double.valueOf(value);
	}

	@Override
	public Type getType() {
		return Types.REAL;
	}

	@Override
	public void parse(String str) {
		value = Double.parseDouble(str);
	}

	@Override
	public void set(Object v) {
		value = (Double) v;
	}

}
