package org.bds.report;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bds.util.AutoHashMap;
import org.bds.util.Gpr;

/**
 * R-Template: The Retarded Template Engine
 *
 * Templates are annotated using:
 *
 * 		- Double curly brackets {{variableName}}
 *
 * 		- Multiline parts have a triple curly brackets:
 * 					{{{ ... text
 * 						... more text including {{variables}}
 * 					... final line }}}
 *
 * @author pcingola
 */
public class RTemplate {

	public static boolean debug = false;

	public static final String MULTILINE_BLOCK_START = "{{{";
	public static final String MULTILINE_BLOCK_END = "}}}";

	public static final String PATTERN_STRING = "\\{\\{(\\S+)\\}\\}";
	public static final Pattern PATTERN = Pattern.compile(PATTERN_STRING);

	String outFile;
	@SuppressWarnings("rawtypes")
	Class baseClass;
	String resourceName;
	AutoHashMap<String, List<String>> keyValues;

	@SuppressWarnings("rawtypes")
	public RTemplate(Class baseClass, String resourceName, String outFile) {
		this.outFile = outFile;
		this.baseClass = baseClass;
		this.resourceName = resourceName;
		keyValues = new AutoHashMap<String, List<String>>(new ArrayList<String>());
	}

	public void add(String key, Object value) {
		if (value == null) keyValues.getOrCreate(key).add("");
		else keyValues.getOrCreate(key).add(value.toString());
	}

	/**
	 * Create output file
	 */
	public void createOuptut() {
		// Open input and read the whole file
		InputStream inStream = baseClass.getResourceAsStream(resourceName);
		if (inStream == null) throw new RuntimeException("Cannot open resource '" + resourceName + "' (class '" + baseClass.getCanonicalName() + "')");
		String input = Gpr.read(inStream);
		if (debug) Gpr.debug("Input:\n" + input);

		// Split by multi-line delimiter '{{{'
		List<String> linesMulti = splitMultiLine(input);

		// Parse input, line by line
		StringBuilder out = new StringBuilder();
		boolean isMultiLine = false;
		for (String lines : linesMulti) {

			if (isMultiLine) {
				// Parse while block as multi-line
				out.append(parseLine(lines));
			} else {
				// Parse each line independently
				for (String line : lines.split("\n"))
					out.append(parseLine(line));
			}

			isMultiLine = !isMultiLine;
		}

		// Create output file
		Gpr.toFile(outFile, out.toString());
	}

	/**
	 * Show error
	 * @param errStr
	 */
	void error(String errStr) {
		String msg = "Error parsing: " + errStr //
				+ "\n\tBase class    : " + baseClass.getCanonicalName() //
				+ "\n\tResource name : " + resourceName//
		;

		throw new RuntimeException(msg);
	}

	/**
	 * Parse each input line
	 */
	String parseLine(String line) {
		// Debug command from template?
		if (line.startsWith("DEBUG")) {
			debug = true;
		}

		Matcher m = PATTERN.matcher(line);

		ArrayList<String> lineParts = new ArrayList<String>(); // Collect the parts of the line that do not match regex here
		ArrayList<String> keys = new ArrayList<String>(); // Collect keys here

		// Find all matching names
		boolean found = false;
		int prevIdx = 0;
		while (m.find()) {
			found = true;

			if (debug) Gpr.debug("Line: |" + line);
			int countGroups = m.groupCount();

			// Groups number 0 is the whole text, so we skip it
			for (int i = 1; i <= countGroups; i++) {
				String linePart = line.substring(prevIdx, m.start());
				lineParts.add(linePart);
				keys.add(m.group(i));
				if (debug) Gpr.debug("\t\tpart: '" + linePart + "'\tkey: '" + m.group(i) + "'\t");
				prevIdx = m.end();
			}
		}

		// Add last bit
		if (found) {
			String linePart = line.substring(prevIdx, line.length());
			lineParts.add(linePart);
			if (debug) Gpr.debug("\t\tpart: '" + linePart + "'");
		}

		if (!found) return line + "\n";

		// Replace values
		return replaceValues(lineParts, keys);
	}

	/**
	 * Replace by values
	 */
	String replaceValues(ArrayList<String> lineParts, ArrayList<String> keys) {
		// How many times do we repeat the line?
		int maxLen = Integer.MAX_VALUE;

		for (String key : keys) {
			if (!keyValues.containsKey(key)) error("Key '" + key + "' not found!");
			List<String> values = keyValues.get(key);
			int len = values.size();
			maxLen = Math.min(maxLen, len);
			if (debug) Gpr.debug("\t\tKey '': " + key + "\tNum. values: " + maxLen + "\tMax len: " + maxLen);
		}
		if (debug) Gpr.debug("\t\tMax len: " + maxLen);

		// Repeat
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < maxLen; i++)
			sb.append(replaceValues(lineParts, keys, i) + "\n");

		return sb.toString();
	}

	/**
	 * Replace by values (list item number 'idx')
	 */
	String replaceValues(ArrayList<String> lineParts, ArrayList<String> keys, int idx) {
		StringBuilder sb = new StringBuilder();

		// Add all parts and values
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = keyValues.get(key).get(idx);

			sb.append(lineParts.get(i));
			sb.append(value);
		}

		// Add last part
		sb.append(lineParts.get(lineParts.size() - 1));

		if (debug) Gpr.debug("ReplaceValues, idx " + idx + " : '" + sb + "'");
		return sb.toString();

	}

	/**
	 * Split into multiple lines finding the corresponding delimiters '{{{' and '}}}'
	 */
	List<String> splitMultiLine(String input) {
		List<String> blocks = new ArrayList<String>();

		while (!input.isEmpty()) {
			int idxStart = input.indexOf(MULTILINE_BLOCK_START);

			if (idxStart < 0) {
				// No multi-line block delimiter found? 
				// => All the remaining text is one block
				blocks.add(input);
				input = "";
			} else {
				// Add next 'regular' block
				String block = input.substring(0, idxStart);
				blocks.add(block);
				input = input.substring(idxStart + MULTILINE_BLOCK_START.length());
				if (debug) Gpr.debug("Adding regular block:" //
						+ "\n--------------------------------------------------------------------------------\n" //
						+ block //
						+ "\n--------------------------------------------------------------------------------" //
				);

				// Find block end
				int idxEnd = input.indexOf(MULTILINE_BLOCK_END);
				if (idxEnd < 0) error("Multiline block does not have an block end delimiter:\n" + input);

				// Add next 'multiline' block
				block = input.substring(0, idxEnd);
				blocks.add(block);
				if (debug) Gpr.debug("Adding multiline block:" //
						+ "\n--------------------------------------------------------------------------------\n" //
						+ block //
						+ "\n--------------------------------------------------------------------------------" //
				);
				input = input.substring(idxEnd + MULTILINE_BLOCK_END.length());
			}
		}

		blocks.add(input);
		return blocks;
	}

}