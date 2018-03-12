package org.bds.lang.type;

import org.bds.lang.value.Value;
import org.bds.lang.value.ValueBool;
import org.bds.lang.value.ValueList;
import org.bds.lang.value.ValueMap;

public class TypeBool extends TypePrimitive {

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

		if (vt.isInt()) val = v.asInt() != 0L;
		else if (vt.isReal()) val = v.asReal() != 0.0;
		else if (vt.isString()) val = !v.asString().isEmpty();
		else if (vt.isList()) val = ((ValueList) v).size() > 0;
		else if (vt.isMap()) val = ((ValueMap) v).size() > 0;
		else throw new RuntimeException("Cannot convert type '" + v.getType() + "' to 'bool'");

		vb.set(val);
		return vb;
	}

	@Override
	public Object castNativeObject(Object o) {
		if (o instanceof Boolean) return o;
		if (o instanceof Long) return ((Long) o) != 0;
		if (o instanceof Double) return ((Double) o) != 0.0;
		if (o instanceof String) return !((String) o).isEmpty();
		throw new RuntimeException("Cannot cast native object '" + o.getClass().getCanonicalName() + "' to type '" + this + "'");
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
	public Value newDefaultValue() {
		return new ValueBool();
	}

}
