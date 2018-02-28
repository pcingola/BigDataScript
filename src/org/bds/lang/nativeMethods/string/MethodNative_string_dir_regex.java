package org.bds.lang.nativeMethods.string;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

import org.bds.lang.Parameters;
import org.bds.lang.nativeMethods.MethodNative;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.run.BdsThread;

public class MethodNative_string_dir_regex extends MethodNative {
	public MethodNative_string_dir_regex() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "dir";
		classType = Type.STRING;
		returnType = TypeList.get(Type.STRING);

		String argNames[] = { "this", "glob" };
		Type argTypes[] = { Type.STRING, Type.STRING };
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
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		String glob = bdsThread.getString("glob");

		//---
		// List all files, filtered by 'glob'
		//---
		final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + glob);

		String baseDir = objThis.toString();
		ArrayList<String> list = bdsThread.data(baseDir) // Create data object
				.list() // List files in dir
				.stream() // Convert to stream
				.filter(d -> matches(d, matcher)) // Filter using path matcher
				.collect(Collectors.toCollection(ArrayList::new)) // Convert stream to arrayList
				;

		// Sort by name
		Collections.sort(list);

		return list;
	}
}
