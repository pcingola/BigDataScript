package org.bds.lang.type;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.expression.ReferenceVar;
import org.bds.lang.value.Literal;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueString;
import org.bds.symbol.SymbolTable;
import org.bds.util.Tuple;

public class InterpolateVars extends Literal {

	private static final long serialVersionUID = 5380913311800422951L;

	// boolean useLiteral;
	String literals[]; // This is used in case of interpolated string literal
	Expression exprs[]; // This is used in case of interpolated string literal; Usually these are VarReferences, but they might change to generic expressions in the future

	/**
	 * Escape string
	 */
	public static String escape(String s) {
		return s.replace("\\", "\\\\") //
				.replace("\b", "\\b") //
				.replace("\f", "\\f") //
				.replace("\n", "\\n") //
				.replace("\r", "\\r") //
				.replace("\t", "\\t") //
				.replace("\0", "\\0") //
		;
	}

	/**
	 * Escape string
	 */
	public static String unEscape(String s) {
		!!!!!!!!!!!!!!!!!
		return s.replace("\\", "\\\\") //
				.replace("\b", "\\b") //
				.replace("\f", "\\f") //
				.replace("\n", "\\n") //
				.replace("\r", "\\r") //
				.replace("\t", "\\t") //
				.replace("\0", "\\0") //
		;
	}

	//	/**
	//	 * Escape multiple lines
	//	 */
	//	static String escapeMultiline(String s) {
	//		boolean endsWithNewLine = (!s.isEmpty()) && (s.charAt(s.length() - 1) == '\n');
	//		String lines[] = s.split("\n");
	//		if (lines.length <= 1 && !endsWithNewLine) return s; // Nothing to escape
	//
	//		// Correct lines ending in backslash (e.g. multi-line unix commands)
	//		StringBuilder sb = new StringBuilder();
	//		for (int i = 0; i < lines.length; i++) {
	//			String l = lines[i];
	//			sb.append(l); // Add line
	//			if (l.endsWith("\\")) sb.append("\\"); // Make sure we have an (escaped) backslash
	//			if (i < (lines.length - 1)) sb.append("\\n"); // Add escaped newline
	//		}
	//
	//		// Does the string end with a newline? Then add escaped newline
	//		if (endsWithNewLine) sb.append("\\n"); // Add escaped newline
	//
	//		return sb.toString();
	//	}
	//
	//	static String escapeSingleQuote(String str) {
	//		return str.replace("'", "\\'");
	//	}
	//
	//	/**
	//	 * Un-escape string
	//	 */
	//	public static String unEscape(String str) {
	//		StringBuilder sb = new StringBuilder();
	//		char cprev = ' ';
	//		for (char c : str.toCharArray()) {
	//			if (cprev == '\\') {
	//				// Convert characters
	//				if (c == '\n') {
	//					// End of line, continues in the next one
	//				} else if (c == '\\') {
	//					// Escaped backslash
	//					sb.append(c);
	//					c = '\0'; // Avoid escaping next char
	//				} else {
	//					switch (c) {
	//					case 'b':
	//						c = '\b';
	//						break;
	//
	//					case 'f':
	//						c = '\f';
	//						break;
	//
	//					case 'n':
	//						c = '\n';
	//						break;
	//
	//					case 'r':
	//						c = '\r';
	//						break;
	//
	//					case 't':
	//						c = '\t';
	//						break;
	//
	//					case '0':
	//						c = '\0';
	//						break;
	//
	//					case '$':
	//						c = '$';
	//						break;
	//
	//					case '_':
	//						c = '_';
	//						break;
	//
	//					default:
	//						break;
	//					}
	//
	//					sb.append(c);
	//				}
	//			} else if (c != '\\') {
	//				sb.append(c);
	//			}
	//
	//			cprev = c;
	//		}
	//
	//		return sb.toString();
	//	}

	/**
	 * Almost literal string: Only un-escape dollar
	 */
	public static String unEscapeDollar(String str) {
		StringBuilder sb = new StringBuilder();

		char cprev = '\0';
		for (char c : str.toCharArray()) {
			if (cprev == '\\') {
				if (c == '$') {
					// Un-escape "\$" to "$"
					sb.append(c);
				} else {
					// Add every other escaped sequence (do not modify it)
					sb.append(cprev);
					sb.append(c);
				}
			} else if (c != '\\') {
				sb.append(c);
			}

			cprev = c;
		}

		if (cprev == '\\') sb.append(cprev); // Append last char

		return sb.toString();
	}

