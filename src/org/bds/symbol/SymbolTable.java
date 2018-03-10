package org.bds.symbol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bds.lang.BdsNode;
import org.bds.lang.statement.Args;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeFunction;
import org.bds.util.AutoHashMap;

/**
 * SymboTable: A table of variables, functions and classes
 *
 * @author pcingola
 */
public class SymbolTable implements Iterable<String> {

	// Global scope
	private static SymbolTable globalSymbolTable = new SymbolTable(null);

	BdsNode bdsNode;
	AutoHashMap<String, List<TypeFunction>> functions; // Functions can have more than one item under the same name. E.g.: f(int x), f(string s), f(int x, int y), all are called 'f'
	Map<String, Type> types; // Types defined within this symbol table
	Set<String> constants; // Symbols defined here are 'constant'

	public static SymbolTable get() {
		if (globalSymbolTable == null) {
			globalSymbolTable = new SymbolTable(null);
		}
		return globalSymbolTable;
	}

	public SymbolTable(BdsNode bdsNode) {
		this.bdsNode = bdsNode;
		types = new HashMap<>();
	}

	public void add(String name, Type type) {
		if (type.isFunction()) {
			// Create hash?
			if (functions == null) functions = new AutoHashMap<>(new LinkedList<TypeFunction>());

			// Add function by name
			functions.getOrCreate(name).add((TypeFunction) type);
		} else {
			types.put(name, type);
		}
	}

	/**
	 * Find a function that matches a function call
	 */
	public TypeFunction findFunction(String functionName, Args args) {
		// Retrieve all functions with the same name
		List<TypeFunction> tfuncs = getTypeFunctions(functionName);

		// Find best matching function...
		TypeFunction bestTf = null;
		int bestScore = Integer.MAX_VALUE;
		for (TypeFunction tf : tfuncs) {
			boolean ok = false;
			int score = 0;

			// Find the ones with the same number of parameters
			int argc = args.size();
			if (argc == tf.getParameters().size()) {
				ok = true;

				// Find the ones with matching exact parameters
				for (int i = 0; i < args.size(); i++) {
					Type argType = args.getArguments()[i].getReturnType();
					Type funcType = tf.getParameters().getType(i);

					// Same argument?
					if ((argType != null) && !argType.equals(funcType)) {
						// Can we cast?
						if (argType.canCastTo(funcType)) score++; // Add a point if we can cast
						else ok = false;
					}
				}
			}

			// Found anything?
			if (ok) {
				// Perfect match? Don't look any further
				if (score == 0) return tf;

				// Get the one with less argument casts
				if (score < bestScore) {
					bestScore = score;
					bestTf = tf;
				}
			}
		}

		return bestTf;
	}

	/**
	 * Find all functions
	 */
	public List<TypeFunction> getFunctions() {
		if (functions == null) return null;
		List<TypeFunction> funcs = new ArrayList<>();

		for (String fname : functions.keySet())
			funcs.addAll(functions.get(fname));

		return funcs;
	}

	public SymbolTable getParent() {
		for (BdsNode n = bdsNode.getParent(); n != null; n = n.getParent()) {
			if (n.getSymbolTable() != null) return n.getSymbolTable();
		}
		return null;
	}

	/**
	 * Get symbol on this scope (or any parent scope)
	 */
	public Type getType(String name) {
		// Find symbol on this or any parent scope
		for (SymbolTable symtab = this; symtab != null; symtab = symtab.getParent()) {
			// Try to find a symbol
			Type ssym = symtab.getTypeLocal(name);
			if (ssym != null) return ssym;

			// Try a function
			List<TypeFunction> fs = symtab.getTypeFunctionsLocal(name);
			// Since we are only matching by name, there has to be one
			// and only one function with that name
			// Note, this is limiting and very naive. A better approach is needed
			if (fs != null && fs.size() == 1) return fs.get(0);
		}

		// Nothing found
		return null;
	}

	/**
	 * Find all functions whose names are 'functionName'
	 */
	public List<TypeFunction> getTypeFunctions(String functionName) {
		List<TypeFunction> funcs = new ArrayList<>();

		for (SymbolTable scope = this; scope != null; scope = scope.getParent()) {
			List<TypeFunction> fs = scope.getTypeFunctionsLocal(functionName);
			if (fs != null) funcs.addAll(fs);
		}

		return funcs;
	}

	/**
	 * Find all functions whose names are 'functionName' (only look in this symboltable)
	 */
	public List<TypeFunction> getTypeFunctionsLocal(String functionName) {
		if (functions == null) return null;
		return functions.get(functionName);
	}

	/**
	 * Get symbol on this scope (only search this scope)
	 */
	public synchronized Type getTypeLocal(String name) {
		return types.get(name);
	}

	public boolean hasFunctions() {
		return functions != null && !functions.isEmpty();
	}

	public boolean hasType(String name) {
		return getType(name) != null;
	}

	/**
	 * Is symbol available on this scope or any parent scope?
	 */
	public boolean hasTypeLocal(String symbol) {
		return getTypeLocal(symbol) != null || getTypeFunctionsLocal(symbol) != null;
	}

	public boolean isConstant(String name) {
		return constants != null && constants.contains(name);
	}

	/**
	 * Is this scope empty?
	 */
	public boolean isEmpty() {
		return types.isEmpty();
	}

	@Override
	public Iterator<String> iterator() {
		return types.keySet().iterator();
	}

	public void setConstant(String name) {
		if (constants == null) constants = new HashSet<>();
		constants.add(name);
	}

	@Override
	public String toString() {
		return toString(true);
	}

	public String toString(boolean showFunc) {
		// Show parents
		StringBuilder sb = new StringBuilder();
		SymbolTable parent = getParent();
		if (parent != null) {
			String parentStr = parent.toString(showFunc);
			if (!parentStr.isEmpty()) sb.append(parentStr);
		}

		// Show scope symbols
		StringBuilder sbThis = new StringBuilder();
		List<String> names = new ArrayList<>();
		names.addAll(types.keySet());
		Collections.sort(names);
		for (String name : names)
			sbThis.append(types.get(name) + " " + name + "\n");

		// Show scope functions
		if (showFunc && functions != null) {
			for (String fname : functions.keySet())
				for (TypeFunction tf : functions.get(fname))
					sbThis.append(tf.getReturnType() + " " + fname + "(" + tf.getParameters() + ")" + "\n");
		}

		// Show header
		String symTabName = "";
		if (bdsNode != null) symTabName = bdsNode.getFileName() + ":" + bdsNode.getLineNum();
		if (sbThis.length() > 0) sb.append("\n---------- SymbolTable " + symTabName + "  ----------\n" + sbThis.toString());

		return sb.toString();
	}
}
