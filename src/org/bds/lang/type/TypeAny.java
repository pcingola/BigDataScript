package org.bds.lang.type;

import org.bds.lang.value.Value;

/**
 * 'Any' map: Generic
 *
 * @author pcingola
 *
 */
public class TypeAny extends TypeUniqueValue {

	private static final long serialVersionUID = 2732411700786104131L;

	public TypeAny() {
		super(PrimitiveType.ANY);
	}

	@Override
	public Value newDefaultValue() {
		return Value.ANY;
	}

}
