package org.bds.lang.type;

import org.bds.lang.value.Value;
import org.bds.lang.value.ValueBool;
import org.bds.lang.value.ValueInt;
import org.bds.lang.value.ValueReal;

public class TypeReal extends Type {

	public TypeReal() {
		super(PrimitiveType.REAL);
	}

	@Override
	public boolean canCast(Type type) {
		return type.isBool() || type.isInt();
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

		if (vt.isBool()) val = ((ValueBool) v).get() ? 1.0 : 0.0;
		else if (vt.isInt()) val = ((ValueInt) v).get();
		else throw new RuntimeException("Cannot convert type '" + v.getType() + "' to 'real'");

		vb.set(val);
		return vb;
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
