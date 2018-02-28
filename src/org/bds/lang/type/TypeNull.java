package org.bds.lang.type;

import org.bds.lang.value.Value;

/**
 * 'Any' value: Generic
 * 
 * @author pcingola
 *
 */
public class TypeNull extends Type {

	public TypeNull() {
		super("null", null);
	}

	/**
	 * Cast a value 'v' to this type (i.e. convert to type 'null')
	 */
	public Value cast(Value v) {
		return v;
	}

}
