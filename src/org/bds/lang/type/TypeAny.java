package org.bds.lang.type;

import org.bds.lang.value.Value;

/**
 * 'Any' map: Generic
 *
 * @author pcingola
 *
 */
public class TypeAny extends TypeUniqueValue {

	public TypeAny() {
		super(PrimitiveType.ANY);
	}

	@Override
	public boolean isAny() {
		return true;
	}

	@Override
	public Value newValue() {
		return Value.ANY;
	}

}
