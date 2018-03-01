package org.bds.lang.type;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.value.Value;
import org.bds.serialize.BdsSerializer;

/**
 * Variable type
 *
 * @author pcingola
 */
public abstract class Type extends BdsNode implements Comparable<Type> {

	String typeName;
	Value defaultValue;

	public static int VOID;

	protected Type(BdsNode parent, ParseTree tree) {
		super(parent, tree);
		typeName = tree.getChild(0).getText();
	}

	public Type(String typeName) {
		super(null, null);
		this.typeName = typeName;
	}

	public Type(String typeName, Value defaultValue) {
		super(null, null);
		this.typeName = typeName;
		this.defaultValue = defaultValue;
	}

	/**
	 * Can 'type' be casted to 'this'?
	 * @param type: The type to be casted to 'this' type
	 */
	@Override
	public boolean canCast(Type type) {
		return equals(type); // Same type
	}

	public void checkCanCast(Type type, CompilerMessages compilerMessages) {
		if (!returnType.isReturnTypesNotNull() && !returnType.canCast(type)) {
			compilerMessages.add(this, "Cannot cast " + type + " to " + returnType, MessageType.ERROR);
		}
	}

	/**
	 * Compare types
	 */
	@Override
	public int compareTo(Type type) {
		return typeName.compareTo(type.getTypeName());
	}

	public boolean equals(Type type) {
		return typeName.equals(type.getTypeName());
	}

	/**
	 * Get default initialization value
	 */
	public Value getDefaultValue() {
		return defaultValue;
	}

	public String getTypeName() {
		return typeName;
	}

	/**
	 * Is this type same as 'type'?
	 */
	public boolean is(Type type) {
		return equals(type);
	}

	/**
	 * Create a new value if this type
	 */
	public abstract Value newValue();

	/**
	 * Create a new value if this type and set it to 'v'
	 */
	public Value newValue(Object v) {
		Value value = newValue();
		value.set(v);
		return value;
	}

	@Override
	protected void parse(ParseTree tree) {
		throw new RuntimeException("UNIMPLEMENTED PARSE TYPE");
	}

	@Override
	public String serializeSave(BdsSerializer serializer) {
		return ""; // We don't save data type nodes
	}

	@Override
	public String toString() {
		return typeName;
	}

	public String toStringSerializer() {
		return typeName;
	}
}
