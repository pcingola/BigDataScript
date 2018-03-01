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

	protected PrimitiveType primitiveType;
	protected Value defaultValue;

	protected Type(BdsNode parent, ParseTree tree) {
		super(parent, tree);
		String typeName = tree.getChild(0).getText();
		primitiveType = PrimitiveType.valueOf(typeName);
		returnType = this;
	}

	public Type(PrimitiveType primitiveType) {
		super(null, null);
		this.primitiveType = primitiveType;
		returnType = this;
	}

	public Type(PrimitiveType primitiveType, Value defaultValue) {
		super(null, null);
		this.primitiveType = primitiveType;
		this.defaultValue = defaultValue;
		returnType = this;
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
		return primitiveType.compareTo(type.primitiveType);
	}

	public boolean equals(Type type) {
		return primitiveType.equals(type.primitiveType);
	}

	/**
	 * Get default initialization value
	 */
	public Value getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Is this type same as 'type'?
	 */
	public boolean is(Type type) {
		return equals(type);
	}

	@Override
	public boolean isAny() {
		return false;
	}

	@Override
	public boolean isBool() {
		return false;
	}

	@Override
	public boolean isClass() {
		return false;
	}

	@Override
	public boolean isFake() {
		return false;
	}

	@Override
	public boolean isFunction() {
		return false;
	}

	@Override
	public boolean isInt() {
		return false;
	}

	@Override
	public boolean isList() {
		return false;
	}

	@Override
	public boolean isList(Type elementType) {
		return false;
	}

	@Override
	public boolean isMap() {
		return false;
	}

	@Override
	public boolean isMap(Type keyType, Type valueType) {
		return false;
	}

	@Override
	public boolean isNull() {
		return false;
	}

	@Override
	public boolean isReal() {
		return false;
	}

	@Override
	public boolean isString() {
		return false;
	}

	@Override
	public boolean isVoid() {
		return false;
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
		return primitiveType.toString();
	}

	public String toStringSerializer() {
		return toString();
	}
}
