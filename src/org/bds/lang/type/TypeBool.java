package org.bds.lang.type;

import org.bds.lang.value.Value;
import org.bds.lang.value.ValueBool;
import org.bds.lang.value.ValueInt;
import org.bds.lang.value.ValueReal;
import org.bds.lang.value.ValueString;

public class TypeBool extends Type {

	public TypeBool() {
		super("bool", new ValueBool(Boolean.FALSE));
	}

	@Override
	public boolean canCast(Type type) {
		return equals(type) // Same type?
				|| type.is(Types.INT) //
				|| type.is(Types.REAL) //
				|| type.is(Types.STRING) //
		;
	}

	/**
	 * Cast a value 'v' to this type (i.e. convert to type 'bool')
	 */
	public Value cast(Value v) {
		Type vt = v.getType();
		if (vt.is(Types.BOOL)) return v;

		ValueBool vb = new ValueBool();
		boolean val = false;

		if (vt.is(Types.INT)) val = (((ValueInt) v).get() != 0L);
		else if (vt.is(Types.REAL)) val = (((ValueReal) v).get() != 0.0);
		else if (vt.is(Types.STRING)) val = !(((ValueString) v).get().isEmpty());
		else throw new RuntimeException("Cannot convert value type '" + v.getType() + "' to 'bool'");

		vb.set(val);
		return vb;
	}

}
