package org.bds.lang.type;

import org.bds.lang.value.Value;
import org.bds.lang.value.ValueString;

public class TypeString extends TypePrimitive {

	private static final String EMPTY = "";

	public TypeString() {
		super(PrimitiveType.STRING);
	}

	/**
	 * We can cast anything to a string
	 */
	@Override
	public boolean canCastTo(Type type) {
		return type.isString() || type.isBool();
	}

	/**
	 * Cast a map 'v' to this type (i.e. convert to type 'string')
	 */
	@Override
	public Value cast(Value v) {
		Type vt = v.getType();
		if (vt.isString()) return v;
		return new ValueString(v.asString());
	}

	@Override
	public Object castNativeObject(Object o) {
		if (o instanceof String) return o;
		return o.toString();
	}

	@Override
	public Object getDefaultValueNative() {
		return EMPTY;
	}

	@Override
	public boolean isString() {
		return true;
	}

	@Override
	public Value newDefaultValue() {
		return new ValueString();
	}

}
