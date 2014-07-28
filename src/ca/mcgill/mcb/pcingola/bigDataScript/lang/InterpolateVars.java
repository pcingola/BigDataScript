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

	enum Context {
		STRING, VAR_NAME, LIST_INDEX, MAP_KEY;

		boolean isVar() {
			return this != STRING;
		}
	}

	List<String> strings; // This is used in case of interpolated string literal
	List<Reference> varRefs; // This is used in case of interpolated string literal;

	public InterpolateVars(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	public Object eval(BigDataScriptThread bdsThread) {
		return interpolate(bdsThread);
	}

	/**
	 * Create a reference form a string
	 */
	Reference factory(String var) {
		if (var == null || var.isEmpty()) return null;

		Reference varRef = null;
		if (var.indexOf('{') > 0) varRef = new VarReferenceMap(parent, null);
		else if (var.indexOf('[') > 0) varRef = new VarReferenceList(parent, null);
		else varRef = new VarReference(parent, null);

		// Parse string
		varRef.parse(var);
		return varRef;
	}

	/**
	 * Split a string (to be interpolated) into a list of strings and a list ov variable names
	 *
	 * @param str
	 * @return A tuple containing a list of strings and a list of variables
	 */
	Tuple<List<String>, List<String>> findVariables(String str) {
		ArrayList<String> listStr = new ArrayList<String>();
		ArrayList<String> listVars = new ArrayList<String>();

		StringBuilder sbStr = new StringBuilder();
		StringBuilder sbVar = new StringBuilder();
		char cprev = ' ';
		Context context = Context.STRING;
		for (char c : str.toCharArray()) {

			// Update context for this char
			context = isVarInterpolatedString(c, cprev, context);

			if (!context.isVar() && sbVar.length() > 0) {
				// End of variable name
				String varName = sbVar.toString().substring(1); // Add variable name (without leading '$')

				listVars.add(varName);
				if (varName.isEmpty()) sbStr.append('$'); // This was just an isolated '$'
				listStr.add(sbStr.toString());

				sbStr = new StringBuilder();
				sbVar = new StringBuilder();
			}

			// New variable name?
			// Note that we can have "some string $var1$var2 ..."
			if (cprev == '\\') {

				// Convert other characters
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

					default:
						break;
					}

					(context.isVar() ? sbVar : sbStr).append(c);
				}
			} else if (c == '$') {
				sbVar.append(c);
			} else if (c != '\\') {
				(context.isVar() ? sbVar : sbStr).append(c);
			}

			cprev = c;
		}

		// Add last one
		if ((sbVar.length() > 0) || (sbStr.length() > 0)) {
			String varName = "";
			if (context.isVar()) {
				if (sbVar.length() > 0) varName = sbVar.toString().substring(1);
				if (varName.isEmpty()) sbStr.append('$'); // This was just an ending '$'
			}

			listVars.add(varName);
			listStr.add(sbStr.toString());
		}

		return new Tuple<List<String>, List<String>>(listStr, listVars);
	}

	/**
	 * Interpolate a string
	 * @param strings : List of string (from GprString.findVariables)
	 * @param variables : A list of variable names (from GprString.findVariables)
	 * @return An interpolated string
	 */
	String interpolate(BigDataScriptThread bdsThread) {
		StringBuilder sb = new StringBuilder();

		// Variable interpolation
		for (int i = 0; i < strings.size(); i++) {
			// String before variable
			sb.append(strings.get(i));

			// Variable's value
			Reference ref = varRefs.get(i);
			if (ref != null) {
				Object val = ref.eval(bdsThread);
				sb.append(interpolateValue(val));
			}
		}

		return sb.toString();
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
		return varRefs == null || varRefs.isEmpty();
	}

	/**
	 * Is this a variable char in an interpolated string?
	 */
	Context isVarInterpolatedString(char c, char cprev, Context context) {
		if (Character.isLetterOrDigit(c)) return context; // Nothing changed
		if (c == '$' && context == Context.STRING) return Context.VAR_NAME;

		switch (c) {
		case ']':
			if (context == Context.LIST_INDEX) return Context.STRING; // Finished list reference, back to string context
			return context;

		case '[':
			if (context == Context.VAR_NAME) return Context.LIST_INDEX; // Start list index
			return context;

		case '{':
			if (context == Context.VAR_NAME) return Context.MAP_KEY; // Start map key
			return context;

		case '\'':
			if (context == Context.MAP_KEY) return Context.MAP_KEY; // Start or end of map key
			return Context.STRING; // Nothing changed

		case '}':
			if (context == Context.MAP_KEY) return Context.STRING; // Finished map reference, back to string context
			return context; // Nothing changed

		default:
			if (context == Context.VAR_NAME && !Character.isLetterOrDigit(c)) return Context.STRING; // End of varName context
			return context; // Nothing changed
		}
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
		strings = interpolated.first;
		List<String> variables = interpolated.second;
		varRefs = new ArrayList<Reference>();

		// Create and add reference
		for (String var : variables) {
			Reference varRef = factory(var);
			varRefs.add(varRef);
		}

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

		for (int i = 0; i < strings.size(); i++) {
			if (sb.length() > 0) sb.append(" + ");
			sb.append("\"" + strings.get(i) + "\"");
			if (varRefs.get(i) != null) sb.append(" + " + varRefs.get(i));
		}

		return sb.toString();
	}

	@Override
	protected void typeCheckNotNull(Scope scope, CompilerMessages compilerMessages) {
		// Do we have any interpolated variables? Make sure they are in the scope
		if (varRefs != null) //
			for (Reference var : varRefs)
				if (var != null) var.typeCheck(scope, compilerMessages);
	}
}
