package org.bds.lang.type;

import org.bds.lang.value.Value;
import org.bds.lang.value.ValueBool;
import org.bds.lang.value.ValueInt;

public class TypeInt extends Type {

	public TypeInt() {
		super("int", new ValueInt(0L));
	}

	@Override
	public boolean canCast(Type type) {
		return type.is(Types.BOOL) //
				|| type.is(Types.INT) //
		;
	}

	/**
	 * Cast a value 'v' to this type (i.e. convert to type 'int')
	 */
	public Value cast(Value v) {
		Type vt = v.getType();
		if (vt.is(Types.INT)) return v;

		ValueInt vb = new ValueInt();
		long val = 0;

		if (vt.is(Types.BOOL)) val = ((ValueBool) v).get() ? 1 : 0;
		else throw new RuntimeException("Cannot convert value type '" + v.getType() + "' to 'bool'");

		vb.set(val);
		return vb;
	}

	@Override
	public Value newValue() {
		return new ValueInt();
	}

}
