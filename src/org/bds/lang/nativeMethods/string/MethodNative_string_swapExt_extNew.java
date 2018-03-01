package org.bds.lang.nativeMethods.string;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import org.bds.lang.Parameters;
import org.bds.lang.nativeMethods.MethodNative;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;
import org.bds.task.Task;
import org.bds.util.Gpr;

public class MethodNative_string_swapExt_extNew extends MethodNativeString {
	public MethodNative_string_swapExt_extNew() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "swapExt";
		classType = Types.STRING;
		returnType = Types.STRING;

		String argNames[] = { "this", "extNew" };
		Type argTypes[] = { Types.STRING, Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		String base = objThis.toString(); String extNew = csThread.getString("extNew"); int idx = base.lastIndexOf('.'); return idx >= 0 ? base.substring(0, idx) + "." + extNew : base + "." + extNew;
	}
}
