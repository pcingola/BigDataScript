package org.bds.lang.type;

import org.bds.lang.value.Value;

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

	@Override
	public Value newDefaultValue() {
		return Value.VOID;
	}

}
