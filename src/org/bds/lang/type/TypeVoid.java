package org.bds.lang.type;

import org.bds.lang.value.Value;

/**
 * 'Any' value: Generic
 * 
 * @author pcingola
 *
 */
public class TypeVoid extends Type {

	public TypeVoid() {
		super("void", null);
	}

	/**
	 * Cast a value 'v' to this type (i.e. convert to type 'void')
	 */
	public Value cast(Value v) {
		return v;
	}

}
