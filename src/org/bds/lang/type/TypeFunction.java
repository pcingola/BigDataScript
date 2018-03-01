package org.bds.lang.type;

import org.bds.lang.Parameters;
import org.bds.lang.statement.FunctionDeclaration;
import org.bds.lang.statement.VarDeclaration;
import org.bds.lang.statement.VariableInit;
import org.bds.lang.value.Value;
import org.bds.util.Gpr;

/**
 * Function type
 *
 * @author pcingola
 */
public class TypeFunction extends Type {

	protected Parameters parameters; // Function parameters
	// Note: returnType already exists in BdsNode

	/**
	 * Get or create TypeFunction
	 */
	public static TypeFunction get(FunctionDeclaration functionDecl) {
		// Get type from hash
		String key = signature(functionDecl.getParameters(), functionDecl.getReturnType());
		TypeFunction type = (TypeFunction) Types.get(key);
		if (type == null) {
			type = new TypeFunction(functionDecl);
			Types.put(type);
		}

		return type;
	}

	/**
	 * Get or create TypeFunction
	 */
	public static TypeFunction get(Parameters parameters, Type returnType) {
		// Get type from hash
		String key = signature(parameters, returnType);
		TypeFunction type = (TypeFunction) Types.get(key);
		if (type == null) {
			type = new TypeFunction(parameters, returnType);
			Types.put(type);
		}

		return type;
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
		super(PrimitiveType.FUNCTION);
		this.parameters = parameters;
		this.returnType = returnType;
	}

	public boolean canCast(TypeFunction type) {
		throw new RuntimeException("Unimplemented!");
	}

	@Override
	public int compareTo(Type type) {
		int cmp = super.compareTo(type);
		if (cmp != 0) return cmp;

		// Compare return types
		TypeFunction typef = (TypeFunction) type;
		cmp = Gpr.compareNull(returnType, typef.getReturnType());
		if (cmp != 0) return cmp;

		// Compare parameters
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
	public Value newValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return signature(parameters, returnType);
	}

}
