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

public class MethodNative_string_dir_regex extends MethodNative {
	public MethodNative_string_dir_regex() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "dir";
		classType = Type.STRING;
		returnType = TypeList.get(Type.STRING);

		String argNames[] = { "this", "regex" };
		Type argTypes[] = { Type.STRING, Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BigDataScriptThread csThread, Object objThis) {
		String regex = csThread.getString("regex"); ArrayList<String> list = new ArrayList<String>(); String files[] = new File(objThis.toString()).list(); if (files == null) return list; for (String s : files) if (s.matches(regex)) list.add(s); Collections.sort(list); return list;
	}
}
