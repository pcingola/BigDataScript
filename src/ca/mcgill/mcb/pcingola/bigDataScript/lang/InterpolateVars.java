package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Tuple;

public class InterpolateVars extends Literal {

	String literals[]; // This is used in case of interpolated string literal
	Expression exprs[]; // This is used in case of interpolated string literal; Usually these are VarReferences, but they might change to generic expressions in the future

	/**
	 * Create a reference form a string
	 */
	public static Expression factory(BigDataScriptNode parent, String var) {
		if (var == null || var.isEmpty()) return null;

		int idxCurly = var.indexOf('{');
		int idxBrace = var.indexOf('[');

		Reference varRef = null;
		if (idxCurly < 0 && idxBrace < 0) varRef = new VarReference(parent, null);
		else if (idxCurly < 0 && idxBrace > 0) varRef = new VarReferenceList(parent, null);
		else if (idxCurly > 0 && idxBrace < 0) varRef = new VarReferenceMap(parent, null);
		else if (idxBrace < idxCurly) varRef = new VarReferenceList(parent, null);
		else varRef = new VarReferenceMap(parent, null);

		// Parse string
		varRef.parse(var);
		return varRef;
	}

	/**
	 * Un-escape string
	 */
	public static String unEscape(String str) {
		StringBuilder sb = new StringBuilder();
		char cprev = ' ';
		for (char c : str.toCharArray()) {

			if (cprev == '\\') {
				// Convert characters
				if (c == '\n') {
					// End of line, continues in the next one
				} else {
					switch (c) {
					case 'b':
						c = '\b';
						break;

					case 'f':
						c = '\f';
						break;

					case 'n':
						c = '\n';
						break;

					case 'r':
						c = '\r';
						break;

					case 't':
						c = '\t';
						break;

					case '0':
						c = '\0';
						break;

					case '$':
						c = '$';
						break;

					default:
						break;
					}

					sb.append(c);
				}
			} else if (c != '\\') {
				sb.append(c);
			}

			cprev = c;
		}

		return sb.toString();
	}

	public InterpolateVars(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public void runStep(BigDataScriptThread bdsThread) {
		StringBuilder sb = new StringBuilder();

		// Variable interpolation
		for (int i = 0; i < literals.length; i++) {
			// String before variable
			sb.append(literals[i]);

			// Variable's value
			Expression ref = exprs[i];
			if (ref != null) {
				bdsThread.run(ref);
				if (!bdsThread.isCheckpointRecover()) {
					Object val = bdsThread.pop();
					sb.append(interpolateValue(val));
				}
			}
		}

		bdsThread.push(sb.toString());
	}

	/**
	 * Find the next 'string' (until an interpolation is found
	 */
	Tuple<String, String> findString(String str) {
		// Empty? Nothing to do
		if (str.isEmpty()) return new Tuple<String, String>(str, "");

		// Find next '$'
		int idx = str.indexOf('$');

		// Skip escaped dollar characters ('\$')
		while (idx > 0 && // Found something?
				(str.charAt(idx - 1) == '\\' // Escaped character, ignore
				|| (idx < (str.length() - 1) && !Character.isLetter(str.charAt(idx + 1))) // First character in variable name has to be a letter
				) //
		) {
			idx = str.indexOf('$', idx + 1); // Find next one
		}

		// Nothing found?
		if (idx < 0) return new Tuple<String, String>(str, "");

		// String that ends with a dollar sign is not a variable
		if (idx == str.length() - 1) return new Tuple<String, String>(str, "");

		return new Tuple<String, String>(str.substring(0, idx), str.substring(idx));
	}

	/**
	 * Find the next variable
	 */
	Tuple<String, String> findVariableRef(String str) {
		if (str.isEmpty()) return new Tuple<String, String>("", "");

		int idx = indexRefEnd(str);

		// Nothing found?
		if (idx < 0) return new Tuple<String, String>(str, "");

		return new Tuple<String, String>(str.substring(1, idx), str.substring(idx)); // Note, subString starts at '1' to remove '$' character
	}

	/**
	 * Find variables and strings
	 */
	Tuple<List<String>, List<String>> findVariables(String str) {
		ArrayList<String> listStr = new ArrayList<String>();
		ArrayList<String> listVars = new ArrayList<String>();

		while (!str.isEmpty()) {
			//---
			// Parse string literal part
			//---
			Tuple<String, String> tupStr = findString(str);
			String strToAdd = unEscape(tupStr.first);
			listStr.add(strToAdd); // Store string
			str = tupStr.second; // Remaining to be analyzed

			//---
			// Parse variable reference part
			//---
			Tuple<String, String> tupVar = findVariableRef(str);
			listVars.add(tupVar.first); // Store variable reference
			str = tupVar.second; // Remaining to be analyzed
		}

		return new Tuple<List<String>, List<String>>(listStr, listVars);
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
				if (!Character.isLetterOrDigit(c) && countBraces == 0 && countCurly == 0) return i;
			}
		}

		return str.length();
	}

	/**
	 * How to show objects in interpolation
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	String interpolateValue(Object val) {
		if (val instanceof Map) {
			StringBuilder sb = new StringBuilder();

			// We sort keys in maps, so that contents are always the same
			Map map = (Map) val;
			ArrayList keys = new ArrayList();
			keys.addAll(map.keySet());
			Collections.sort(keys);

			int count = 0;
			sb.append("{ ");
			for (Object k : keys) {
				sb.append((count > 0 ? ", " : "") + k + " => " + map.get(k));
				count++;
			}
			sb.append(" }");

			return sb.toString();
		}

		return val.toString();
	}

	public boolean isEmpty() {
		return exprs == null || exprs.length <= 0;
	}

	/**
	 * Parse variable interpolation
	 */
	public boolean parse(String str) {
		Tuple<List<String>, List<String>> interpolated = findVariables(str);

		if (interpolated.second.isEmpty() // No variables?
				|| (interpolated.second.size() == 1 && interpolated.second.get(0).isEmpty()) // One empty variable?
		) return false; // Nothing to do

		// Something was found, we have to interpolate
		List<String> strings = interpolated.first;
		List<String> variables = interpolated.second;
		List<Expression> exprs = new ArrayList<Expression>();

		// Create and add reference
		for (String var : variables) {
			Expression varRef = factory(parent, var);
			exprs.add(varRef);
		}

		// Convert to array
		literals = strings.toArray(new String[0]);
		this.exprs = exprs.toArray(new Expression[0]);
		return !isEmpty();
	}

	@Override
	public Type returnType(Scope scope) {
		if (returnType != null) return returnType;

		returnType = Type.STRING;
		return returnType;
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
	protected void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		// Do we have any interpolated variables? Make sure they are in the scope
		if (exprs != null) //
			for (Expression expr : exprs)
				if (expr != null) expr.typeCheck(scope, compilerMessages);
	}
}
