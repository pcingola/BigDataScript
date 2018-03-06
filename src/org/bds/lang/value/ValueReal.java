package org.bds.lang.value;

import org.bds.lang.type.Types;

public class ValueReal extends ValuePrimitive<Double> {

	public ValueReal() {
		super(Types.REAL);
	}

	public ValueReal(Double v) {
		super(Types.REAL);
		set(v);
	}

	@Override
	public double asReal() {
		return value;
	}

	@Override
	public ValueReal clone() {
		return new ValueReal(asReal());
	}

	@Override
	public void parse(String str) {
		value = Double.parseDouble(str);
	}

}
