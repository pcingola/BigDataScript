package org.bds.lang.type;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.nativeMethods.MethodNative;
import org.bds.lang.value.Value;
import org.bds.serialize.BdsSerializer;
import org.bds.symbol.SymbolTable;

/**
 * Variable type
 *
 * @author pcingola
 */
public abstract class Type extends BdsNode implements Comparable<Type> {

	protected PrimitiveType primitiveType;
	protected SymbolTable symbolTable; // A type requires a SymbolTable to define all methods related to this type / class

	protected Type(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	public Type(PrimitiveType primitiveType) {
		super(null, null);
		this.primitiveType = primitiveType;
		initialize();
	}

	/**
	 * Add all library methods here
	 */
	protected void addNativeMethods() {
		List<MethodNative> methods = declateNativeMethods();
		for (MethodNative method : methods) {
			TypeFunction tf = new TypeFunction(method);
			getSymbolTable().add(method.getFunctionName(), tf);
		}
	}

	/**
	 * Can this type be casted to 'type'?
	 */
	public boolean canCastTo(Type type) {
		return equals(type); // Same type
	}

	/**
	 * Cast value 'v' to this type
	 */
	public Value cast(Value v) {
		if (is(v.getType())) return v; // Same type? No need to cast
		throw new RuntimeException("Cannot cast type '" + v.getType() + "' to type '" + this + "'");
	}

	/**
	 * This is used when casting Java object (e.g. running native functions & methods)
	 */
	public abstract Object castNativeObject(Object o);
	// { throw new RuntimeException("Cannot cast native object '" + o.getClass().getCanonicalName() + "' to type '" + this + "'"); }

	public void checkCanCast(Type type, CompilerMessages compilerMessages) {
		if (returnType.isReturnTypesNotNull() && !returnType.canCastTo(type)) {
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

	/**
	 * Declare native methods (default is 'object' level methods
	 */
	protected List<MethodNative> declateNativeMethods() {
		return new ArrayList<>(); // Empty list
	}

	public boolean equals(Type type) {
		return primitiveType.equals(type.primitiveType);
	}

	/**
	 * Default value (native object). Most of the time this value is null
	 * @return
	 */
	public Object getDefaultValueNative() {
		return null;
	}

	public PrimitiveType getPrimitiveType() {
		return primitiveType;
	}

	@Override
	public SymbolTable getSymbolTable() {
		return symbolTable;
	}

	@Override
	protected void initialize() {
		returnType = this;
		symbolTable = new SymbolTable(this);
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

	public boolean isFunctionNative() {
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
	 * Create a new value (properly initialzed)
	 */
	public abstract Value newValue();

	/**
	 * Create a new map if this type and set it to 'v'
	 */
	public Value newValue(Object v) {
		Value value = newValue();
		value.set(v);
		return value;
	}

	@Override
	protected void parse(ParseTree tree) {
		throw new RuntimeException("This method should never be called!");
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
