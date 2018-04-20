package org.bds.lang.nativeMethods.string;

import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueList;
import org.bds.run.BdsThread;

public class MethodNative_string_split_regex extends MethodNativeString {

	private static final long serialVersionUID = 3074534436096288291L;

	public MethodNative_string_split_regex() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "split";
		classType = Types.STRING;
		returnType = TypeList.get(Types.STRING);

		String argNames[] = { "this", "regex" };
		Type argTypes[] = { Types.STRING, Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	@Override
	public Value runMethod(BdsThread bdsThread, Value vThis) {
		String str = vThis.asString();
		ValueList vlist = new ValueList(returnType);

		if (str.isEmpty()) return vlist;

		try {
			String regex = bdsThread.getString("regex");
			return arrayString2valuelist(str.split(regex, -1));
		} catch (Throwable t) {
			// Error using regex? Return a list with only one element
			vlist.add(vThis);
			return vlist;
		}
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		throw new RuntimeException("This method should never be invoked!");
	}
}
