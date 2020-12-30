package org.bds.lang.value;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.Expression;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.symbol.SymbolTable;
import org.bds.util.GprString;
import org.bds.util.Tuple;

public class InterpolateVars extends Literal {

	private static final long serialVersionUID = 5380913311800422951L;

	static boolean debug = false;

	// boolean useLiteral;
	String literals[]; // This is used in case of interpolated string literal
	String exprStrs[];
	Expression exprs[]; // This is used in case of interpolated string literal; Usually these are VarReferences, but they might change to generic expressions in the future

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
			String strToAdd = GprString.unescapeDollar(tupStr.first);
			debug("Interpolate string: |" + str + "|\n\tstring: |" + tupStr.first + "|\n\trest: |" + tupStr.second + "|");
			listStr.add(strToAdd); // Store string
			str = tupStr.second; // Remaining to be analyzed

			//---
			// Parse variable reference part
			//---
			Tuple<String, String> tupVar = findVariableRef(str);
			listVars.add(tupVar.first); // Store variable reference
			debug("Interpolate variables: |" + str + "|\n\tstring: |" + tupVar.first + "|\n\trest: |" + tupVar.second + "|");
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

	@Override
	public Type getReturnType() {
		return Types.STRING;
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
		return Character.isLetterOrDigit(c) || (c == '_') || (c == '.');
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
		List<String> exprStrs = new ArrayList<>();

		// Create and add reference
		for (String exprStr : variables) {
			exprStr = exprStr.trim();
			Expression expr = Expression.factory(parent, exprStr);
			exprStrs.add(exprStr);
			exprs.add(expr);
		}

		// Convert to array
		literals = strings.toArray(new String[0]);
		this.exprs = exprs.toArray(new Expression[0]);
		this.exprStrs = exprStrs.toArray(new String[0]);
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
		if (count == 2) sb.append("adds\n");
		else sb.append("addsm " + count + "\n");

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
		if (exprs != null) {
			for (int i = 0; i < exprs.length; i++) {
				Expression expr = exprs[i];
				String exprStr = exprStrs[i];
				if (expr != null) expr.typeCheck(symtab, compilerMessages);
				else if (!exprStr.isEmpty()) {
					// Non-empty strings should compile
					compilerMessages.add(this, "Could not compile expression '" + exprStrs[i] + "' in interpolated string", MessageType.ERROR);
				}
			}
		}
	}

}
