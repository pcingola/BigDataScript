package org.bds.lang.type;

import java.util.Collection;
import java.util.HashMap;

/**
 * A registry of all available types
 * 
 * @author pcingola
 */
public class Types {

	public final static TypeAny ANY = new TypeAny(); // ANY type (wild-card type)
	public final static TypeBool BOOL = new TypeBool();
	public final static TypeFake FAKE = new TypeFake(); // Fake type (for serialization)
	public final static TypeInt INT = new TypeInt();
	public final static TypeNull NULL = new TypeNull(); // NULL type
	public final static TypeReal REAL = new TypeReal();
	public final static TypeString STRING = new TypeString();
	public final static TypeVoid VOID = new TypeVoid(); // Void for side-effect functions

	protected static HashMap<String, Type> types = new HashMap<>();

	/**
	 * Get all available types
	 */
	public static Collection<Type> getAll() {
		return types.values();
	}

	static void put(Type type) {
		types.put(type.toString(), type);
	}

	static public Type get(String typeStr) {
		return types.get(typeStr.toLowerCase());
	}

	/**
	 * Reset all types
	 */
	public static void reset() {
		types = new HashMap<>();

		// Add base types
		put(ANY);
		put(BOOL);
		put(FAKE);
		put(INT);
		put(NULL);
		put(REAL);
		put(STRING);
		put(VOID);
	}

}
