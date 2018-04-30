package org.bds.lang.type;

import org.bds.lang.value.Value;
import org.bds.lang.value.ValueBool;

public class TypeBool extends TypePrimitive {

	private static final long serialVersionUID = -4022365099135422176L;

	public TypeBool() {
		super(PrimitiveType.BOOL);
	}

	@Override
	public boolean canCastTo(Type type) {
		return type.isBool() || type.isInt() || type.isReal() || type.isString() || type.isAny();
	}

	@Override
	public Object getDefaultValueNative() {
		return Boolean.FALSE;
	}

	@Override
	public boolean isBool() {
		return true;
	}

	@Override
	public Value newDefaultValue() {
		return new ValueBool();
	}

	@Override
	public Value newValue(Object v) {
		return new ValueBool((Boolean) v);
	}

}
