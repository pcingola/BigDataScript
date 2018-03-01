package org.bds.lang.value;

import org.bds.lang.type.Types;

public class ValueInt extends ValuePrimitive<Long> {

	public ValueInt() {
		super(Types.INT);
	}

	public ValueInt(Long v) {
		super(Types.INT);
		set(v);
	}

	@Override
	public long asInt() {
		return value;
	}

}
