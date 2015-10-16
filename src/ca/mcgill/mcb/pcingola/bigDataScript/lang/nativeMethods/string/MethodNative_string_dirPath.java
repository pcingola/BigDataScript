package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string;

import java.util.ArrayList;
import java.util.Collections;

import ca.mcgill.mcb.pcingola.bigDataScript.data.Data;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeList;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.MethodNative;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThread;

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
