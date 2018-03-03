package org.bds.lang.type;

import org.bds.lang.value.Value;
import org.bds.lang.value.ValueBool;
import org.bds.lang.value.ValueInt;
import org.bds.lang.value.ValueReal;
import org.bds.lang.value.ValueString;

public class TypeBool extends Type {

	public TypeBool() {
		super(PrimitiveType.BOOL);
	}

	@Override
	public boolean canCastTo(Type type) {
		return type.isBool() || type.isInt() || type.isReal() || type.isString();
	}

	/**
	 * Cast a map 'v' to this type (i.e. convert to type 'bool')
	 */
	@Override
	public Value cast(Value v) {
		Type vt = v.getType();
		if (vt.isBool()) return v;

		ValueBool vb = new ValueBool();
		boolean val = false;

		if (vt.isInt()) val = (((ValueInt) v).get() != 0L);
		else if (vt.isReal()) val = (((ValueReal) v).get() != 0.0);
		else if (vt.isString()) val = !(((ValueString) v).get().isEmpty());
		else throw new RuntimeException("Cannot convert type '" + v.getType() + "' to 'bool'");

		vb.set(val);
		return vb;
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
	public Value newValue() {
		return new ValueBool();
	}

}
