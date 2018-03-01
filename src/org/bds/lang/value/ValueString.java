package org.bds.lang.value;

import org.bds.lang.type.Types;

public class ValueString extends ValuePrimitive<String> {

	public ValueString() {
		super(Types.STRING);
	}

	public ValueString(String v) {
		super(Types.STRING);
		set(v);
	}

	@Override
	public String asString() {
		return value;
	}

}
