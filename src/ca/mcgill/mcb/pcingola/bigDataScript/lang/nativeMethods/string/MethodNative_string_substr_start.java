package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeList;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.MethodNative;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

public class MethodNative_string_substr_start extends MethodNative {
	public MethodNative_string_substr_start() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "substr";
		classType = Type.STRING;
		returnType = Type.STRING;

		String argNames[] = { "this", "start" };
		Type argTypes[] = { Type.STRING, Type.INT };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BigDataScriptThread csThread, Object objThis) {
		String s = objThis.toString(); int start = (int) csThread.getInt("start") ; start=Math.max(0,start); return s.substring(start);
	}
}
