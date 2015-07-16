package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * Function type
 *
 * @author pcingola
 */
public class TypeFunc extends Type {

	protected Parameters parameters;

	/**
	 * Get or create TypeFunc
	 */
	public static TypeFunc get(FunctionDeclaration functionDecl) {
		// Get type from hash
		String key = PrimitiveType.FUNC + ":" + functionDecl.signature();

		TypeFunc type = (TypeFunc) types.get(key);
		if (type == null) {
			type = new TypeFunc(functionDecl);
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
				Type type = vdecl.getType();
				for (VariableInit vi : vdecl.getVarInit())
					sb.append(type + ",");
			}
		}

		int lastChar = sb.length() - 1;
		if (sb.charAt(lastChar) == ',') sb.deleteCharAt(lastChar);
		sb.append(") -> ");
		sb.append(returnType);

		return sb.toString();
	}

	public TypeFunc(FunctionDeclaration functionDeclaration) {
		this(functionDeclaration.getParameters(), functionDeclaration.getReturnType());
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

		// Compare return types
		cmp = Gpr.compareNull(returnType, type.getReturnType());
		if (cmp != 0) return cmp;

		TypeFunc typef = (TypeFunc) type;

		// Compare names
		return Gpr.compareNull(parameters, typef.getParameters());
	}

	public Parameters getParameters() {
		return parameters;
	}

	@Override
	public Type getReturnType() {
		return returnType;
	}

	@Override
	public boolean isFunction() {
		return true;
	}

	@Override
	public boolean isPrimitiveType() {
		return false;
	}

	@Override
	public String toString() {
		return signature(parameters, returnType);
	}

}
