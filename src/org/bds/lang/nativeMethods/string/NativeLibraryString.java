package org.bds.lang.nativeMethods.string;

import java.util.ArrayList;

import org.bds.lang.nativeMethods.MethodNative;

/**
 * Loads all classes used for native library
 * @author pcingola
 *
 */
public class NativeLibraryString {

	public static String classNames[] = { "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_length" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_toUpper" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_toLower" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_trim" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_substr_start_end" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_substr_start" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_split_regex" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_lines" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_isEmpty" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_startsWith_str" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_endsWith_str" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_indexOf_str" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_lastIndexOf_str" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_replace_str1_str2" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_parseInt" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_parseReal" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_parseBool" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_replace_regex_repl" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_path" //
			// String as files
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_baseName" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_baseName_ext" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_canExec" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_canRead" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_canWrite" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_chdir" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_delete" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_dir" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_dir_regex" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_dirName" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_dirPath" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_dirPath_regex" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_download" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_download_localname" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_exists" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_extName" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_isDir" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_isFile" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_mkdir" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_pathName" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_read" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_readLines" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_removeExt" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_removeExt_ext" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_rm" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_rmOnExit" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_size" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_swapExt_extNew" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_swapExt_extOld_extNew" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_upload" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_upload_localname" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_write_str" //
			// String as task
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_exitCode" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_isDone" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_isDoneOk" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_stdout" //
			, "org.bigDataScript.lang.nativeMethods.string.MethodNative_string_stderr" //

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
