package org.bds.lang.value;

import org.bds.lang.type.Types;

public class ValueBool extends ValuePrimitive<Boolean> {

	public ValueBool() {
		super(Types.BOOL);
	}

	public ValueBool(Boolean v) {
		super(Types.BOOL);
		set(v);
	}

}
