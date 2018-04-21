package org.bds.lang.type;

import org.bds.lang.value.Value;
import org.bds.lang.value.ValueNull;

/**
 * 'Any' map: Generic
 *
 * @author pcingola
 *
 */
public class TypeNull extends TypeUniqueValue {

	private static final long serialVersionUID = 8321504654535669530L;

	public TypeNull() {
		super(PrimitiveType.NULL);
	}

	@Override
	public boolean isNull() {
		return true;
	}

	@Override
	public Value newDefaultValue() {
		return ValueNull.NULL;
	}
}
