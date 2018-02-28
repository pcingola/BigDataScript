package org.bds.lang.type;

import org.bds.lang.value.Value;
import org.bds.lang.value.ValueBool;
import org.bds.lang.value.ValueInt;
import org.bds.lang.value.ValueReal;

public class TypeReal extends Type {

	public TypeReal() {
		super("real", new ValueReal(0.0));
	}

	@Override
	public boolean canCast(Type type) {
		return type.is(Types.BOOL) //
				|| type.is(Types.INT) //
		;
	}

	/**
	 * Cast a value 'v' to this type (i.e. convert to type 'real')
	 */
	public Value cast(Value v) {
		Type vt = v.getType();
		if (vt.is(Types.REAL)) return v;

		ValueReal vb = new ValueReal();
		double val = 0;

		if (vt.is(Types.BOOL)) val = ((ValueBool) v).get() ? 1.0 : 0.0;
		else if (vt.is(Types.INT)) val = ((ValueInt) v).get();
		else throw new RuntimeException("Cannot convert value type '" + v.getType() + "' to 'bool'");

		vb.set(val);
		return vb;
	}

}
