package org.bds.lang.nativeMethods.string;

import java.util.List;

import org.bds.data.Data;
import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueList;
import org.bds.lang.value.ValueString;
import org.bds.run.BdsThread;

public class MethodNative_string_dirPath extends MethodNativeString {

	private static final long serialVersionUID = 7696947923325583360L;

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
	public Value runMethod(BdsThread bdsThread, Value vthis) {
		ValueList vlist = new ValueList(returnType);

		String baseDir = vthis.asString();
		Data dBaseDir = bdsThread.data(baseDir);
		if (!baseDir.endsWith("/")) baseDir += "/";

		List<String> l = bdsThread.data(baseDir).list();
		for (String file : l) {
			Data d = dBaseDir.join(file);
			String path = d.isRemote() ? d.getUri().toString() : d.getAbsolutePath();
			//			Gpr.debug("FILE: " + file + "\td:" + path);
			vlist.add(new ValueString(path));
		}

		vlist.sort();
		return vlist;
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		throw new RuntimeException("This method should never be invoked!");
	}

}
