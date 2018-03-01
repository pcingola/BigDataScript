package org.bds.lang.type;

import org.bds.lang.value.Value;
import org.bds.lang.value.ValueBool;
import org.bds.lang.value.ValueString;

public class TypeString extends Type {

	public TypeString() {
		super(PrimitiveType.STRING);
	}

	/**
	 * Cast a map 'v' to this type (i.e. convert to type 'string')
	 */
	@Override
	public Value cast(Value v) {
		Type vt = v.getType();
		if (vt.isString()) return v;

		ValueString vs = new ValueString();
		String val = "";

		if (vt.isBool()) val = ((ValueBool) v).get() ? "true" : "false";
		else val = v.toString();

		vs.set(val);
		return vs;
	}

	@Override
	public boolean isString() {
		return false;
	}

	@Override
	public Value newValue() {
		return new ValueString();
	}

}
