package org.bds.lang.value;

import org.bds.lang.type.Types;

public class ValueBool extends ValuePrimitive<Boolean> {

	public final static ValueBool TRUE = new ValueBool(true);
	public final static ValueBool FALSE = new ValueBool(false);

	public ValueBool() {
		super(Types.BOOL);
	}

	public ValueBool(Boolean v) {
		super(Types.BOOL);
		set(v);
	}

	@Override
	public boolean asBool() {
		return value;
	}

	@Override
	public void parse(String str) {
		value = Boolean.parseBoolean(str);
	}

}
