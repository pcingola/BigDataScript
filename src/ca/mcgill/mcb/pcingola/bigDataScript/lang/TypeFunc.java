package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions.FunctionNative;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.MethodNative;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerializer;

/**
 * Function type
 * 
 * @author pcingola
 */
public class TypeFunc extends Type {

	FunctionDeclaration functionDeclaration;

	/**
	 * Get or create TypeFunc
	 */
	public static TypeFunc get(FunctionDeclaration function) {
		// Get type from hash
		String key = PrimitiveType.FUNC + ":" + function.signature();
		TypeFunc type = (TypeFunc) types.get(key);
		if (type == null) {
			type = new TypeFunc(function);
			types.put(key, type);
		}

		return type;
	}

	/**
	 * Get a class type
	 */
	public static TypeFunc get(String functionName) {
		// Get type from hash
		String key = PrimitiveType.FUNC + ":" + functionName;
		return (TypeFunc) types.get(key);
	}

	protected TypeFunc(FunctionDeclaration functionDeclaration) {
		super();
		primitiveType = PrimitiveType.FUNC;
		this.functionDeclaration = functionDeclaration;
	}

	public boolean canCast(TypeFunc type) {
		throw new RuntimeException("Unimplemented!");
	}

	@Override
	public int compareTo(Type type) {
		int cmp = primitiveType.ordinal() - type.primitiveType.ordinal();
		if (cmp != 0) return cmp;

		TypeFunc typef = (TypeFunc) type;
		// Both type names are null? => Equal
		if ((functionDeclaration == null) && (typef.functionDeclaration == null)) return 0;

		// Any null is 'first'
		if ((functionDeclaration != null) && (typef.functionDeclaration == null)) return 1;
		if ((functionDeclaration == null) && (typef.functionDeclaration != null)) return -1;

		// Compare names
		return functionDeclaration.signature().compareTo(typef.functionDeclaration.signature());
	}

	public FunctionDeclaration getFunctionDeclaration() {
		return functionDeclaration;
	}

	public String getFunctionName() {
		return functionDeclaration.getFunctionName();
	}

	public Parameters getParameters() {
		return functionDeclaration.getParameters();
	}

	public Type getReturnType() {
		return functionDeclaration.getReturnType();
	}

	@Override
	public boolean isFunction() {
		return true;
	}

	@Override
	public boolean isNative() {
		return (functionDeclaration instanceof FunctionNative) || (functionDeclaration instanceof MethodNative);
	}

	@Override
	public boolean isPrimitiveType() {
		return false;
	}

	@Override
	public void serializeParse(BigDataScriptSerializer serializer) {
		if (functionDeclaration instanceof MethodNative) {
			// Nothing to do: Native methods are not serialized
		} else super.serializeParse(serializer);
	}

	@Override
	public String serializeSave(BigDataScriptSerializer serializer) {
		// Nothing to do: Native methods are not serialized
		if (functionDeclaration instanceof MethodNative) {
			// Nothing to do: Native methods are not serialized
			return "";
		} else return super.serializeSave(serializer);
	}

	@Override
	public String toString() {
		return functionDeclaration.signature();
	}

}
