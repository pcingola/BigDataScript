package org.bds.lang.value;

import org.bds.lang.type.Type;
import org.bds.lang.type.Types;

public class ValueString extends ValuePrimitive {

	private static final long serialVersionUID = 6659374250068336012L;
	String value;

	public ValueString() {
		super();
		value = "";
	}

	public ValueString(String v) {
		super();
		value = v;
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
	public boolean equals(Object v) {
		if (v instanceof ValueString) return value.equals(((ValueString) v).value);
		return false;
	}

	@Override
	public Type getType() {
		return Types.STRING;
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public void parse(String str) {
		value = str;
	}

	@Override
	public void setValue(Value v) {
		value = v.asString();
	}

	@Override
	public String toString() {
		return value == null ? "null" : value;
	}

}
