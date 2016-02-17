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

public class MethodNative_string_split_regex extends MethodNative {
	public MethodNative_string_split_regex() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "split";
		classType = Type.STRING;
		returnType = TypeList.get(Type.STRING);

		String argNames[] = { "this", "regex" };
		Type argTypes[] = { Type.STRING, Type.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	protected Object runMethodNative(BdsThread csThread, Object objThis) {
		String str = objThis.toString(); if( str.isEmpty() ) return new ArrayList<String>(); try { return array2list( str.split( csThread.getString("regex") ) ); } catch( Throwable t ) { ArrayList<String> l = new ArrayList<String>(); l.add(str); return l; }
	}
}
