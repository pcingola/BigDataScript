package org.bds.lang.nativeFunctions;

import org.bds.lang.BdsNode;
import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;
import org.bds.util.Timer;

/**
 * Log a message to STDERR
 *
 * @author pcingola
 */
public class FunctionNativeLog extends FunctionNative {

	private static final long serialVersionUID = 4328132832275759104L;

	public FunctionNativeLog() {
		super();
	}

	@Override
	protected void initFunction() {
		functionName = "log";
		returnType = Types.STRING;

		String argNames[] = { "str" };
		Type argTypes[] = { Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeFunction();
	}

	@Override
	protected Object runFunctionNative(BdsThread bdsThread) {
		String str = bdsThread.getString("str");
		BdsNode bdsNode = bdsThread.getBdsNodeCurrent();
		Timer.showStdErr(str);
		return str;
	}

}
