package ca.mcgill.mcb.pcingola.bigDataScript.scope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeFunc;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerialize;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerializer;

/**
 * Scope: Variables, functions and classes 
 * 
 * @author pcingola
 */
public class Scope implements BigDataScriptSerialize {

	public static final String GLOBAL_VAR_K = "K";
	public static final String GLOBAL_VAR_M = "M";
	public static final String GLOBAL_VAR_G = "G";
	public static final String GLOBAL_VAR_T = "T";
	public static final String GLOBAL_VAR_P = "P";
	public static final String GLOBAL_VAR_LOCAL_CPUS = "cpusLocal";

	// Command line arguments are avaialble sing this list
	public static final String VAR_ARGS_LIST = "args";

	// Program name
	public static final String VAR_PROGRAM_NAME = "programName";

	private static Scope globalScope = new Scope(null);

	Scope parent;
	HashMap<String, ScopeSymbol> symbols;

	/**
	 * Global scope
	 * @return
	 */
	public static Scope getGlobalScope() {
		return globalScope;
	}

	/**
	 * Reset Global Scope
	 */
	public static void resetGlobalScope() {
		globalScope = new Scope(null);
	}

	public Scope() {
		parent = getGlobalScope();
		symbols = new HashMap<String, ScopeSymbol>();
	}

	/**
	 * Constructor
	 * @param parent : If null => use global Scope
	 */
	public Scope(Scope parent) {
		this.parent = parent;
		symbols = new HashMap<String, ScopeSymbol>();
	}

	public synchronized void add(ScopeSymbol symbol) {
		symbols.put(symbol.getName(), symbol);
	}

	/**
	 * Find all functions whose names are 'functionName'
	 * @param functionName
	 * @return
	 */
	public List<ScopeSymbol> getFunctions(String functionName) {
		ArrayList<ScopeSymbol> funcs = new ArrayList<ScopeSymbol>();

		// Find all functions
		for (ScopeSymbol ss : symbols.values()) {
			if (ss.getType().isFunction()) {
				TypeFunc tf = (TypeFunc) ss.getType();
				if (tf.getFunctionName().equals(functionName)) funcs.add(ss);
			}
		}

		// Recurse
		if (parent != null) funcs.addAll(parent.getFunctions(functionName));

		return funcs;
	}

	public Scope getParent() {
		return parent;
	}

	/**
	 * Get symbol on this scope (or any parent scope)
	 * @param symbol
	 * @return
	 */
	public ScopeSymbol getSymbol(String symbol) {
		return getSymbol(symbol, false);
	}

	/**
	 * Get symbol on this scope (or any parent scope if not local)
	 * @param symbol
	 * @return
	 */
	public synchronized ScopeSymbol getSymbol(String symbol, boolean local) {
		if (symbols.containsKey(symbol)) return symbols.get(symbol);
		if ((parent != null) && !local) return parent.getSymbol(symbol, local);
		return null;
	}

	/**
	 * Is symbol available on this scope or any parent scope?
	 * @param symbol
	 * @return
	 */
	public boolean hasSymbol(String symbol, boolean local) {
		return getSymbol(symbol, local) != null;
	}

	/**
	 * Interpolate a string
	 * @param strings : List of string (from GprString.findVariables)
	 * @param variables : A list of variable names (from GprString.findVariables)
	 * @return An interpolated string
	 */
	public String interpolate(List<String> strings, List<String> variables) {
		StringBuilder sb = new StringBuilder();

		// Variable interpolation
		for (int i = 0; i < strings.size(); i++) {
			// String before variable
			sb.append(strings.get(i));

			// Variable's value
			String varName = variables.get(i);
			if (!varName.isEmpty()) {
				ScopeSymbol ssym = getSymbol(varName);
				sb.append(ssym.getValue().toString());
			}
		}

		return sb.toString();
	}

	/**
	 * Is this scope empty?
	 * @return
	 */
	public boolean isEmpty() {
		return symbols.size() <= 0;
	}

	@Override
	public void serializeParse(BigDataScriptSerializer serializer) {
		// Nothing to do
	}

	@Override
	public String serializeSave(BigDataScriptSerializer serializer) {
		StringBuilder out = new StringBuilder();
		out.append("Scope\n");

		for (ScopeSymbol ss : symbols.values()) {
			if (ss.getType().isNative()) {
				; // Do not save native functions
			} else out.append(ss.serializeSave(serializer));
		}

		if (parent != null) out.append(parent.serializeSave(serializer));

		return out.toString();
	}

	public void setParent(Scope parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		// if (symbols.isEmpty()) return "";

		// Show parents
		StringBuilder sb = new StringBuilder();
		if (parent != null) {
			String parentStr = parent.toString();
			if (!parentStr.isEmpty()) sb.append("\n---------- Scope ----------\n" + parentStr);
		}

		// Show current
		StringBuilder sbThis = new StringBuilder();
		for (ScopeSymbol ss : symbols.values())
			sbThis.append(ss + "\n");
		if (sbThis.length() > 0) sb.append("\n---------- Scope ----------\n" + sbThis.toString());

		return sb.toString();
	}

}