	public InterpolateVars(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Find the next 'string' (until an interpolation is found)
	 */
	Tuple<String, String> findString(String str) {
		// Empty? Nothing to do
		if (str.isEmpty()) return new Tuple<>(str, "");

		// Find next '$'
		int idx = str.indexOf('$');

		// Skip escaped dollar characters ('\$')
		while (idx > 0 // Found something?
				&& (str.charAt(idx - 1) == '\\' // Escaped character, ignore
						|| (idx < (str.length() - 1) //
								&& !isVariableNameStartChar(str.charAt(idx + 1)) // First character in variable name has to be a letter
						))) {
			idx = str.indexOf('$', idx + 1); // Find next one
		}

		// Nothing found?
		if (idx < 0) return new Tuple<>(str, "");

		// String that ends with a dollar sign is not a variable
		if (idx == str.length() - 1) return new Tuple<>(str, "");

		return new Tuple<>(str.substring(0, idx), str.substring(idx));
	}

	/**
	 * Find the next variable
	 */
	Tuple<String, String> findVariableRef(String str) {
		if (str.isEmpty()) return new Tuple<>("", "");

		int idx = indexRefEnd(str);

		// Nothing found?
		if (idx < 0) return new Tuple<>(str, "");

		return new Tuple<>(str.substring(1, idx), str.substring(idx)); // Note, subString starts at '1' to remove '$' character
	}

	/**
	 * Find variables and strings
	 */
	Tuple<List<String>, List<String>> findVariables(String str) {
		ArrayList<String> listStr = new ArrayList<>();
		ArrayList<String> listVars = new ArrayList<>();

		while (!str.isEmpty()) {
			//---
			// Parse string literal part
			//---
			Tuple<String, String> tupStr = findString(str);
			String strToAdd = escape(unEscapeDollar(tupStr.first));
			listStr.add(strToAdd); // Store string
			str = tupStr.second; // Remaining to be analyzed

			//---
			// Parse variable reference part
			//---
			Tuple<String, String> tupVar = findVariableRef(str);
			listVars.add(tupVar.first); // Store variable reference
			str = tupVar.second; // Remaining to be analyzed
		}

		return new Tuple<>(listStr, listVars);
	}

	public Expression[] getExpressions() {
		return exprs;
	}

	public String[] getLiterals() {
		return literals;
	}

	int indexRefEnd(String str) {
		// Only a dollar sign? It's probably a string finishing with '$', not a variable
		if (str.equals("$")) return -1;

		char cprev = ' ';
		char chars[] = str.toCharArray();

		int countBraces = 0, countCurly = 0;
		boolean quote = false;

		for (int i = 0; i < str.length(); i++) {
			if (cprev == '\\') continue; // Skip escaped characters

			char c = chars[i];

			// Inside quotes? Keep looking until we find another quote
			if (quote) {
				if (c == '\'') quote = false;
				continue;
			}

			switch (c) {
			case ']':
				countBraces--;
				if (countBraces < 0) return i;
				break;

			case '[':
				countBraces++;
				break;

			case '{':
				countCurly++;
				break;

			case '\'':
				if (countBraces == 0 && countCurly == 0) return i; // End of variable
				quote = true;
				break;

			case '}':
				countCurly--;
				if (countCurly < 0) return i;
				break;

			case '$':
				if (i > 0 && countBraces == 0 && countCurly == 0) return i; // New variable
				break;

			default:
				if (!isVariableNameChar(c) && countBraces == 0 && countCurly == 0) return i;
			}
		}

		return str.length();
	}

	/**
	 * How to show objects in interpolation
	 */
	String interpolateValue(Value val) {
		return val != null ? val.toString() : "null";
	}

	public boolean isEmpty() {
		return exprs == null || exprs.length <= 0;
	}

	/**
	 * Is this a valid character for a variable name?
	 */
	boolean isVariableNameChar(char c) {
		return Character.isLetterOrDigit(c) || (c == '_');
	}

	/**
	 * Is this a valid character for starting a variable name?
	 * (e.g. the first letter in a variable name cannot be a digit)
	 */
	boolean isVariableNameStartChar(char c) {
		return Character.isLetter(c);
	}

	/**
	 * Parse variable interpolation
	 */
	public boolean parse(String str) {
		Tuple<List<String>, List<String>> interpolated = findVariables(str);

		// Something was found, we have to interpolate
		List<String> strings = interpolated.first;
		List<String> variables = interpolated.second;

		if (interpolated.second.isEmpty() // No variables?
				|| (interpolated.second.size() == 1 && interpolated.second.get(0).isEmpty()) // One empty variable?
		) return false; // Nothing to do

		List<Expression> exprs = new ArrayList<>();

		// Create and add reference
		for (String var : variables) {
			Expression varRef = ReferenceVar.factory(parent, var);
			exprs.add(varRef);
		}

		// Convert to array
		literals = strings.toArray(new String[0]);
		this.exprs = exprs.toArray(new Expression[0]);
		return !isEmpty();
	}

	@Override
	protected ValueString parseValue(ParseTree tree) {
		return new ValueString(tree.getChild(0).getText());
	}

	@Override
	public Type returnType(SymbolTable symtab) {
		if (returnType != null) return returnType;

		returnType = Types.STRING;
		return returnType;
	}

	@Override
	public String toAsm() {
		StringBuilder sb = new StringBuilder();

		// Variable interpolation
		int count = 0;
		for (int i = 0; i < literals.length; i++) {
			// String before variable
			sb.append("pushs '" + literals[i] + "'\n");
			count++;

			// Reference to value
			Expression ref = exprs[i];
			if (ref != null) {
				sb.append(ref.toAsm());
				count++;
			}
		}

		// Join all strings
		for (int i = 0; i < count - 1; i++)
			sb.append("adds\n");

		return sb.toString();
	}

	@Override
	public String toString() {
		if (isEmpty()) return "null";

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < literals.length; i++) {
			if (sb.length() > 0) sb.append(" + ");
			sb.append("\"" + literals[i] + "\"");
			if (exprs[i] != null) sb.append(" + " + exprs[i]);
		}

		return sb.toString();
	}

	@Override
	public void typeCheckNotNull(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Do we have any interpolated variables? Make sure they are in the scope
		if (exprs != null) //
			for (Expression expr : exprs)
			if (expr != null) expr.typeCheck(symtab, compilerMessages);
	}

}
