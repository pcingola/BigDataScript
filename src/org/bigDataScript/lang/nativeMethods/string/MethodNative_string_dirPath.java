package org.bigDataScript.lang.nativeMethods.string;

import java.util.ArrayList;
import java.util.Collections;

import org.bigDataScript.data.Data;
import org.bigDataScript.lang.Parameters;
import org.bigDataScript.lang.Type;
import org.bigDataScript.lang.TypeList;
import org.bigDataScript.lang.nativeMethods.MethodNative;
import org.bigDataScript.run.BdsThread;

public class MethodNative_string_dirPath extends MethodNative {
	public MethodNative_string_dirPath() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "dirPath";
		classType = Type.STRING;
		returnType = TypeList.get(Type.STRING);

		String argNames[] = { "this" };
		Type argTypes[] = { Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		ArrayList<String> list = new ArrayList<>();

		String baseDir = objThis.toString();
		if (!baseDir.endsWith("/")) baseDir += "/";

		for (String file : bdsThread.data(baseDir).list()) {
			file = baseDir + file;
			Data d = bdsThread.data(file);
			list.add(d.getAbsolutePath());
		}

		Collections.sort(list);
		return list;
	}
}
