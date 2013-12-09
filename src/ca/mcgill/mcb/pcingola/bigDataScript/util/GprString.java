package ca.mcgill.mcb.pcingola.bigDataScript.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

public class GprString {

	public static String escape(String str) {
		return StringEscapeUtils.escapeJava(str);
	}

	/**
	 * Split a string (to be interpolated) into a list of strings and a list ov variable names
	 * 
	 * @param str
	 * @return A tuple containing a list of strings and a list of variables
	 */
	public static Tuple<List<String>, List<String>> findVariables(String str) {
		ArrayList<String> listStr = new ArrayList<String>();
		ArrayList<String> listVars = new ArrayList<String>();

		StringBuilder sbStr = new StringBuilder();
		StringBuilder sbVar = new StringBuilder();
		boolean isVar = false;
		char cprev = ' ';
		for (char c : str.toCharArray()) {

			// End of variable name
			// TODO: Add lists and maps
			if (isVar && (!(Character.isLetterOrDigit(c) || (c == '_')))) {
				// End of variable name
				isVar = false;
				String varName = sbVar.toString().substring(1); // Add variable name (without leading '$')

				if (varName.isEmpty()) listVars.add("$$"); // Just a dollar sign
				else listVars.add(varName);

				listStr.add(sbStr.toString());

				sbStr = new StringBuilder();
				sbVar = new StringBuilder();
			}

			// New variable name?
			// Note that we can have "some string $var1$var2 ..."
			if ((c == '$') && (cprev != '\\')) isVar = true;

			(isVar ? sbVar : sbStr).append(c);

			cprev = c;
		}

		// Add last one
		if ((sbVar.length() > 0) || (sbStr.length() > 0)) {
			listStr.add(sbStr.toString());

			String varName = "";
			if (sbVar.length() > 0) varName = sbVar.toString().substring(1);

			listVars.add(varName);
		}

		return new Tuple<List<String>, List<String>>(listStr, listVars);
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
	 * @param str
	 * @return
	 */
	public static String[] splitCommandMultiline(String str) {
		return str.split(";\\\\\n");
	}

	/**
	 * Unescape chars
	 * @param str
	 * @return
	 */
	public static String unescape(String str) {
		return StringEscapeUtils.unescapeJava(str);
	}

}
