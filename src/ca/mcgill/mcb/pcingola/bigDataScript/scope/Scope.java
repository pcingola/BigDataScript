package ca.mcgill.mcb.pcingola.bigDataScript.scope;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.BigDataScriptNode;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.BigDataScriptNodeFactory;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.ParentNode;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeFunc;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerialize;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerializer;

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
	BigDataScriptNode node;

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

	public BigDataScriptNode getNode() {
		return node;
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

	public Collection<ScopeSymbol> getSymbols() {
		return symbols.values();
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

				Object val = ssym.getValue();
				sb.append(interpolate(val));

			}
		}

		return sb.toString();
	}

	/**
	 * How to show objects in interpolation
	 * @param val
	 * @return
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
			if (!parentStr.isEmpty()) sb.append("\n---------- Scope ----------\n" + parentStr);
		}

		// Show current
		StringBuilder sbThis = new StringBuilder();
		ArrayList<ScopeSymbol> ssyms = new ArrayList<ScopeSymbol>();
		ssyms.addAll(symbols.values());
		Collections.sort(ssyms);
		for (ScopeSymbol ss : ssyms)
			if (!ss.getType().isFunction() || showFunc) sbThis.append(ss + "\n");

		if (sbThis.length() > 0) sb.append("\n---------- Scope ----------\n" + sbThis.toString());

		return sb.toString();
	}
}
