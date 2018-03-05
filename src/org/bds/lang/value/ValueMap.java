package org.bds.lang.value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
		map = new HashMap();
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
		// !!! TODO: Check list elements class
		map = (Map) v;
	}

	public int size() {
		return map.size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Map map = get();
		List keys = new ArrayList<>();
		keys.addAll(map.keySet());
		Collections.sort(keys);

		for (Object o : keys) {
			if (sb.length() > 0) sb.append(", ");
			sb.append(o.toString() + "=" + map.get(o));
		}
		return "{" + sb.toString() + "}";
	}

}
