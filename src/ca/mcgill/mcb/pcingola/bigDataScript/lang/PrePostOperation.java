package ca.mcgill.mcb.pcingola.bigDataScript.lang;

/**
 * Pre/Post pperation type.
 * E.g. i++ , --j
 * 
 */
public enum PrePostOperation {
	INCREMENT, DECREMENT;

	/**
	 * Parse a string
	 * @param opStr
	 * @return
	 */
	public static PrePostOperation parse(String opStr) {
		if (opStr.equals("++")) return PrePostOperation.INCREMENT;
		else if (opStr.equals("--")) return PrePostOperation.DECREMENT;
		throw new RuntimeException("Cannot parse string'" + opStr + "'");
	}
};
