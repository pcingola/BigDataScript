package org.bds.lang.type;

/**
 * An enumeration of primitive types
 *
 * @author pcingola
 */
public enum PrimitiveType {
	ANY, BOOL, CLASS, FAKE, FUNCTION, LIST, MAP, NULL, INT, REAL, STRING, VOID;

	/**
	 * Get 'Type' object from this enum
	 * @return
	 */
	public Type get() {
		switch (this) {
		case ANY:
			return Types.ANY;

		case BOOL:
			return Types.BOOL;

		case CLASS:
			return Types.CLASS;

		case FAKE:
			return Types.FAKE;

		case FUNCTION:
			return Types.FUNCTION;

		case INT:
			return Types.INT;

		case LIST:
			return Types.LIST;

		case MAP:
			return Types.MAP;

		case NULL:
			return Types.NULL;

		case REAL:
			return Types.REAL;

		case STRING:
			return Types.STRING;

		case VOID:
			return Types.VOID;

		default:
			throw new RuntimeException("Type " + this + " not found. This should never happen!");
		}
	}

	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}

}
