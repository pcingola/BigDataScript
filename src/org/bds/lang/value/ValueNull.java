package org.bds.lang.value;

import org.bds.lang.type.Type;
import org.bds.lang.type.Types;

public class ValueNull extends ValuePrimitive {

	private static final long serialVersionUID = -7431146579571420544L;

	public static final ValueNull NULL = new ValueNull();

	private ValueNull() {
		super();
	}

	@Override
	public boolean asBool() {
		return false;
	}

	@Override
	public long asInt() {
		return 0L;
	}

	/**
	 * Convert to 'real'
	 */
	@Override
	public double asReal() {
		return 0.0;
	}

	@Override
	public String asString() {
		return "null";
	}

	@Override
	public ValueNull clone() {
		return this;
	}

	@Override
	public boolean equals(Object v) {
		return v instanceof ValueNull;
	}

	@Override
	public Type getType() {
		return Types.NULL;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public void parse(String str) {
	}

	@Override
	public void setValue(Value v) {
	}

	@Override
	public String toString() {
		return "null";
	}

}
