package org.bds.lang.value;

import org.bds.lang.type.Type;
import org.bds.lang.type.Types;

public class ValueString extends ValuePrimitive {

	String value;

	public ValueString() {
		super();
		value = "";
	}

	public ValueString(String v) {
		super();
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
	public String get() {
		return value;
	}

	@Override
	public Type getType() {
		return Types.STRING;
	}

	@Override
	public void parse(String str) {
		value = str;
	}

	@Override
	public void set(Object v) {
		value = v.toString();
	}

	@Override
	public String toString() {
		return value == null ? "null" : '"' + value + '"';
	}

}
