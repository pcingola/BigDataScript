package org.bds.lang.nativeFunctions;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.lang.value.Value;
import org.bds.run.BdsThread;

/**
 * Get an expression's type and return it as a string
 *
 * @author pcingola
 */
public class FunctionNativeType extends FunctionNative {

	private static final long serialVersionUID = 6415943745404236449L;

	public FunctionNativeType() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "type";
		returnType = Types.STRING;

		String argNames[] = { "expr" };
		Type argTypes[] = { Types.ANY };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		Value expr = bdsThread.getValue("expr");
		if (expr == null) throw new RuntimeException("Could not evaluate expression.");
		if (expr.getType() == null) throw new RuntimeException("Could not evaluate expression's type: " + expr);
		return expr.getType().toString();
	}

}
