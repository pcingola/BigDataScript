package org.bds.lang.type;

import org.bds.lang.value.Value;
import org.bds.lang.value.ValueBool;
import org.bds.lang.value.ValueInt;
import org.bds.lang.value.ValueReal;
import org.bds.lang.value.ValueString;

public class TypeString extends Type {

	public TypeString() {
		super("string", new ValueReal(0.0));
	}

	/**
	 * Cast a value 'v' to this type (i.e. convert to type 'string')
	 */
	public Value cast(Value v) {
		Type vt = v.getType();
		if (vt.is(Types.STRING)) return v;

		ValueString vs = new ValueString();
		String val = "";

		if (vt.is(Types.BOOL)) val = ((ValueBool) v).get() ? "true" : "false";
		else if (vt.is(Types.INT)) val = ((ValueInt) v).get().toString();
		else if (vt.is(Types.REAL)) val = ((ValueInt) v).get().toString();
		else val = v.toString();

		vs.set(val);
		return vs;
	}

	@Override
	public Value newValue() {
		return new ValueString();
	}

}
