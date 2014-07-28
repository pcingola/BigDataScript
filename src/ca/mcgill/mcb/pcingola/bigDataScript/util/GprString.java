package ca.mcgill.mcb.pcingola.bigDataScript.util;

import org.apache.commons.lang3.StringEscapeUtils;

public class GprString {

	public static String escape(String str) {
		return StringEscapeUtils.escapeJava(str);
	}

	/**
	 * Split a muti-line command string
	 *
	 * E.g.
	 *
	 *    sys ls -al ;\
	 *    		echo hi there;\
	 *    		du -sm
	 *
	 */
	public static String[] splitCommandMultiline(String str) {
		return str.split(";\\\\\n");
	}

	/**
	 * Unescape chars
	 */
	public static String unescape(String str) {
		return StringEscapeUtils.unescapeJava(str);
	}

}
