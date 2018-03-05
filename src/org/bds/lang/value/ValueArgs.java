package org.bds.lang.value;

/**
 * Define values to be passed to a function as arguments
 * @author pcingola
 */
public class ValueArgs extends Value {

	Value values[];

	public ValueArgs(int n) {
		super(null);
		values = new Value[n];
	}

	@Override
	public Object get() {
		return values;
	}

	/**
	 * Get argument number 'idx'
	 */
	public Value getValue(long idx) {
		return values[(int) idx];
	}

	/**
	 * Is this index out of range?
	 */
	public boolean isIndexOutOfRange(long idx) {
		return idx < 0L || idx > values.length;
	}

	@Override
	public void set(Object v) {
		values = (Value[]) v;
	}

	public void setValue(long idx, Value v) {
		values[(int) idx] = v;
	}

	public int size() {
		return values.length;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Value v : values) {
			if (sb.length() > 0) sb.append(", ");
			sb.append(v);
		}
		return "(" + sb.toString() + ")";
	}
}
