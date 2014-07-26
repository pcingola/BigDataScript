package ca.mcgill.mcb.pcingola.bigDataScript.scope;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.Args;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.BigDataScriptNode;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.BigDataScriptNodeFactory;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.ParentNode;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeFunc;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerialize;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerializer;
import ca.mcgill.mcb.pcingola.bigDataScript.util.AutoHashMap;

/**
 * Scope: Variables, functions and classes
 *
 * @author pcingola
 */
public class Scope implements BigDataScriptSerialize, Iterable<String> {

	public static final String GLOBAL_VAR_K = "K";
	public static final String GLOBAL_VAR_M = "M";
	public static final String GLOBAL_VAR_G = "G";
	public static final String GLOBAL_VAR_T = "T";
	public static final String GLOBAL_VAR_P = "P";
	public static final String GLOBAL_VAR_MINUTE = "minute";
	public static final String GLOBAL_VAR_HOUR = "hour";
	public static final String GLOBAL_VAR_DAY = "day";
	public static final String GLOBAL_VAR_WEEK = "week";
	public static final String GLOBAL_VAR_LOCAL_CPUS = "cpusLocal";

	// Command line arguments are available in this list
	public static final String GLOBAL_VAR_ARGS_LIST = "args";

	// Program name
	public static final String GLOBAL_VAR_PROGRAM_NAME = "programName";
	public static final String GLOBAL_VAR_PROGRAM_PATH = "programPath";

	// Global scope
	private static Scope globalScope = new Scope(null, null);

	Scope parent;
	HashMap<String, ScopeSymbol> symbols;
	AutoHashMap<String, List<ScopeSymbol>> functions; // Functions can have more than one item under the same name. E.g.: f(int x), f(string s), f(int x, int y), all are called 'f'
	BigDataScriptNode node;

	/**
	 * Global scope
	 */
	public static Scope getGlobalScope() {
		return globalScope;
	}

	/**
	 * Reset Global Scope
	 */
	public static void resetGlobalScope() {
		globalScope = new Scope(null, null);
	}

	public Scope() {
		parent = getGlobalScope();
		symbols = new HashMap<String, ScopeSymbol>();
	}

	/**
	 * Constructor
	 * @param parent : If null => use global Scope
	 */
	public Scope(Scope parent, BigDataScriptNode node) {
		this.parent = parent;
		this.node = node;
		symbols = new HashMap<String, ScopeSymbol>();
	}

	public synchronized void add(ScopeSymbol symbol) {
		if (symbol.isFunction()) {
			// Create hash?
			if (functions == null) functions = new AutoHashMap<String, List<ScopeSymbol>>(new LinkedList<ScopeSymbol>());

			// Add function by name
			functions.getOrCreate(symbol.getName()).add(symbol);
		} else symbols.put(symbol.getName(), symbol);
	}

	/**
	 * Find a function that matches a function call
	 */
	public ScopeSymbol findFunction(String functionName, Args args) {
		// Retrieve all functions with the same name
		List<ScopeSymbol> ssfuncs = getFunctions(functionName);

		// Find best matching function...
		ScopeSymbol bestSsfunc = null;
		int bestScore = Integer.MAX_VALUE;
		for (ScopeSymbol ssfunc : ssfuncs) {
			boolean ok = false;
			int score = 0;
			TypeFunc sstype = (TypeFunc) ssfunc.getType();

			// Find the ones with the same number of parameters
			int argc = args.size();
			if (argc == sstype.getParameters().size()) {
				ok = true;

				// Find the ones with matching exact parameters
				for (int i = 0; i < args.size(); i++) {
					Type argType = args.getArguments()[i].getReturnType();
					Type funcType = sstype.getParameters().getType(i);

					// Same argument?
					if ((argType != null) && !argType.equals(funcType)) {
						// Can we cast?
						if (argType.canCast(funcType)) score++; // Add a point if we can cast
						else ok = false;
					}
				}
			}

			// Found anything?
			if (ok) {
				// Perfect match? Don't look any further
				if (score == 0) return ssfunc;

				// Get the one with less argument casts
				if (score < bestScore) {
					bestScore = score;
					bestSsfunc = ssfunc;
				}
			}
		}

		return bestSsfunc;
	}

