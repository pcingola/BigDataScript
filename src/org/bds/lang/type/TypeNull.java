package org.bds.lang.type;

/**
 * 'Any' value: Generic
 *
 * @author pcingola
 *
 */
public class TypeNull extends TypeUniqueValue {

	public TypeNull() {
		super(PrimitiveType.NULL);
	}

	@Override
	public boolean isNull() {
		return true;
	}

}
