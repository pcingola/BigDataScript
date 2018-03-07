package org.bds.lang.value;

import java.util.Map;

import org.bds.lang.type.Type;

/**
 * Define a value of an object (i.e. a class)
 * @author pcingola
 */
public class ValueClass extends ValueComposite {

	Map<String, Value> classMap;

	public ValueClass(Type type) {
		super(type);
		init();
	}

	@Override
	public Value clone() {
		// !!! TODO Auto-generated method stub
		throw new RuntimeException("!!! UNIMPLEMENTED");
	}

	/**
	 * Get native object (raw data)
	 */
	@Override
	public Object get() {
		return classMap;
	}

	@Override
	protected void init() {
		set(type.newValue());
		throw new RuntimeException("!!! INITIALIZE ALL FIELDS");
	}

	@Override
	public void parse(String str) {
		throw new RuntimeException("String parsing unimplemented for type '" + this + "'");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void set(Object v) {
		classMap = (Map<String, Value>) v;
	}

}
