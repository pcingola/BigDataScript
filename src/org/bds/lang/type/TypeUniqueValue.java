package org.bds.lang.type;

import org.bds.lang.value.Value;

/**
 * A type that does not have values
 *
 * @author pcingola
 *
 */
public class TypeUniqueValue extends Type {

	public TypeUniqueValue(PrimitiveType primitiveType) {
		super(primitiveType, null);
	}

	@Override
	public boolean canCast(Type type) {
		return true;
	}

	/**
	 * Cast a value 'v' to this type
	 */
	public Value cast(Value v) {
		return v;
	}

	@Override
	public Value newValue() {
		throw new RuntimeException("Cannot create new value of type '" + primitiveType.toString() + "'");
	}

}
