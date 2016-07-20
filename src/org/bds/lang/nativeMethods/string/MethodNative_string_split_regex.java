package org.bds.lang.nativeMethods.string;

import java.util.ArrayList;

import org.bds.lang.Parameters;
import org.bds.lang.Type;
import org.bds.lang.TypeList;
import org.bds.lang.nativeMethods.MethodNative;
import org.bds.run.BdsThread;

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
		String str = objThis.toString();
		if (str.isEmpty()) return new ArrayList<String>();
		try {
			String regex = csThread.getString("regex");
			return array2list(str.split(regex, -1));
		} catch (Throwable t) {
			ArrayList<String> l = new ArrayList<>();
			l.add(str);
			return l;
		}
	}
}
