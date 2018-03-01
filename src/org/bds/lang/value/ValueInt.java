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

	public void set(long value) {
		this.value = value;
	}

	@Override
	public long asInt() {
		return value;
	}

	@Override
	public void parse(String str) {
		value = Long.parseLong(str);
	}

}
