package org.bds.lang.type;

import org.bds.lang.value.Value;

/**
 * 'Function' value
 * 
 * @author pcingola
 *
 */
public class TypeFunction extends Type {

	public TypeFunction() {
		super("function", null);
	}

	/**
	 * Cast a value 'v' to this type (i.e. convert to type 'function')
	 */
	public Value cast(Value v) {
		return v;
	}

}
