package org.bds.lang.value;

import org.bds.lang.type.Types;

public class ValueInt extends ValuePrimitive<Long> {

	public ValueInt() {
		super(Types.INT);
		set(0L);
	}

	public ValueInt(Long v) {
		super(Types.INT);
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
	public void parse(String str) {
		value = Long.parseLong(str);
	}

	public void set(long value) {
		this.value = value;
	}

}
