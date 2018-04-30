package org.bds.lang.type;

import org.bds.lang.value.Value;
import org.bds.lang.value.ValueReal;

public class TypeReal extends TypePrimitive {

	private static final long serialVersionUID = 786178415084699701L;

	private static final Double ZERO = 0.0;

	public TypeReal() {
		super(PrimitiveType.REAL);
	}

	@Override
	public boolean canCastTo(Type type) {
		return type.isReal() || type.isBool() || type.isString() || type.isAny();
	}

	@Override
	public Object getDefaultValueNative() {
		return ZERO;
	}

	@Override
	public boolean isReal() {
		return true;
	}

	@Override
	public Value newDefaultValue() {
		return new ValueReal();
	}

	@Override
	public Value newValue(Object v) {
		return new ValueReal((Double) v);
	}

}
