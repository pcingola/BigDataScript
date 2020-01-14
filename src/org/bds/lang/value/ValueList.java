package org.bds.lang.value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

	List<Value> list;

	private static final long serialVersionUID = -9220660671873943097L;

	public ValueList(Type type) {
		this(type, -1);
	}

	public ValueList(Type type, int len) {
		super(type);
		list = len > 0 ? new ArrayList<>(len) : new ArrayList<>();
	}

	public void add(int idx, Value v) {
		list.add(idx, v);
	}

	public boolean add(Value v) {
		return list.add(v);
	}

	public void addAll(Collection<? extends Value> vcol) {
		for (Value v : vcol)
			list.add(v);
	}

	public void addAll(ValueList vlist) {
		for (Value v : vlist)
			list.add(v);
	}

	/**
	 * Convert to 'bool': !isEmpty()
	 */
	@Override
	public boolean asBool() {
		return !isEmpty();
	}

	@Override
	public Value clone() {
		ValueList vl = new ValueList(type, list.size());
		vl.addAll(this);
		return vl;
	}

	@Override
	public int compareTo(Value v) {
		int cmp = compareClass(v);
		if (cmp != 0) return cmp;

		// Compare types
		cmp = type.compareTo(v.getType());
		if (cmp != 0) return cmp;

		// Compare all elements
		ValueList vl = (ValueList) v;
		int len = Math.min(size(), vl.size());
		for (int i = 0; i < len; i++) {
			Value v1 = getValue(i);
			Value v2 = getValue(i);
			cmp = v1.compareTo(v2);
			if (cmp != 0) return cmp;
		}

		// May be one of the list has more elements...
		return size() - vl.size();
	}

	public boolean contains(Value v) {
		return list.contains(v);
	}

	public Type getElementType() {
		return getType().getElementType();
	}

	@Override
	public TypeList getType() {
		return (TypeList) type;
	}

	/**
	 * Get element number 'idx' from the list
	 */
	public Value getValue(long idx) {
		int max_idx = list.size();
		int idx_reminder = (int) (idx % max_idx);
		int idx_int = (idx_reminder >= 0 ? idx_reminder : max_idx + idx_reminder);
		return list.get(idx_int);
	}

	@Override
	public int hashCode() {
		return list.hashCode();
	}

	public int indexOf(Value v) {
		return list.indexOf(v);
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public Iterator<Value> iterator() {
		return list.iterator();
	}

	/**
	 * Remove element number 'idx' from the list
	 */
	public Value remove(int idx) {
		return list.remove(idx);
	}

	public boolean remove(Value v) {
		return list.remove(v);
	}

	public void reverse() {
		Collections.reverse(list);
	}

	public void setValue(long idx, Value value) {
		ArrayList<Value> list = (ArrayList<Value>) this.list;

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

	@Override
	public void setValue(Value value) {
		list = ((ValueList) value).list;
	}

	public int size() {
		return list.size();
	}

	public void sort() {
		Collections.sort(list);
	}

	@Override
	protected void toString(StringBuilder sb) {
		int i = 0;
		sb.append('[');
		for (Value v : this) {
			if (i > 0) sb.append(", ");
			if (sb.length() < MAX_TO_STRING_LEN) {
				v.toString(sb);
			} else {
				sb.append("...]");
				return;
			}
			i++;
		}
		sb.append(']');
	}

}
