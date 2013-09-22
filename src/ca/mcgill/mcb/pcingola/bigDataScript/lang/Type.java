package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerializer;

/**
 * Variable type
 * 
 * @author pcingola
 */
public class Type extends BigDataScriptNode implements Comparable<Type> {

	protected static HashMap<String, Type> types = new HashMap<String, Type>();

	public final static Type VOID = Type.get(PrimitiveType.VOID);
	public final static Type BOOL = Type.get(PrimitiveType.BOOL);
	public final static Type INT = Type.get(PrimitiveType.INT);
	public final static Type REAL = Type.get(PrimitiveType.REAL);
	public final static Type STRING = Type.get(PrimitiveType.STRING);
	public final static Type FAKE = Type.get(PrimitiveType.FAKE); // Fake type (for serialization)
	public final static Type ANY = Type.get(PrimitiveType.ANY); // ANY type (wildcard type)

	public final static Boolean BOOL_FALSE = new Boolean(false);

	public final static Long INT_ZERO = new Long(0);
	public final static Long INT_ONE = new Long(1);

	public final static Double REAL_ZERO = new Double(0);
	public final static Double REAL_ONE = new Double(1);

	public final static String STRING_EMPTY = "";

	PrimitiveType primitiveType;

	Scope classScope; // A scope defining all class variables and methods

	/**
	 * Get a type
	 * @param primitiveType
	 * @return
	 */
	public static Type get(PrimitiveType primitiveType) {
		// Get type from hash
		String key = primitiveType.toString();
		Type type = types.get(key);

		// No type available? Create & add
		if (type == null) {
			type = new Type();
			type.primitiveType = primitiveType;
			types.put(key, type);
		}

		return type;
	}

	/**
	 * Get a primitive type
	 * @param primitiveType as a string
	 * @return
	 */
	public static Type get(String primitiveType) {
		return get(PrimitiveType.valueOf(primitiveType));
	}

	/**
	 * Get all available types
	 * @return
	 */
	public static Collection<Type> getAll() {
		return types.values();
	}

	static void put(Type type) {
		types.put(type.getPrimitiveType().toString(), type);
	}

	/**
	 * Reset all types
	 */
	public static void reset() {
		types = new HashMap<String, Type>();

		// Add base types
		put(VOID);
		put(BOOL);
		put(INT);
		put(REAL);
		put(STRING);
		put(FAKE);
		put(ANY);
	}

	public Type() {
		super(null, null);
		primitiveType = PrimitiveType.FAKE;
		classScope = new Scope(null);
	}

	protected Type(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Can we cast "this type" to "type"?
	 * 
	 * Allowed (automatic) conversions:
	 * 	bool -> int, real
	 * 	int -> real
	 *	Anything -> string
	 *	string -> list (list of characters) 	
	 * 
	 * @param type
	 * @return
	 */
	public boolean canCast(Type type) {
		if (equals(type)) return true; // Same class? OK
		if (type.equals(Type.STRING)) return true; // Anything can be converted to string

		// Both of them are primitive?
		if (this.isPrimitiveType() && type.isPrimitiveType()) {
			if (primitiveType == type.primitiveType) return true; // Same type?

			// Allowed conversion?
			if ((primitiveType == PrimitiveType.BOOL) && (type.primitiveType == PrimitiveType.INT)) return true;
			if ((primitiveType == PrimitiveType.BOOL) && (type.primitiveType == PrimitiveType.REAL)) return true;
			if ((primitiveType == PrimitiveType.INT) && (type.primitiveType == PrimitiveType.REAL)) return true;
			if (type.primitiveType == PrimitiveType.STRING) return true;

		}
		return false;
	}

	/**
	 * Cast an object t another
	 * @param toType : Final type for object 'obj'
	 * @param obj : Object
	 * @return
	 */
	public Object cast(Object obj) {

		if (isBool()) {
			if (obj instanceof Boolean) return obj;
		} else if (isInt()) {
			if (obj instanceof Long) return obj;
			if (obj instanceof Boolean) return ((Boolean) obj) ? Type.INT_ONE : Type.INT_ZERO;
		} else if (isReal()) {
			if (obj instanceof Boolean) return ((Boolean) obj) ? Type.REAL_ONE : Type.REAL_ZERO;
			if (obj instanceof Long) return new Double((Long) obj);
			if (obj instanceof Double) return obj;
		} else if (isList()) {
			if (obj instanceof ArrayList) return obj;
		} else if (isMap()) {
			if (obj instanceof HashMap) { //
				return obj; //
			}
		} else if (isString()) return obj.toString();

		throw new RuntimeException("Cannot convert '" + (obj == null ? "null" : obj.getClass().getSimpleName()) + "' to " + this);
	}

	/** 
	 * Compare types
	 * 
	 * @param type
	 * @return
	 */
	@Override
	public int compareTo(Type type) {
		return primitiveType.ordinal() - type.primitiveType.ordinal();
	}

	/**
	 * Get default initialization value
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Object defaultValue() {
		// Set default value
		switch (getPrimitiveType()) {
		case BOOL:
			return Type.BOOL_FALSE;
		case INT:
			return Type.INT_ZERO;
		case REAL:
			return Type.REAL_ZERO;
		case STRING:
			return Type.STRING_EMPTY;
		case LIST:
			return new ArrayList();
		case MAP:
			return new HashMap();
		default:
			throw new RuntimeException("Cannot find default value for type " + this);
		}
	}

	public boolean equals(Type type) {
		return primitiveType == type.primitiveType;
	}

	public Scope getClassScope() {
		return classScope;
	}

	public PrimitiveType getPrimitiveType() {
		return primitiveType;
	}

	/**
	 * Is this type same as 'type'?
	 */
	public boolean is(Type type) {
		return equals(type);
	}

	/**
	 * Is this type 'bool'?
	 */
	public boolean isBool() {
		return primitiveType == PrimitiveType.BOOL;
	}

	public boolean isClass() {
		return false;
	}

	public boolean isFunction() {
		return false;
	}

	/**
	 * Is this type 'string'?
	 */
	public boolean isInt() {
		return primitiveType == PrimitiveType.INT;
	}

	/**
	 * Is this type 'list'?
	 */
	public boolean isList() {
		return false;
	}

	public boolean isList(Type baseType) {
		return false;
	}

	/**
	 * Is this type 'map'?
	 */
	public boolean isMap() {
		return false;
	}

	public boolean isMap(Type baseType) {
		return false;
	}

	public boolean isNative() {
		return false;
	}

	public boolean isPrimitiveType() {
		return true;
	}

	/**
	 * Is this type 'real'?
	 */
	public boolean isReal() {
		return primitiveType == PrimitiveType.REAL;
	}

	/**
	 * Is this type 'string'?
	 */
	public boolean isString() {
		return primitiveType == PrimitiveType.STRING;
	}

	/**
	 * Is this type 'void'?
	 */
	public boolean isVoid() {
		return primitiveType == PrimitiveType.VOID;
	}

	@Override
	protected void parse(ParseTree tree) {
		String typeName = tree.getChild(0).getText();
		primitiveType = PrimitiveType.valueOf(typeName.toUpperCase());
	}

	@Override
	public String serializeSave(BigDataScriptSerializer serializer) {
		return "";
	}

	@Override
	public String toString() {
		return primitiveType != null ? primitiveType.toString().toLowerCase() : "null";
	}

	public String toStringSerializer() {
		return primitiveType.toString();
	}
}
