package org.bds.lang.type;

import org.bds.lang.value.Value;
import org.bds.lang.value.ValueString;

public class TypeString extends TypePrimitive {

	private static final long serialVersionUID = 6094530000399176225L;

	private static final String EMPTY = "";

	public TypeString() {
		super(PrimitiveType.STRING);
	}

	/**
	 * We can cast anything to a string
	 */
	@Override
	public boolean canCastTo(Type type) {
		return type.isString() || type.isBool() || type.isAny();
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

	@Override
	public Value newValue(Object v) {
		return new ValueString(v.toString());
	}

}
