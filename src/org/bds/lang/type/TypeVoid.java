package org.bds.lang.type;

/**
 * 'void' map
 * 
 * @author pcingola
 *
 */
public class TypeVoid extends TypeUniqueValue {

	public TypeVoid() {
		super(PrimitiveType.VOID);
	}

	@Override
	public boolean isVoid() {
		return true;
	}

}
