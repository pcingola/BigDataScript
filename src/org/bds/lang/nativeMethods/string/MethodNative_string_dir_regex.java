package org.bds.lang.nativeMethods.string;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;

import org.bds.data.Data;
import org.bds.lang.Parameters;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueList;
import org.bds.lang.value.ValueString;
import org.bds.run.BdsThread;

public class MethodNative_string_dir_regex extends MethodNativeString {

	private static final long serialVersionUID = 6661798225404664743L;

	public MethodNative_string_dir_regex() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "dir";
		classType = Types.STRING;
		returnType = TypeList.get(Types.STRING);

		String argNames[] = { "this", "glob" };
		Type argTypes[] = { Types.STRING, Types.STRING };
		parameters = Parameters.get(argTypes, argNames);
		addNativeMethodToClassScope();
	}

	/**
	 * Does the path match?
	 */
	boolean matches(BdsThread bdsThread, Data path, PathMatcher matcher) {
		File file = new File(path.getName());
		return matcher.matches(file.toPath().getFileName());
	}

	@Override
	public Value runMethod(BdsThread bdsThread, Value vthis) {
		Data dir = bdsThread.data(vthis.asString());

		// Filter list by Glob
		ValueList vlist = new ValueList(returnType);
		String glob = bdsThread.getString("glob");
		final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + glob);
		for (Data d : dir.list()) {
			if (matches(bdsThread, d, matcher)) vlist.add(new ValueString(d.getName()));
		}

		vlist.sort();
		return vlist;
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		throw new RuntimeException("This method should never be invoked!");
	}

}
