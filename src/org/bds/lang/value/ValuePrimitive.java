package org.bds.lang.value;

import org.bds.lang.type.Type;

/**
 * Define a map
 * @author pcingola
 */
public abstract class ValuePrimitive<T extends Object> extends Value {

	protected T value;

	public ValuePrimitive(Type type) {
		super(type);
	}

	@Override
	public abstract ValuePrimitive<T> clone();

	@Override
	public T get() {
		return value;
	}

	@Override
	public void set(Object value) {
		this.value = (T) value;
	}

	@Override
	public String toString() {
		return value != null ? value.toString() : "null";
	}

}
