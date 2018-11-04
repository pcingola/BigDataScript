package org.bds.lang.type;

import java.io.ObjectStreamException;
import java.util.Collection;
import java.util.HashMap;

/**
 * A registry of all available types & classes
 *
 * @author pcingola
 */
public class Types {

	protected static HashMap<String, Type> types = new HashMap<>();

	public final static TypeAny ANY = new TypeAny(); // ANY type (wild-card type)
	public final static TypeBool BOOL = new TypeBool();
	public final static TypeInt INT = new TypeInt();
	public final static TypeNull NULL = new TypeNull(); // NULL type
	public final static TypeReal REAL = new TypeReal();
	public final static TypeString STRING = new TypeString();
	public final static TypeVoid VOID = new TypeVoid(); // Void for side-effect functions

	public final static Type BASE_TYPES[] = { ANY, BOOL, INT, REAL, STRING };

	public static void add(Type type) {
		String key = type.getCanonicalName();
		if (types.get(key) == null) types.put(key, type);
	}

	static public Type get(String canonicalName) {
		return types.get(canonicalName);
	}

	/**
	 * Get all available types
	 */
	public static Collection<Type> getAll() {
		return types.values();
	}

	/**
	 * Reset all types
	 */
	public static void reset() {
		types = new HashMap<>();

		add(NULL);
		add(VOID);

		// Add base types
		for (Type t : BASE_TYPES)
			add(t);

	}

	public static String toStr() {
		StringBuilder sb = new StringBuilder();
		for (String n : types.keySet()) {
			Type t = get(n);
			sb.append(n + ":" + t + "\n");
			if (t.isClass()) sb.append(((TypeClass) t).getClassDeclaration());
		}
		return sb.toString();
	}

	/**
	 * Resolve un-serialization
	 */
	private Object readResolve() throws ObjectStreamException {
		return this;
	}
}
