package org.bds.lang.type;

import org.bds.lang.Parameters;
import org.bds.lang.statement.FunctionDeclaration;
import org.bds.lang.statement.VarDeclaration;
import org.bds.lang.statement.VariableInit;
import org.bds.util.Gpr;

/**
 * Function type
 *
 * @author pcingola
 */
public class TypeFunction extends Type {

	protected Parameters parameters;

	/**
	 * Get or create TypeFunction
	 */
	public static TypeFunction get(FunctionDeclaration functionDecl) {
		// Get type from hash
		String key = PrimitiveType.FUNCTION + ":" + functionDecl.signature();

		TypeFunction type = (TypeFunction) types.get(key);
		if (type == null) {
			type = new TypeFunction(functionDecl);
			types.put(key, type);
		}

		return (TypeFunction) types.get(key);
	}

	/**
	 * Get or create TypeFunction
	 */
	public static TypeFunction get(Parameters parameters, Type returnType) {
		// Get type from hash
		String key = PrimitiveType.FUNCTION + ":" + signature(parameters, returnType);

		TypeFunction type = (TypeFunction) types.get(key);
		if (type == null) {
			type = new TypeFunction(parameters, returnType);
			types.put(key, type);
		}

		return (TypeFunction) types.get(key);
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

	public TypeFunction(FunctionDeclaration functionDeclaration) {
		this(functionDeclaration.getParameters(), functionDeclaration.getReturnType());
	}

	protected TypeFunction(Parameters parameters, Type returnType) {
		super();
		primitiveType = PrimitiveType.FUNCTION;
		this.parameters = parameters;
		this.returnType = returnType;
	}

	public boolean canCast(TypeFunction type) {
		throw new RuntimeException("Unimplemented!");
	}

	@Override
	public int compareTo(Type type) {
		int cmp = primitiveType.ordinal() - type.primitiveType.ordinal();
		if (cmp != 0) return cmp;

		// Compare return types
		cmp = Gpr.compareNull(returnType, type.getReturnType());
		if (cmp != 0) return cmp;

		TypeFunction typef = (TypeFunction) type;

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
	public String toString() {
		return signature(parameters, returnType);
	}

}
