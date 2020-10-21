package org.bds.lang.nativeMethods.string;

import java.util.ArrayList;

import org.bds.lang.nativeMethods.MethodNative;

/**
 * Loads all classes used for native library
 * @author pcingola
 *
 */
public class NativeLibraryString {

	public static String classNames[] = { "org.bds.lang.nativeMethods.string.MethodNative_string_length" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_toUpper" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_toLower" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_trim" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_substr_start_end" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_substr_start" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_split_regex" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_lines" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_isEmpty" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_startsWith_str" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_endsWith_str" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_indexOf_str" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_lastIndexOf_str" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_replace_str1_str2" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_parseInt" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_parseReal" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_parseBool" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_replace_regex_repl" //
			// String as files
			, "org.bds.lang.nativeMethods.string.MethodNative_string_baseName" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_baseName_ext" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_canExec" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_canRead" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_canWrite" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_chdir" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_delete" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_dir" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_dir_regex" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_dirName" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_dirPath" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_dirPath_regex" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_download" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_download_localname" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_exists" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_extName" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_isDir" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_isFile" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_mkdir" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_path" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_pathCanonical" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_pathName" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_read" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_readLines" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_removeExt" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_removeExt_ext" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_rm" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_rmOnExit" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_size" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_swapExt_extNew" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_swapExt_extOld_extNew" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_upload" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_upload_localname" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_write_str" //
			// String as task
			, "org.bds.lang.nativeMethods.string.MethodNative_string_exitCode" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_isDone" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_isDoneOk" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_stdout" //
			, "org.bds.lang.nativeMethods.string.MethodNative_string_stderr" //

	};

	ArrayList<MethodNative> methods;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public NativeLibraryString() {
		try {
			methods = new ArrayList<>();

			for (String className : classNames) {
				Class c = Class.forName(className);
				methods.add((MethodNative) c.getDeclaredConstructor().newInstance());
			}
		} catch (Exception e) {
			throw new RuntimeException("Error creating native library", e);
		}
	}

	public int size() {
		return methods.size();
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
