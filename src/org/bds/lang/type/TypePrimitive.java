package org.bds.lang.type;

/**
 * Primitive types
 *
 * @author pcingola
 */
public abstract class TypePrimitive extends Type {

	private static final long serialVersionUID = 234476895377379821L;

	public TypePrimitive(PrimitiveType primitiveType) {
		super(primitiveType);
	}

	@Override
	public boolean isPrimitive() {
		return true;
	}

}
