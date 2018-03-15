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
	public boolean asBool() {
		return !value.isEmpty();
	}

	@Override
	public long asInt() {
		throw new RuntimeException("Cannot convert type '" + getType() + "' to int");
	}

	@Override
	public double asReal() {
		throw new RuntimeException("Cannot convert type '" + getType() + "' to real");
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
