package org.bds.lang.value;

import java.util.Map;

import org.bds.lang.type.Type;
import org.bds.lang.type.TypeMap;

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
	 * Get element 'idx' wrapped into a 'Value'
	 */
	public Value getValue(Value idx) {
		Object oidx = idx.get();
		Object oelem = get().get(oidx);
		return ((TypeMap) type).getValueType().newValue(oelem);
	}

	@SuppressWarnings("unchecked")
	public void put(Value key, Value val) {
		Map map = get();
		map.put(key.get(), val.get());
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
