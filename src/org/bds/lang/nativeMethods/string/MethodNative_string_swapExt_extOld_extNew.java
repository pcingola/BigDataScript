package org.bds.lang.nativeMethods.string;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import org.bds.lang.Parameters;
import org.bds.lang.Type;
import org.bds.lang.TypeList;
import org.bds.lang.nativeMethods.MethodNative;
import org.bds.run.BdsThread;
import org.bds.task.Task;
import org.bds.util.Gpr;

public class MethodNative_string_swapExt_extOld_extNew extends MethodNative {
	public MethodNative_string_swapExt_extOld_extNew() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "swapExt";
		classType = Type.STRING;
		returnType = Type.STRING;

		String argNames[] = { "this", "extOld", "extNew" };
		Type argTypes[] = { Type.STRING, Type.STRING, Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		String extNew = csThread.getString("extNew"); String extOld = csThread.getString("extOld"); String b = objThis.toString(); if (b.endsWith(extOld)) return b.substring(0, b.length() - extOld.length()) + extNew; return b + extNew;
	}
}
