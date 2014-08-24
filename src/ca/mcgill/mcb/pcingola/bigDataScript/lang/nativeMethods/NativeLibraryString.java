
package ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods;

import java.util.ArrayList;

/**
 * Loads all classes used for native library
 * @author pcingola
 *
 */
public class NativeLibraryString {

	
	public static String classNames[] = {
			"ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_length" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_toUpper" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_toLower" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_trim" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_substr_start_end" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_substr_start" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_split_regex" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_lines" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_isEmpty" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_startsWith_str" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_endsWith_str" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_indexOf_str" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_lastIndexOf_str" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_replace_str1_str2" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_parseInt" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_parseReal" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_parseBool" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_replace_regex_repl" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_path" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_baseName" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_baseName_ext" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_dirName" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_pathName" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_extName" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_canRead" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_canWrite" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_canExec" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_exists" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_isDir" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_isFile" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_delete" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_rm" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_mkdir" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_read" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_removeExt" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_removeExt_ext" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_rmOnExit" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_swapExt_extNew" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_swapExt_extOld_extNew" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_write_str" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_readLines" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_dir" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_dir_regex" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_dirPath" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_dirPath_regex" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_size" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_isDone" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_isDoneOk" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_stdout" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_stderr" // 
	, "ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.MethodNative_string_exitCode" // 

	};

	ArrayList<MethodNative> methods;

	@SuppressWarnings("rawtypes")
	public NativeLibraryString() {
		try {
			methods = new ArrayList<MethodNative>();

			for (String className : classNames) {
				Class c = Class.forName(className);
				methods.add((MethodNative) c.newInstance());
			}
		} catch (Exception e) {
			throw new RuntimeException("Error creating native library", e);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName() + ":\n");
		for (MethodNative m : methods)
			sb.append("\t" + m.getClass().getSimpleName() + "\n");
		return sb.toString();
	}

}
