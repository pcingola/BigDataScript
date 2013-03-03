package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions;

import java.util.ArrayList;

/**
 * Loads all classes used for native functions library
 * 
 * @author pcingola
 *
 */
public class NativeLibraryFunctions {

	public static Class classes[] = { //
	FunctionNativePrint.class //
			, FunctionNativePrintErr.class //
	};

	ArrayList<FunctionNative> functions;

	@SuppressWarnings("rawtypes")
	public NativeLibraryFunctions() {
		try {
			functions = new ArrayList<FunctionNative>();

			for (Class c : classes)
				functions.add((FunctionNative) c.newInstance());

		} catch (Exception e) {
			throw new RuntimeException("Error creating native library", e);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName() + ":\n");
		for (FunctionNative m : functions)
			sb.append("\t" + m.getClass().getSimpleName() + "\n");
		return sb.toString();
	}

}
