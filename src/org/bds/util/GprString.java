package org.bds.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

public class GprString {

	public static final String[] EMPTY_STRING_ARRAY = new String[0];

	public static String escape(String str) {
		return StringEscapeUtils.escapeJava(str);
	}

	/**
	 * Escape multi-line strings so they can be printed in one line
	 */
	public static String escapeMultiline(String str) {
		// Escape sequences:
		//     '\n' => '\' + 'n'
		//     '\r' => '\' + 'r'
		// If line ends in backslash, then it is joined to the previous line
		//     '\' + '\n' => ''
		//     '\' + '\r\n' => ''
		return str.replace("\\\r\n", "") //
				.replace("\\\n", "") //
				.replace("\n", "\\n") //
				.replace("\r", "\\r") //
		;
	}

	public static int indexOfUnescaped(String str, char symbol) {
		// Empty? Nothing to do
		if (str == null || str.isEmpty()) return -1;

		// Find next 'symbol
		int idx = str.indexOf(symbol);

		// Skip escaped dollar characters ('\$')
		while (idx > 0 && (str.charAt(idx - 1) == '\\')) { // Escaped character, ignore
			idx = str.indexOf('$', idx + 1); // Find next one
		}

		return idx;
	}

	/**
	 * Find next occurrence of 'symbol' (un-escaped) outside quotes (return -1 if not found)
	 */
	public static int indexOutsideQuotes(String str, char symbol) {
		// Empty? Nothing to do
		if (str == null || str.isEmpty()) return -1;

		char cprev = ' ';
		char chars[] = str.toCharArray();
		boolean quote = false;
		for (int i = 0; i < chars.length; cprev = chars[i], i++) {
			char c = chars[i];

			if (quote) {
				if (c == '"' && cprev != '\\') quote = false; // Quote finished
				continue;
			}

			if (chars[i] == '"') quote = true;
			else if (chars[i] == symbol && cprev != '\\') return i;
		}

		return -1;
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

	public static String[] splitCsv(String str) {
		List<String> list = new ArrayList<>();

		while (!str.isEmpty()) {
			int idx = indexOutsideQuotes(str, ',');

			String value = null;
			if (idx < 0) {
				value = str;
				str = "";
			} else {
				value = str.substring(0, idx);
				str = str.substring(idx + 1);
			}

			// Try to remove quotes (if any)
			String val = value.trim();
			if (val.length() > 1 && val.startsWith("\"") && val.endsWith("\"")) value = val.substring(1, val.length() - 1);

			list.add(value);
		}

		return list.toArray(EMPTY_STRING_ARRAY);
	}

	/**
	 * Unescape chars
	 */
	public static String unescape(String str) {
		return StringEscapeUtils.unescapeJava(str);
	}

	/**
	 * Unescape dollar sign
	 */
	public static String unescapeDollar(String str) {
		return str.replace("\\$", "$");
	}

}
