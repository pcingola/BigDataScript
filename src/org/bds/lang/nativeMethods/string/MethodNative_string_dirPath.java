package org.bds.lang.nativeMethods.string;

import java.util.ArrayList;
import java.util.Collections;

import org.bds.data.Data;
import org.bds.lang.Parameters;
import org.bds.lang.nativeMethods.MethodNative;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;

public class MethodNative_string_dirPath extends MethodNative {
	public MethodNative_string_dirPath() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "dirPath";
		classType = Types.STRING;
		returnType = TypeList.get(Types.STRING);

		String argNames[] = { "this" };
		Type argTypes[] = { Types.STRING };
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
