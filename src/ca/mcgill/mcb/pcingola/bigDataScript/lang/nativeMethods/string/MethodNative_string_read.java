package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.MethodNative;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThread;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

public class MethodNative_string_read extends MethodNative {
	public MethodNative_string_read() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "read";
		classType = Type.STRING;
		returnType = Type.STRING;

		String argNames[] = { "this" };
		Type argTypes[] = { Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		String path = bdsThread.dataLocalPath(objThis.toString());
		return Gpr.readFile(path, false);
	}
}
