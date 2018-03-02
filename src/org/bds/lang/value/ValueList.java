package org.bds.lang.value;

import java.util.ArrayList;
import java.util.List;

import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;

/**
 * Define a map of type list
 * @author pcingola
 */
@SuppressWarnings("rawtypes")
public class ValueList extends Value {

	List value;

	public ValueList(Type type) {
		super(type);
		value = new ArrayList<>();
	}

	@Override
	public List get() {
		return value;
	}

	/**
	 * Get element number 'idx' from the list wrapped into a 'Value'
	 */
	public Value getValue(long idx) {
		Object elem = get().get((int) idx);
		return ((TypeList) type).getElementType().newValue(elem);
	}

	/**
	 * Is this index out of range?
	 */
	public boolean isIndexOutOfRange(long idx) {
		List list = get();
		return idx < 0L || idx > list.size();
	}

	@Override
	public void set(Object v) {
		// TODO: Check list elements class
		value = (List) v;
	}

	@SuppressWarnings("unchecked")
	public void setValue(long idx, Value value) {
		ArrayList list = (ArrayList) get();

		int iidx = (int) idx;
		if (idx < 0) throw new RuntimeException("Cannot set list element indexed with negative index value: " + idx);

		// Make sure the array is big enough to hold the data
		if (iidx >= list.size()) {
			list.ensureCapacity(iidx + 1);
			Type elemType = ((TypeList) type).getElementType();
			while (list.size() <= iidx)
				list.add(elemType.getDefaultValueNative());
		}

		list.set((int) idx, value.get());
	}

	@SuppressWarnings("unchecked")
	public void setValueNative(long idx, Object obj) {
		get().set((int) idx, obj);
	}

	public int size() {
		return get().size();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		List list = get();
		for (Object o : list) {
			if (sb.length() > 0) sb.append(", ");
			sb.append(o.toString());
		}
		return "[" + sb.toString() + "]";
	}

}