package org.bds.lang.type;

import org.bds.lang.value.Value;
import org.bds.lang.value.ValueBool;
import org.bds.lang.value.ValueInt;

public class TypeInt extends Type {

	private static final Integer ZERO = 0;

	public TypeInt() {
		super(PrimitiveType.INT);
	}

	@Override
	public boolean canCast(Type type) {
		return type.isInt() || type.isBool();
	}

	/**
	 * Cast a map 'v' to this type (i.e. convert to type 'int')
	 */
	@Override
	public Value cast(Value v) {
		Type vt = v.getType();
		if (vt.isInt()) return v;

		ValueInt vb = new ValueInt();
		long val = 0;

		if (vt.isBool()) val = ((ValueBool) v).get() ? 1 : 0;
		else throw new RuntimeException("Cannot convert type '" + v.getType() + "' to 'int'");

		vb.set(val);
		return vb;
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
	public Value newValue() {
		return new ValueInt();
	}
}