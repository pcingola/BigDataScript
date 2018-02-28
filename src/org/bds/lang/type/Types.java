package org.bds.lang.type;

import java.util.Collection;
import java.util.HashMap;

public class Types {

	public final static TypeAny ANY = new TypeAny(); // ANY type (wildcard type)
	public final static TypeNull NULL = new TypeNull(); // NULL type
	public final static TypeBool BOOL = new TypeBool();
	public final static TypeFake FAKE = new TypeFake(); // Fake type (for serialization)
	public final static TypeFunction FUNC = new TypeFunction();
	public final static TypeInt INT = new TypeInt();
	public final static TypeReal REAL = new TypeReal();
	public final static TypeString STRING = new TypeString();
	public final static TypeVoid VOID = new TypeVoid();

	public final static Type[] BASIC_TYPES = { ANY, BOOL, FAKE, FUNC, INT, REAL, STRING, VOID };

	protected static HashMap<String, Type> types = new HashMap<>();

	/**
	 * Get all available types
	 */
	public static Collection<Type> getAll() {
		return types.values();
	}

	static void put(Type type) {
		types.put(type.getTypeName(), type);
	}

	/**
	 * Reset all types
	 */
	public static void reset() {
		types = new HashMap<>();

		// Add base types
		for (Type t : BASIC_TYPES)
			put(t);
	}

}
