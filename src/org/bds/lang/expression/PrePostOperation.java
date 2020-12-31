package org.bds.lang.expression;

/**
 * Pre/Post pperation type.
 * E.g.:
 * 	```
 * 		i++
 * 		--j
 *	```
 */
public enum PrePostOperation {
	INCREMENT, DECREMENT;

	/**
	 * Parse a string
	 */
	public static PrePostOperation parse(String opStr) {
		if (opStr.equals("++")) return PrePostOperation.INCREMENT;
		else if (opStr.equals("--")) return PrePostOperation.DECREMENT;
		throw new RuntimeException("Cannot parse string'" + opStr + "'");
	}

	@Override
	public String toString() {
		switch (this) {
		case INCREMENT:
			return "++";

		case DECREMENT:
			return "--";

		default:
			throw new RuntimeException("Unhandled operation " + this);
		}

	}
};
