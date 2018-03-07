package org.bds.lang.value;

import org.bds.lang.type.Type;
import org.bds.lang.type.Types;

public class ValueInt extends ValuePrimitive {

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
	public long asInt() {
		return value;
	}

	@Override
	public ValueInt clone() {
		return new ValueInt(asInt());
	}

	@Override
	public Long get() {
		return Long.valueOf(value);
	}

	@Override
	public Type getType() {
		return Types.INT;
	}

	@Override
	public void parse(String str) {
		value = Long.parseLong(str);
	}

	public void set(long value) {
		this.value = value;
	}

	@Override
	public void set(Object v) {
		value = (Long) v;
	}

}
