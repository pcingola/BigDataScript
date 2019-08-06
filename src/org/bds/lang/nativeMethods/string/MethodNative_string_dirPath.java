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
import org.bds.util.Gpr;

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
		if (!baseDir.endsWith("/")) baseDir += "/";

		Gpr.debug("OK 1");
		List<String> l = bdsThread.data(baseDir).list();
		Gpr.debug("l:" + l);
		for (String file : l) {
			file = baseDir + file;
			Data d = bdsThread.data(file);
			vlist.add(new ValueString(d.getAbsolutePath()));
		}

		vlist.sort();
		return vlist;
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		throw new RuntimeException("This method should never be invoked!");
	}

}
