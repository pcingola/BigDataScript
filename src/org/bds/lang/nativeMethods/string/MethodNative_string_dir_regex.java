package org.bds.lang.nativeMethods.string;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

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
	boolean matches(String path, PathMatcher matcher) {
		File file = new File(path);
		return matcher.matches(file.toPath());
	}

	@Override
	public Value runMethod(BdsThread bdsThread, Value vthis) {
		String glob = bdsThread.getString("glob");

		//---
		// List all files, filtered by 'glob'
		//---
		final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + glob);

		String baseDir = vthis.asString();
		ArrayList<String> list = bdsThread.data(baseDir) // Create data object
				.list() // List files in dir
				.stream() // Convert to stream
				.filter(d -> matches(d, matcher)) // Filter using path matcher
				.collect(Collectors.toCollection(ArrayList::new)) // Convert stream to arrayList
		;

		// Sort by name
		Collections.sort(list);

		// Convert to ValueList
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
