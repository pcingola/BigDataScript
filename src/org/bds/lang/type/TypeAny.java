package org.bds.lang.type;

import org.bds.lang.value.Value;

/**
 * 'Any' value: Generic
 * 
 * @author pcingola
 *
 */
public class TypeAny extends Type {

	public TypeAny() {
		super("any", null);
	}

	@Override
	public boolean canCast(Type type) {
		return true;
	}

	/**
	 * Cast a value 'v' to this type (i.e. convert to type 'any')
	 */
	public Value cast(Value v) {
		return v;
	}

}
