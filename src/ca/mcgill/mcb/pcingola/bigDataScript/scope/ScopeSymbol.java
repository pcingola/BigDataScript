package ca.mcgill.mcb.pcingola.bigDataScript.scope;

import java.util.ArrayList;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerialize;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerializer;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
import ca.mcgill.mcb.pcingola.bigDataScript.util.GprString;

/**
 * A symbol in the scope
 * 
 * @author pcingola
 */
public class ScopeSymbol implements BigDataScriptSerialize {

	public static boolean debug = false;

	Type type;
	String name;
	Object value;

	public ScopeSymbol() {
	}

	public ScopeSymbol(String name, Type type) {
		this.name = name;
		this.type = type;
		// Set default value
		if (!type.isFunction()) value = defaultValue();
	}

	public ScopeSymbol(String name, Type type, Object value) {
		this.name = name;
		this.type = type;
		this.value = value;
	}

	/**
	 * Get default initialization value
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected Object defaultValue() {
		// Set default value
		switch (type.getPrimitiveType()) {
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
		default:
			throw new RuntimeException("Cannot find default value for type " + type);
		}

	}

	public String getName() {
		return name;
	}

	public Type getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public void serializeParse(BigDataScriptSerializer serializer) {
		// Parse type
		name = serializer.getNextField();
		type = serializer.getNextFieldType();

		// Parse value
		value = serializer.getNextField(type);
	}

	@Override
	public String serializeSave(BigDataScriptSerializer serializer) {
		return getClass().getSimpleName() //
				+ "\t" + name //
				+ "\t" + BigDataScriptSerializer.TYPE_IDENTIFIER + type.toStringSerializer() //
				+ "\t" + serializer.serializeSaveValue(value) //
				+ "\n";
	}

	public void setValue(Object value) {
		if (debug) Gpr.debug("Setting value:\t" + name + " = " + value);
		this.value = value;
	}

	@Override
	public String toString() {
		String valStr = "null";

		if ((type != null) && type.isString()) valStr = "\"" + GprString.escape(value.toString()) + "\"";
		else valStr = "" + value;

		return type //
				+ " : " + name //
				+ " = " + valStr;
	}

}
