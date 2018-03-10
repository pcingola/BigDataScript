package org.bds.lang.type;

import org.bds.lang.Parameters;
import org.bds.lang.statement.FunctionDeclaration;
import org.bds.lang.statement.MethodDeclaration;
import org.bds.lang.statement.VarDeclaration;
import org.bds.lang.statement.VariableInit;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueFunction;
import org.bds.util.Gpr;

/**
 * Function type
 *
 * @author pcingola
 */
public class TypeFunction extends TypeComposite {

	// Note: returnType already exists in BdsNode
	FunctionDeclaration functionDecl;

	public TypeFunction(FunctionDeclaration functionDeclaration) {
		super(PrimitiveType.FUNCTION);
		returnType = functionDeclaration.getReturnType();
	}

	public boolean canCast(TypeFunction type) {
		throw new RuntimeException("Unimplemented!");
	}

	@Override
	public Object castNativeObject(Object o) {
		if (o instanceof MethodDeclaration || o instanceof FunctionDeclaration) return o;
		throw new RuntimeException("Cannot cast native object '" + o.getClass().getCanonicalName() + "' to type '" + this + "'");
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

	public FunctionDeclaration getFunctionDeclaration() {
		return functionDecl;
	}

	public Parameters getParameters() {
		return functionDecl.getParameters();
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
		return new ValueFunction(this);
	}

	/**
	 * Generic signature for a function
	 */
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
