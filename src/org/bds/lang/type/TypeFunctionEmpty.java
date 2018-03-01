package org.bds.lang.type;

import org.bds.lang.Parameters;

/**
 * Empty function type: This is a placeholder for a generic "TypeFunction" definition
 *
 * @author pcingola
 */
public class TypeFunctionEmpty extends TypeFunction {

	/**
	 * Get or create TypeFunction
	 */
	public static TypeFunctionEmpty get() {
		// Get type from hash
		String key = signature(Parameters.EMPTY, Types.VOID);
		TypeFunctionEmpty type = (TypeFunctionEmpty) Types.get(key);
		if (type == null) {
			type = new TypeFunctionEmpty();
			Types.put(type);
		}

		return type;
	}

	private TypeFunctionEmpty() {
		super(Parameters.EMPTY, Types.VOID);
	}

}
