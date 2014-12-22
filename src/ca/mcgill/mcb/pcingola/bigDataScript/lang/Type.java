package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerializer;

/**
 * Variable type
 *
 * @author pcingola
 */
public class Type extends BigDataScriptNode implements Comparable<Type> {

	protected static HashMap<String, Type> types = new HashMap<String, Type>();

	public final static Type ANY = Type.get(PrimitiveType.ANY); // ANY type (wildcard type)
	public final static Type BOOL = Type.get(PrimitiveType.BOOL);
	public final static Type FAKE = Type.get(PrimitiveType.FAKE); // Fake type (for serialization)
	public final static Type FUNC = Type.get(PrimitiveType.FUNC);
	public final static Type INT = Type.get(PrimitiveType.INT);
	public final static Type LIST = Type.get(PrimitiveType.LIST);
	public final static Type MAP = Type.get(PrimitiveType.MAP);
	public final static Type REAL = Type.get(PrimitiveType.REAL);
	public final static Type STRING = Type.get(PrimitiveType.STRING);
	public final static Type VOID = Type.get(PrimitiveType.VOID);

	public final static Boolean BOOL_FALSE = new Boolean(false);

	public final static Long INT_ZERO = new Long(0);
	public final static Long INT_ONE = new Long(1);

	public final static Double REAL_ZERO = new Double(0);
	public final static Double REAL_ONE = new Double(1);

	public final static String STRING_EMPTY = "";

	public final static Object FUNC_EMTPY = new FunctionDeclaration(null, null);

	PrimitiveType primitiveType;

	/**
	 * Get a type
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
	 */
	public static Type get(String primitiveType) {
		return get(PrimitiveType.valueOf(primitiveType));
	}

	/**
	 * Get all available types
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
		for (PrimitiveType pt : PrimitiveType.values())
			put(get(pt));
	}

	public Type() {
		super(null, null);
		primitiveType = PrimitiveType.FAKE;
		// classScope = new Scope(null, null);
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
	 */
	public boolean canCast(Type type) {
		if (equals(type)) return true; // Same class? OK
		if (type.equals(Type.STRING)) return true; // Anything can be converted to string
		if (type.equals(Type.BOOL)) return true; // Anything can be converted to bool

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
	 * Can we cast an object to 'this' type?
	 */
	public boolean canCastObject(Object obj) {
		if (isBool()) return (obj instanceof Boolean);
		else if (isVoid()) return true;
		else if (isInt()) return (obj instanceof Long) || (obj instanceof Boolean);
		else if (isReal()) return (obj instanceof Boolean) || (obj instanceof Long) || (obj instanceof Double);
		else if (isList()) return (obj instanceof ArrayList);
		else if (isMap()) return (obj instanceof HashMap);
		else if (isString()) return true;
		return false;
	}

	/**
	 * Cast an object t another
	 * @param toType : Final type for object 'obj'
	 * @param obj : Object
	 */
	@SuppressWarnings("rawtypes")
	public Object cast(Object obj) {
		if (isBool()) {
			if (obj instanceof Boolean) return obj;
			if (obj instanceof Long) return ((Long) obj) != 0;
			if (obj instanceof Integer) return ((Long) obj) != 0;
			if (obj instanceof Double) return ((Double) obj) != 0.0;
			if (obj instanceof String) return !((String) obj).isEmpty();
			if (obj instanceof List) return !((List) obj).isEmpty();
			if (obj instanceof Map) return !((Map) obj).isEmpty();
		} else if (isInt()) {
			if (obj instanceof Long) return obj;
			if (obj instanceof Integer) return (long) obj;
			if (obj instanceof Boolean) return ((Boolean) obj) ? Type.INT_ONE : Type.INT_ZERO;
		} else if (isReal()) {
			if (obj instanceof Boolean) return ((Boolean) obj) ? Type.REAL_ONE : Type.REAL_ZERO;
			if (obj instanceof Long) return new Double((Long) obj);
			if (obj instanceof Integer) return new Double((Integer) obj);
			if (obj instanceof Double) return obj;
		} else if (isList()) {
			if (obj instanceof ArrayList) return obj;
		} else if (isMap()) {
			if (obj instanceof HashMap) { //
				return obj; //
			}
		} else if (isFunction()) {
			if (obj instanceof FunctionDeclaration) { //
				return obj; //
			}
		} else if (isString()) return obj.toString();

		throw new RuntimeException("Cannot convert '" + (obj == null ? "null" : obj.getClass().getSimpleName()) + "' to " + this);
	}

	/**
	 * Compare types
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
		case FUNC:
			return Type.FUNC_EMTPY;
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

	//	public Scope getClassScope() {
	//		return classScope;
	//	}

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
	@Override
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
	@Override
	public boolean isInt() {
		return primitiveType == PrimitiveType.INT;
	}

	/**
	 * Is this type 'list'?
	 */
	@Override
	public boolean isList() {
		return false;
	}

	@Override
	public boolean isList(Type baseType) {
		return false;
	}

	/**
	 * Is this type 'map'?
	 */
	@Override
	public boolean isMap() {
		return false;
	}

	@Override
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
	@Override
	public boolean isReal() {
		return primitiveType == PrimitiveType.REAL;
	}

	/**
	 * Is this type 'string'?
	 */
	@Override
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
		return ""; // We don't save data type nodes
	}

	@Override
	public String toString() {
		return primitiveType != null ? primitiveType.toString().toLowerCase() : "null";
	}

	public String toStringSerializer() {
		return primitiveType.toString();
	}
}
