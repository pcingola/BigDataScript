package org.bds.lang.type;

import org.bds.lang.value.Value;

/**
 * A type that does not have values
 *
 * @author pcingola
 *
 */
public abstract class TypeUniqueValue extends TypePrimitive {

	private static final long serialVersionUID = -6886551737572639463L;

	public TypeUniqueValue(PrimitiveType primitiveType) {
		super(primitiveType);
	}

	@Override
	public boolean canCastTo(Type type) {
		return true;
	}

	@Override
	public Value newValue(Object v) {
		throw new RuntimeException("Unimplemented. This method should never be invoked!");
	}

}
