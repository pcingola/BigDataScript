package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Parameters;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeList;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.MethodNative;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThread;

public class MethodNative_string_dirPath_regex extends MethodNative {

	public MethodNative_string_dirPath_regex() {
		super();
	}

	@Override
	protected void initMethod() {
		functionName = "dirPath";
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
		File canFile;
		try {
			canFile = file.getCanonicalFile();
			return matcher.matches(canFile.toPath());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected Object runMethodNative(BdsThread bdsThread, Object objThis) {
		String glob = bdsThread.getString("glob");

		//---
		// List all files, filtered by 'glob'
		//---
		final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + glob);

		ArrayList<String> list = bdsThread.data(objThis.toString()) // Create data object
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
