package org.bds.lang.type;

import org.bds.lang.value.Value;

/**
 * 'Any' map: Generic
 *
 * @author pcingola
 *
 */
public class TypeNull extends TypeClass {

	private static final long serialVersionUID = 8321504654535669530L;

	public TypeNull() {
		super(null);
	}

	@Override
	public boolean isNull() {
		return true;
	}

	@Override
	public Value newDefaultValue() {
		return null;
	}

}
