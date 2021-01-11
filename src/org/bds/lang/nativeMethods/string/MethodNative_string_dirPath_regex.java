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

public class MethodNative_string_dirPath_regex extends MethodNativeString {

	private static final long serialVersionUID = -2092737341202495762L;

	public MethodNative_string_dirPath_regex() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "dirPath";
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
	boolean matches(Data d, PathMatcher matcher) {
		File file = new File(d.getAbsolutePath());
		return matcher.matches(file.toPath());
	}

	@Override
	public Value runMethod(BdsThread bdsThread, Value vthis) {
		// Create pattern matcher from 'glob'
		String glob = bdsThread.getString("glob");
		final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + glob);

		String baseDir = vthis.asString();
		if (!baseDir.endsWith("/")) baseDir += "/";

		// List files
		ValueList vlist = new ValueList(returnType);
		for (Data sub : bdsThread.data(baseDir).list()) {
			if (matches(sub, matcher)) vlist.add(new ValueString(sub.getPathOrUrl()));
		}

		vlist.sort();
		return vlist;
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		throw new RuntimeException("This method should never be invoked!");
	}

}
