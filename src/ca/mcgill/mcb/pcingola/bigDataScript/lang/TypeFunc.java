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
	protected Type returnType;
	protected Parameters parameters;

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

		return (TypeFunc) types.get(key);
	}

	/**
	 * Get or create TypeFunc
	 */
	public static TypeFunc get(Parameters parameters, Type returnType) {
		// Get type from hash
		String key = PrimitiveType.FUNC + ":" + signature(parameters, returnType);

		TypeFunc type = (TypeFunc) types.get(key);
		if (type == null) {
			type = new TypeFunc(parameters, returnType);
			types.put(key, type);
		}

		return (TypeFunc) types.get(key);
	}

	/**
	 * Generic signature for a function
	 */
	public static String signature(Parameters parameters, Type returnType) {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		if (parameters != null) {
			for (VarDeclaration vdecl : parameters.getVarDecl()) {
				Type type = vdecl.type;
				for (VariableInit vi : vdecl.getVarInit())
					sb.append(type + ",");

				sb.deleteCharAt(sb.length() - 1);
			}
		}
		sb.append(") -> ");
		sb.append(returnType);

		return sb.toString();
	}

	protected TypeFunc(FunctionDeclaration functionDeclaration) {
		super();
		primitiveType = PrimitiveType.FUNC;
		this.functionDeclaration = functionDeclaration;
		parameters = functionDeclaration.getParameters();
		returnType = functionDeclaration.getReturnType();
	}

	protected TypeFunc(Parameters parameters, Type returnType) {
		super();
		primitiveType = PrimitiveType.FUNC;
		this.parameters = parameters;
		this.returnType = returnType;
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

	public Parameters getParameters() {
		return parameters;
	}

	public Type getReturnType() {
		return returnType;
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
		return signature(parameters, returnType);
	}

}
