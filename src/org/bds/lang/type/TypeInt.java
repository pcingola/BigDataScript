package org.bds.lang.type;

import org.bds.lang.value.Value;
import org.bds.lang.value.ValueBool;
import org.bds.lang.value.ValueInt;

public class TypeInt extends Type {

	public TypeInt() {
		super(PrimitiveType.INT);
		defaultValue = new ValueInt(0L);
	}

	@Override
	public boolean canCast(Type type) {
		return isInt() || isBool();
	}

	/**
	 * Cast a map 'v' to this type (i.e. convert to type 'int')
	 */
	public Value cast(Value v) {
		Type vt = v.getType();
		if (vt.is(Types.INT)) return v;

		ValueInt vb = new ValueInt();
		long val = 0;

		if (vt.isBool()) val = ((ValueBool) v).get() ? 1 : 0;
		else throw new RuntimeException("Cannot convert map type '" + v.getType() + "' to 'bool'");

		vb.set(val);
		return vb;
	}

	@Override
	public boolean isInt() {
		return true;
	}

	@Override
	public Value newValue() {
		return new ValueInt();
	}

}
