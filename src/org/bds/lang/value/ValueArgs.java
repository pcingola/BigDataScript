package org.bds.lang.value;

import java.util.Set;

/**
 * Define values to be passed to a function as arguments
 * @author pcingola
 */
public class ValueArgs extends ValueComposite {

	private static final long serialVersionUID = -721120684142267125L;

	Value values[];

	public ValueArgs(int n) {
		super(null);
		values = new Value[n];
	}

	/**
	 * Clone all values in the argument list
	 */
	@Override
	public ValueArgs clone() {
		ValueArgs va = new ValueArgs(values.length);

		// Populate values with clones for each value
		Value newValues[] = va.values;
		for (int i = 0; i < values.length; i++) {
			newValues[i] = values[i].clone();
		}

		return va;
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

	public void setValue(long idx, Value v) {
		values[(int) idx] = v;
	}

	@Override
	public void setValue(Value v) {
		values = ((ValueArgs) v).values;
	}

	public int size() {
		return values.length;
	}

	@Override
	protected String toString(Set<Value> done) {
		StringBuilder sb = new StringBuilder();
		for (Value v : values) {
			if (sb.length() > 0) sb.append(", ");
			sb.append(v.toString(done));
		}
		return "(" + sb.toString() + ")";
	}

}
