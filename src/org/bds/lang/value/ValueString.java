package org.bds.lang.value;

import org.bds.lang.type.Types;

public class ValueString extends ValuePrimitive<String> {

	public ValueString() {
		super(Types.STRING);
		value = "";
	}

	public ValueString(String v) {
		super(Types.STRING);
		set(v);
	}

	@Override
	public String asString() {
		return value;
	}

	@Override
	public ValueString clone() {
		return new ValueString(asString());
	}

	@Override
	public void parse(String str) {
		value = str;
	}

}
