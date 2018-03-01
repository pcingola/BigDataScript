package org.bds.lang.value;

import java.util.Map;

import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;

/**
 * Define a map of type list
 * @author pcingola
 */
@SuppressWarnings("rawtypes")
public class ValueMap extends Value {

	Map map;

	public ValueMap(Type type) {
		super(type);
	}

	@Override
	public Map get() {
		return map;
	}

	/**
	 * Get element 'idx'
	 */
	public Value getValue(Value idx) {
		Object elem = get().get(idx);
		return ((TypeMap) type).getValueType().newValue(elem);
	}

	/**
	 * Get element number 'idx' from the list wrapped into a 'Value'
	 */
	public Value getValue(Value idx) {
		Object elem = get().get(idx);
		return ((TypeList) type).getElementType().newValue(elem);
	}

	@Override
	public void set(Object v) {
		// TODO: Check list elements class
		map = (Map) v;
	}

	public int size() {
		return map.size();
	}

}
