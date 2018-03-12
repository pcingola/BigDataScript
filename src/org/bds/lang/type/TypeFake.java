package org.bds.lang.type;

import org.bds.lang.value.Value;

public class TypeFake extends TypeUniqueValue {

	public TypeFake() {
		super(PrimitiveType.FAKE);
	}

	@Override
	public boolean isFake() {
		return true;
	}

	@Override
	public Value newDefaultValue() {
		return Value.FAKE;
	}

}
