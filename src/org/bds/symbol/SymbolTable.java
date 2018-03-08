package org.bds.symbol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bds.lang.statement.Args;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeFunction;
import org.bds.scope.Scope;
import org.bds.scope.ScopeSymbol;
import org.bds.util.AutoHashMap;

/**
 * SymboTable: A table of variables, functions and classes
 *
 * @author pcingola
 */
public class SymbolTable implements Iterable<String> {

	SymbolTable parent;
	AutoHashMap<String, List<TypeFunction>> functions; // Functions can have more than one item under the same name. E.g.: f(int x), f(string s), f(int x, int y), all are called 'f'
	Map<String, Type> types; // Types defined within this symbol table

	public SymbolTable() {
		parent = null;
	}

	/**
	 * Constructor
	 * @param parent : If null => use global symbol table
	 */
	public SymbolTable(SymbolTable parent) {
		this.parent = parent;
	}

	public void add(String name, Type type) {
		if (type.isFunction()) {
			// Create hash?
			if (functions == null) functions = new AutoHashMap<>(new LinkedList<TypeFunction>());

			// Add function by name
			functions.getOrCreate(name).add((TypeFunction) type);
		} else types.put(name, type);
	}

	/**
	 * Add all variables from a scope
	 */
	public void addAll(Scope scope) {
		for (String name : scope) {
			ScopeSymbol ss = scope.getSymbol(name);
			add(name, ss.getType());
		}
	}

	/**
	 * Find a function that matches a function call
	 */
	public TypeFunction findFunction(String functionName, Args args) {
		// Retrieve all functions with the same name
		List<TypeFunction> tfuncs = getFunctions(functionName);

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

	/**
	 * Find all functions whose names are 'functionName'
	 */
	public List<TypeFunction> getFunctions(String functionName) {
		List<TypeFunction> funcs = new ArrayList<>();

		for (SymbolTable scope = this; scope != null; scope = scope.parent) {
			List<TypeFunction> fs = scope.getFunctionsLocal(functionName);
			if (fs != null) funcs.addAll(fs);
		}

		return funcs;
	}

	/**
	 * Find all functions whose names are 'functionName' (only look in this symboltable)
	 */
	public List<TypeFunction> getFunctionsLocal(String functionName) {
		if (functions == null) return null;
		return functions.get(functionName);
	}

	public SymbolTable getParent() {
		return parent;
	}

	/**
	 * Get symbol on this scope (or any parent scope)
	 */
	public Type getType(String name) {
		// Find symbol on this or any parent scope
		for (SymbolTable scope = this; scope != null; scope = scope.parent) {
			// Try to find a symbol
			Type ssym = scope.getTypeLocal(name);
			if (ssym != null) return ssym;

			// Try a function
			List<TypeFunction> fs = scope.getFunctionsLocal(name);
			// Since we are only matching by name, there has to be one
			// and only one function with that name
			// Note, this is limiting and very naive. A better approach is needed
			if (fs != null && fs.size() == 1) return fs.get(0);
		}

		// Nothing found
		return null;
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

	public boolean hasSymbol(String name) {
		return getType(name) != null;
	}

	/**
	 * Is symbol available on this scope or any parent scope?
	 */
	public boolean hasSymbolLocal(String symbol) {
		return getTypeLocal(symbol) != null || getFunctionsLocal(symbol) != null;
	}

	/**
	 * Is this scope empty?
	 */
	public boolean isEmpty() {
		return types.isEmpty() && (functions == null || functions.isEmpty());
	}

	@Override
	public Iterator<String> iterator() {
		return types.keySet().iterator();
	}

	@Override
	public String toString() {
		return toString(true);
	}

	public String toString(boolean showFunc) {
		// Show parents
		StringBuilder sb = new StringBuilder();
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
		if (sbThis.length() > 0) sb.append("\n---------- SymbolTable ----------\n" + sbThis.toString());

		return sb.toString();
	}
}