	/**
	 * Find all functions whose names are 'functionName'
	 */
	public List<ScopeSymbol> getFunctions(String functionName) {
		List<ScopeSymbol> funcs = new ArrayList<ScopeSymbol>();

		for (Scope scope = this; scope != null; scope = scope.parent) {
			List<ScopeSymbol> fs = scope.getFunctionsLocal(functionName);
			if (fs != null) funcs.addAll(fs);
		}

		return funcs;
	}

	public List<ScopeSymbol> getFunctionsLocal(String functionName) {
		if (functions == null) return null;
		return functions.get(functionName);
	}

	public BigDataScriptNode getNode() {
		return node;
	}

	public Scope getParent() {
		return parent;
	}

	/**
	 * Get symbol on this scope (or any parent scope)
	 */
	public ScopeSymbol getSymbol(String symbol) {
		// Find symbol on this or any parent scope
		for (Scope scope = this; scope != null; scope = scope.parent) {
			// Try to find a symbol
			ScopeSymbol ssym = scope.getSymbolLocal(symbol);
			if (ssym != null) return ssym;

			// Try a function
			List<ScopeSymbol> fs = scope.getFunctions(symbol);
			// Since we are only matching by name, there has to be one
			// and only one function with that name
			// Note, this is limiting and very naive. A better approach is needed
			if (fs != null && fs.size() == 1) return fs.get(0);
		}

		// Nothing found
		return null;
	}

	/**
	 * Get symbol on this scope (or any parent scope if not local)
	 */
	public synchronized ScopeSymbol getSymbolLocal(String symbol) {
		if (symbols.containsKey(symbol)) return symbols.get(symbol);
		return null;
	}

	public Collection<ScopeSymbol> getSymbols() {
		return symbols.values();
	}

	public boolean hasSymbol(String symbol) {
		return getSymbol(symbol) != null;
	}

	/**
	 * Is symbol available on this scope or any parent scope?
	 */
	public boolean hasSymbolLocal(String symbol) {
		return getSymbolLocal(symbol) != null;
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

				Object val = ssym.getValue();
				sb.append(interpolate(val));

			}
		}

		return sb.toString();
	}

	/**
	 * How to show objects in interpolation
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String interpolate(Object val) {
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

	/**
	 * Is this scope empty?
	 * @return
	 */
	public boolean isEmpty() {
		return symbols.size() <= 0;
	}

	@Override
	public Iterator<String> iterator() {
		return symbols.keySet().iterator();
	}

	/**
	 * Replace fake nodes by real nodes (serialization)
	 */
	public void replaceFake() {
		node = BigDataScriptNodeFactory.get().realNode(node);
	}

	@Override
	public void serializeParse(BigDataScriptSerializer serializer) {
		// Nothing to do
		int nodeId = serializer.getNextFieldNodeId();
		if (nodeId != 0) {
			// Node is not null
			node = new ParentNode();
			node.setFakeId(nodeId);
		}
	}

	@Override
	public String serializeSave(BigDataScriptSerializer serializer) {
		StringBuilder out = new StringBuilder();
		out.append("Scope\t" + serializer.serializeSaveValue(node) + "\n");

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
		return toString(true);
	}

	public String toString(boolean showFunc) {
		// Show parents
		StringBuilder sb = new StringBuilder();
		if (parent != null) {
			String parentStr = parent.toString();
			// if (!parentStr.isEmpty()) sb.append("\n---------- Scope ----------\n" + parentStr);
			if (!parentStr.isEmpty()) sb.append(parentStr);
		}

		// Show scope symbols
		StringBuilder sbThis = new StringBuilder();
		ArrayList<ScopeSymbol> ssyms = new ArrayList<ScopeSymbol>();
		ssyms.addAll(symbols.values());
		Collections.sort(ssyms);
		for (ScopeSymbol ss : ssyms)
			sbThis.append(ss + "\n");

		// Show scope functions
		if (showFunc && functions != null) {
			for (String fname : functions.keySet())
				for (ScopeSymbol ss : functions.get(fname))
					sbThis.append(ss + "\n");
		}

		if (sbThis.length() > 0) sb.append("\n---------- Scope ----------\n" + sbThis.toString());

		return sb.toString();
	}

}
