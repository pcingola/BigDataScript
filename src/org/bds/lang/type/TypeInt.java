package org.bds.lang.type;

import org.bds.lang.value.Value;
import org.bds.lang.value.ValueInt;

public class TypeInt extends TypePrimitive {

	private static final long serialVersionUID = 1032236278001833639L;

	private static final Integer ZERO = 0;

	public TypeInt() {
		super(PrimitiveType.INT);
	}

	@Override
	public boolean canCastTo(Type type) {
		return type.isBool() || type.isInt() || type.isReal() || type.isString() || type.isAny();
	}

	@Override
	public Object getDefaultValueNative() {
		return ZERO;
	}

	@Override
	public boolean isInt() {
		return true;
	}

	@Override
	public Value newDefaultValue() {
		return new ValueInt();
	}

	@Override
	public Value newValue(Object v) {
		return new ValueInt((Long) v);
	}
}
