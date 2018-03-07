package org.bds.lang.type;

import org.bds.lang.value.Value;
import org.bds.lang.value.ValueReal;

public class TypeReal extends TypePrimitive {

	private static final Double ZERO = 0.0;

	public TypeReal() {
		super(PrimitiveType.REAL);
	}

	@Override
	public boolean canCastTo(Type type) {
		return type.isReal() || type.isBool() || type.isString();
	}

	/**
	 * Cast a map 'v' to this type (i.e. convert to type 'real')
	 */
	@Override
	public Value cast(Value v) {
		Type vt = v.getType();
		if (vt.isReal()) return v;

		ValueReal vb = new ValueReal();
		double val = 0;

		if (vt.isBool()) val = v.asBool() ? 1.0 : 0.0;
		else if (vt.isInt()) val = v.asInt();
		else throw new RuntimeException("Cannot convert type '" + v.getType() + "' to 'real'");

		vb.set(val);
		return vb;
	}

	@Override
	public Object castNativeObject(Object o) {
		if (o instanceof Double) return o;
		if (o instanceof Boolean) return ((Boolean) o) ? 1.0 : 0.0;
		if (o instanceof Long) return ((Long) o).doubleValue();
		throw new RuntimeException("Cannot cast native object '" + o.getClass().getCanonicalName() + "' to type '" + this + "'");
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
	public Value newValue() {
		return new ValueReal();
	}

}
