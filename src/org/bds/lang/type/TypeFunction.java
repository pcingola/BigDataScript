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
public class TypeFunction extends TypeComposite {

	private static final long serialVersionUID = -6636870217965016947L;

	// Note: returnType already exists in BdsNode
	Parameters parameters;

	public TypeFunction(FunctionDeclaration functionDeclaration) {
		super(PrimitiveType.FUNCTION);
		parameters = functionDeclaration.getParameters();
		returnType = functionDeclaration.getReturnType();
	}

	public boolean canCast(Type type) {
		return equals(type) // Same type
				|| type.isAny() // Cast to 'any'
		;
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
		return Gpr.compareNull(getParameters(), typef.getParameters());
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
	public Value newDefaultValue() {
		throw new RuntimeException("Cannot instanciate default value for function type " + this);
	}

	/**
	 * Generic signature for a function
	 */
	@SuppressWarnings("unused")
	public String signature() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		Parameters parameters = getParameters();
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

	@Override
	public String toString() {
		return signature();
	}

}
