package org.bds.lang.value;

import java.util.List;

import org.bds.lang.type.Type;

/**
 * Define a value of type list
 * @author pcingola
 */
@SuppressWarnings("rawtypes")
public class ValueList extends Value {

	protected Type type;
	List value;

	public ValueList(Type type) {
		super(type);
	}

	@Override
	public void set(Object v) {
		// TODO: Check list elements class
		value = (List) v;
	}

	@Override
	public Object get() {
		return value;
	}

}
