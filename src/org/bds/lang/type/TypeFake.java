package org.bds.lang.type;

public class TypeFake extends TypeUniqueValue {

	public TypeFake() {
		super(PrimitiveType.FAKE);
	}

	@Override
	public boolean isFake() {
		return true;
	}

}
