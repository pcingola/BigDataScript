package org.bds.lang.value;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;

/**
 * Define a value of type list
 *
 * Note: This is a list of 'Value', meaning that each element in the list is itself a value
 *
 * @author pcingola
 */
public class ValueList extends ValueComposite implements Iterable<Value> {

	private static final long serialVersionUID = -9220660671873943097L;

	List<Value> list;

	public ValueList(Type type) {
		this(type, -1);
	}

	public ValueList(Type type, int len) {
		super(type);
		list = len > 0 ? new ArrayList<>(len) : new ArrayList<>();
	}

	public void add(Value v) {
		list.add(v);
	}

	public void addAll(ValueList v) {
		list.addAll(v.get());
	}

	public void addNative(Object o) {
		list.add(type.newValue(o));
	}

	@Override
	public Value clone() {
		ValueList vl = new ValueList(type, list.size());
		vl.addAll(this);
		return vl;
	}

	@Override
	public List<Value> get() {
		return list;
	}

	public Type getElementType() {
		return getType().getElementType();
	}

	@Override
	public TypeList getType() {
		return (TypeList) type;
	}

	/**
	 * Get element number 'idx' from the list wrapped into a 'Value'
	 */
	public Value getValue(long idx) {
		return list.get((int) idx);
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	/**
	 * Is this index out of range?
	 */
	public boolean isIndexOutOfRange(long idx) {
		return idx < 0L || idx > size();
	}

	@Override
	public Iterator<Value> iterator() {
		return list.iterator();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void set(Object v) {
		// !!! TODO: Check list elements class
		list = (List<Value>) v;
	}

	public void setValue(long idx, Value value) {
		ArrayList<Value> list = (ArrayList<Value>) get();

		int iidx = (int) idx;
		if (idx < 0) throw new RuntimeException("Cannot set list element indexed with negative index value: " + idx);

		// Make sure the array is big enough to hold the data
		if (iidx >= list.size()) {
			list.ensureCapacity(iidx + 1);
			Type elemType = ((TypeList) type).getElementType();
			while (list.size() <= iidx)
				list.add(elemType.newDefaultValue());
		}

		list.set((int) idx, value);
	}

	public void setValueNative(long idx, Object obj) {
		setValue(idx, type.newValue(obj));
	}

	public int size() {
		return get().size();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Value v : this) {
			if (sb.length() > 0) sb.append(", ");
			sb.append(v.toString());
		}
		return "[" + sb.toString() + "]";
	}

	@Override
	public int compareTo(Value v) {
		!!!!!!!!!!! COMPARE TYPES !!!!!!!!!!!!!!!!
		return toString().compareTo(v.toString());
	}

	@Override
	public boolean equals(Object v) {
		!!!!!!!!!!! COMPARE TYPES !!!!!!!!!!!!!!!!
		return compareTo(v) == 0;
	}

}
