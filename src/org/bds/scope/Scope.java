package org.bds.scope;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.bds.lang.BdsNode;
import org.bds.lang.BdsNodeFactory;
import org.bds.lang.ParentNode;
import org.bds.lang.statement.Args;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeFunction;
import org.bds.serialize.BdsSerialize;
import org.bds.serialize.BdsSerializer;
import org.bds.util.AutoHashMap;

/**
 * Scope: Variables, functions and classes
 *
 * @author pcingola
 */
public class Scope implements BdsSerialize, Iterable<String> {

	private static int scopeNum = 0;

	int id;
	Scope parent;
	String parentNodeId;
	HashMap<String, ScopeSymbol> symbols;
	AutoHashMap<String, List<ScopeSymbol>> functions; // Functions can have more than one item under the same name. E.g.: f(int x), f(string s), f(int x, int y), all are called 'f'
	BdsNode node;

	protected static int nextId() {
		return ++scopeNum;
	}

	public Scope() {
		parent = GlobalScope.get();
		node = null;
		id = nextId();
		symbols = new HashMap<>();
	}

	/**
	 * Constructor
	 * @param parent : If null => use global Scope
	 */
	public Scope(Scope parent, BdsNode node) {
		this.parent = parent;
		this.node = node;
		id = nextId();

		symbols = new HashMap<>();
		if (node != null) copy(node.getScope()); // Copy symbols from other scope
	}

	public synchronized void add(ScopeSymbol symbol) {
		if (symbol.isFunction()) {
			// Create hash?
			if (functions == null) functions = new AutoHashMap<>(new LinkedList<ScopeSymbol>());

			// Add function by name
			functions.getOrCreate(symbol.getName()).add(symbol);
		} else symbols.put(symbol.getName(), symbol);
	}

	/**
	 * Add all symbols from 'scope'
	 */
	public synchronized void addAll(Scope scope) {
		for (String name : scope)
			add(scope.getSymbol(name));
	}

	/**
	 * Copy symbols from other scope
	 */
	void copy(Scope oscope) {
		if (oscope == null) return;

		// Note: Symbols are added at variable declaration time (i.e. when we evaluate a 'VarDeclaration')
		//       So we should not copy them here (otherwise we'd get 'Duplicate symbol error')

		// Copy functions
		if (oscope.hasFunctions()) {
			for (ScopeSymbol ss : oscope.getFunctions())
				add(ss);
		}
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
			TypeFunction sstype = (TypeFunction) ssfunc.getType();

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
						if (argType.canCastTo(funcType)) score++; // Add a point if we can cast
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
	 * Find all functions
	 */
	public List<ScopeSymbol> getFunctions() {
		if (functions == null) return null;
		List<ScopeSymbol> funcs = new ArrayList<>();

		for (String fname : functions.keySet())
			funcs.addAll(functions.get(fname));

		return funcs;
	}

	/**
	 * Find all functions whose names are 'functionName'
	 */
	public List<ScopeSymbol> getFunctions(String functionName) {
		List<ScopeSymbol> funcs = new ArrayList<>();

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

	public BdsNode getNode() {
		return node;
	}

	@Override
	public String getNodeId() {
		return getClass().getSimpleName() + ":" + id;
	}

	public Scope getParent() {
		return parent;
	}

	public String getParentNodeId() {
		return parentNodeId;
	}

	public String getScopeName() {
		if (node == null) return "Global";
		return (node.getFileName() != null ? node.getFileName() + ":" + node.getLineNum() + ":" : "") + node.getClass().getSimpleName();
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
			List<ScopeSymbol> fs = scope.getFunctionsLocal(symbol);
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
	public synchronized ScopeSymbol getSymbolLocal(String symbol) {
		return symbols.get(symbol);
	}

	public Collection<ScopeSymbol> getSymbols() {
		return symbols.values();
	}

	public boolean hasFunctions() {
		return functions != null && !functions.isEmpty();
	}

	public boolean hasSymbol(String symbol) {
		return getSymbol(symbol) != null;
	}

	/**
	 * Is symbol available on this scope or any parent scope?
	 */
	public boolean hasSymbolLocal(String symbol) {
		return getSymbolLocal(symbol) != null || getFunctionsLocal(symbol) != null;
	}

	/**
	 * Is this scope empty?
	 */
	public boolean isEmpty() {
		return symbols.isEmpty() && (functions == null || functions.isEmpty());
	}

	@Override
	public Iterator<String> iterator() {
		return symbols.keySet().iterator();
	}

	//	public Object peek() {
	//		return stack.getFirst();
	//	}
	//
	//	public Object pop() {
	//		return stack.removeFirst();
	//	}
	//
	//	public void push(Object obj) {
	//		if (stack == null) stack = new LinkedList<>();
	//		stack.addFirst(obj);
	//	}

	/**
	 * Replace fake nodes by real nodes (serialization)
	 */
	public void replaceFake() {
		node = BdsNodeFactory.get().realNode(node);
	}

	@Override
	public void serializeParse(BdsSerializer serializer) {
		// Nothing to do
		id = (int) serializer.getNextFieldInt();
		parentNodeId = serializer.getNextFieldString();
		int nodeId = serializer.getNextFieldNodeId();

		if (nodeId != 0) {
			// Node is not null
			node = new ParentNode();
			node.setFakeId(nodeId);
		}

		if (id > scopeNum) scopeNum = id + 1;
	}

	@Override
	public String serializeSave(BdsSerializer serializer) {
		StringBuilder out = new StringBuilder();
		out.append("Scope");
		out.append("\t" + serializer.serializeSaveValue(id));
		out.append("\t" + serializer.serializeSaveValue(parent != null ? parent.getNodeId() : ""));
		out.append("\t" + serializer.serializeSaveValue(node));
		out.append("\n");

		for (ScopeSymbol ss : symbols.values()) {
			if (ss.getType().isFunctionNative()) {
				; // Do not save native functions
			} else out.append(serializer.serializeSave(ss));
		}

		if (parent != null) out.append(serializer.serializeSave(parent));

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
			String parentStr = parent.toString(showFunc);
			if (!parentStr.isEmpty()) sb.append(parentStr);
		}

		// Show scope symbols
		StringBuilder sbThis = new StringBuilder();
		ArrayList<ScopeSymbol> ssyms = new ArrayList<>();
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

		// Show header
		if (sbThis.length() > 0) sb.append("\n---------- Scope " + getScopeName() + " ----------\n" + sbThis.toString());

		return sb.toString();
	}

	public String toStringScopeNames() {
		StringBuilder sb = new StringBuilder();
		sb.append("Scopes:\n");

		int i = 0;
		for (Scope scope = this; scope != null; scope = scope.getParent())
			sb.append("\t" + (i++) + ": " + scope.getScopeName() + "\n");

		return sb.toString();
	}

}
