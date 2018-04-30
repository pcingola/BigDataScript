package org.bds.lang.nativeMethods.string;

import java.util.ArrayList;
import java.util.Collections;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueList;
import org.bds.lang.value.ValueString;
import org.bds.run.BdsThread;

public class MethodNative_string_dir extends MethodNativeString {

	private static final long serialVersionUID = -8013491174060654835L;

	public MethodNative_string_dir() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "dir";
		classType = Types.STRING;
		returnType = TypeList.get(Types.STRING);

		String argNames[] = { "this" };
		Type argTypes[] = { Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	public Value runMethod(BdsThread bdsThread, Value vthis) {
		ArrayList<String> list = bdsThread.data(vthis.asString()).list();
		Collections.sort(list);

		ValueList vlist = new ValueList(returnType);
		for (String s : list)
			vlist.add(new ValueString(s));
		return vlist;
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		throw new RuntimeException("This method should never be invoked!");
	}

}
