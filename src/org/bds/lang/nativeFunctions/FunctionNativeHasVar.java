package org.bds.lang.nativeFunctions;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

/**
 * Does a variable exists?
 *
 * @author pcingola
 */
public class FunctionNativeHasVar extends FunctionNative {

	private static final long serialVersionUID = 6415943745404236449L;

	public FunctionNativeHasVar() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "hasVar";
		returnType = Types.STRING;

		String argNames[] = { "name" };
		Type argTypes[] = { Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		String name = bdsThread.getString("name");
		return bdsThread.hasVariable(name);
	}

}
