package org.bds.lang.type;

import org.bds.lang.value.Value;

/**
 * A type that does not have values
 *
 * @author pcingola
 *
 */
public abstract class TypeUniqueValue extends TypePrimitive {

	public TypeUniqueValue(PrimitiveType primitiveType) {
		super(primitiveType);
	}

	@Override
	public boolean canCastTo(Type type) {
		return true;
	}

	/**
	 * Cast a map 'v' to this type
	 */
	@Override
	public Value cast(Value v) {
		return v;
	}

	@Override
	public Object castNativeObject(Object o) {
		throw new RuntimeException("Cannot cast native object '" + o.getClass().getCanonicalName() + "' to type '" + this + "'");
	}

}
