package org.bds.symbol;

import java.io.Serializable;
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
import org.bds.lang.Parameters;
import org.bds.lang.statement.Args;
import org.bds.lang.statement.ClassDeclaration;
import org.bds.lang.statement.FunctionDeclaration;
import org.bds.lang.statement.MethodDeclaration;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeClass;
import org.bds.lang.value.ValueFunction;
import org.bds.util.AutoHashMap;

/**
 * SymboTable: A table of variables, functions and classes
 *
 * @author pcingola
 */
public class SymbolTable implements Serializable, Iterable<String> {

	private static final long serialVersionUID = 7091121153416204307L;

	public static final String INTERNAL_SYMBOL_START = "$";

	BdsNode bdsNode;
	AutoHashMap<String, List<ValueFunction>> functions; // Functions can have more than one item under the same name. E.g.: f(int x), f(string s), f(int x, int y), all are called 'f'
	Map<String, Type> variableTypes; // Variables defined within this symbol table
	Set<String> constants; // Symbols defined here are 'constant'

	public SymbolTable(BdsNode bdsNode) {
		this.bdsNode = bdsNode;
		variableTypes = new HashMap<>();
	}

	/**
	 * Add a function definition
	 */
	public void addFunction(FunctionDeclaration fdecl) {
		// Create hash?
		if (functions == null) functions = new AutoHashMap<>(new LinkedList<ValueFunction>());

		// Add function by name
		String name = fdecl.getFunctionName();
		ValueFunction vf = new ValueFunction(fdecl);
		functions.getOrCreate(name).add(vf);
	}

	/**
	 * Add a variable - type association
	 */
	public void addVariable(String name, Type type) {
		variableTypes.put(name, type);
	}

	/**
	 * Find a native function or method by class
	 */
	public ValueFunction findFunction(Class<? extends FunctionDeclaration> clazz) {
		for (SymbolTable symtab = this; symtab != null; symtab = symtab.getParent()) {
			for (List<ValueFunction> lvf : symtab.functions.values()) {
				for (ValueFunction vf : lvf) {
					FunctionDeclaration fd = vf.getFunctionDeclaration();
					if (fd.getClass() == clazz) return vf;
				}
			}
		}
		return null;
	}

	/**
	 * Find a function that matches a function call
	 */
	public ValueFunction findFunction(String functionName, Args args) {
		// Retrieve all functions with the same name
		List<ValueFunction> vfuncs = getValueFunctions(functionName);

		// Find best matching function...
		ValueFunction bestVf = null;
		int bestScore = Integer.MAX_VALUE;
		for (ValueFunction vf : vfuncs) {
			boolean ok = false;
			int score = 0;

			// Find the ones with the same number of parameters
			int argc = args.size();
			if (argc == vf.getParameters().size()) {
				ok = true;

				// Find the ones with matching exact parameters
				for (int i = 0; i < args.size(); i++) {
					Type argType = args.getArguments()[i].getReturnType();
					Type funcType = vf.getParameters().getType(i);

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
				if (score == 0) return vf;

				// Get the one with less argument casts
				if (score < bestScore) {
					bestScore = score;
					bestVf = vf;
				}
			}
		}

		return bestVf;
	}

	/**
	 * Find a method from function declaration
	 */
	public MethodDeclaration findMethod(FunctionDeclaration fdecl) {
		if (functions == null) return null;

		Parameters pars = fdecl.getParameters();

		// Find all methods with same name
		List<ValueFunction> functs = functions.get(fdecl.getFunctionName());
		if (functs == null) return null;

		for (ValueFunction vf : functs) {
			FunctionDeclaration fd = vf.getFunctionDeclaration();
			Parameters fdp = fd.getParameters();
			if (pars.equalsMethod(fdp)) return (MethodDeclaration) fd;
		}

		return null; // Not found
	}

	/**
	 * Find all functions
	 */
	public List<ValueFunction> getFunctions() {
		if (functions == null) return null;
		List<ValueFunction> funcs = new ArrayList<>();

		for (String fname : functions.keySet())
			funcs.addAll(functions.get(fname));

		return funcs;
	}

	protected String getName() {
		if (bdsNode != null) return bdsNode.getFileName() + ":" + bdsNode.getLineNum();
		return "";
	}

	public SymbolTable getParent() {
		for (BdsNode n = bdsNode.getParent(); n != null; n = n.getParent()) {
			if (n.getSymbolTable() != null) return n.getSymbolTable();
		}
		return GlobalSymbolTable.get(); // No parent node? Then the SymbolTable parent is 'GlobalSymbolTable'
	}

	/**
	 * Get type for symbol 'this.name'. If not found, search in any parent scope.
	 * Note: It does not try to solve in local scopes (i.e. only class fields, not local variables)
	 */
	public Type getTypeThis(String name) {
		// Find symbol on this or any parent scope
		for (SymbolTable symtab = this; symtab != null; symtab = symtab.getParent()) {
			// Resolve 'name'
			Type t = symtab.resolveThis(name);
			if (t != null) return t;
		}

		// Nothing found
		return null;
	}

	/**
	 * Find all functions whose names are 'functionName'
	 */
	public List<ValueFunction> getValueFunctions(String functionName) {
		List<ValueFunction> funcs = new ArrayList<>();

		for (SymbolTable symtab = this; symtab != null; symtab = symtab.getParent()) {
			List<ValueFunction> fs = symtab.getValueFunctionsLocal(functionName);
			if (fs != null) funcs.addAll(fs);
		}

		return funcs;
	}

	/**
	 * Find all functions whose names are 'functionName' (only look in this symbol table)
	 */
	public List<ValueFunction> getValueFunctionsLocal(String functionName) {
		if (functions == null) return null;
		return functions.get(functionName);
	}

	/**
	 * Get type for variable 'name'.
	 * If not found, search in any parent scope.
	 */
	public Type getVariableType(String name) {
		// Find symbol on this or any parent scope
		for (SymbolTable symtab = this; symtab != null; symtab = symtab.getParent()) {
			// Resolve 'name'
			if (!symtab.isEmpty()) {
				Type t = symtab.getVariableTypeLocal(name);
				if (t != null) return t;
			}
		}

		// Nothing found
		return null;
	}

	/**
	 * Get symbol on this scope (only search this scope)
	 */
	public synchronized Type getVariableTypeLocal(String name) {
		return variableTypes.get(name);
	}

	public boolean hasFunctions() {
		return functions != null && !functions.isEmpty();
	}

	/**
	 * Does the symbol table have another function with the same signature?
	 */
	public boolean hasOtherFunction(FunctionDeclaration fdecl) {
		if (!hasFunctions()) return false;
		String signature = fdecl.signature();
		for (List<ValueFunction> vfuncs : functions.values()) {
			for (ValueFunction vf : vfuncs) {
				FunctionDeclaration fd = vf.getFunctionDeclaration();
				// Same signature and different declaration? We've found two function with the same signature
				if (fd.signature().equals(signature) && fd != fdecl) return true;
			}
		}
		return false;
	}

	public boolean hasType(String name) {
		return resolve(name) != null;
	}

	/**
	 * Is symbol available on this scope or any parent scope?
	 */
	public boolean hasTypeLocal(String symbol) {
		return getVariableTypeLocal(symbol) != null || getValueFunctionsLocal(symbol) != null;
	}

	public boolean isConstant(String name) {
		return constants != null && constants.contains(name);
	}

	/**
	 * Is this scope empty?
	 */
	public boolean isEmpty() {
		return variableTypes.isEmpty();
	}

	/**
	 * Does 'name' resolve as a variable or a class field?
	 */
	public boolean isField(String name) {
		// Find symbol on this or any parent scope
		for (SymbolTable symtab = this; symtab != null; symtab = symtab.getParent()) {
			if (symtab.resolveLocal(name) != null) return false; // Local variable => not a field
			if (symtab.resolveThis(name) != null) return true; // Class field
		}

		// Nothing found
		return false;
	}

	@Override
	public Iterator<String> iterator() {
		return variableTypes.keySet().iterator();
	}

	/**
	 * Get type for symbol 'name': Could be a variable, function or method.
	 * If not found, search in any parent scope.
	 */
	public Type resolve(String name) {
		// Find symbol on this or any parent scope
		for (SymbolTable symtab = this; symtab != null; symtab = symtab.getParent()) {
			// Resolve 'name'
			if (!symtab.isEmpty()) {
				Type t = symtab.resolveLocalOrThis(name);
				if (t != null) return t;
			}
		}

		// Nothing found
		return null;
	}

	/**
	 * Resolve symbol in local symbol table (no recursion)
	 */
	public Type resolveLocal(String name) {
		// Try to find 'name' as a variable in local symbol table
		Type t = getVariableTypeLocal(name);
		if (t != null) return t;

		// Is it a function?
		// Try a function in local symbol table
		List<ValueFunction> fs = getValueFunctionsLocal(name);
		// Since we are only matching by name, there has to be one
		// and only one function with that name
		// Note, this is limiting and very naive. A better approach is needed
		if (fs != null) {
			// If there is only one symbol, we can resolve it
			// Otherwise, we don't know which one of the symbols we are trying to get
			// E.g. multiple functions with the same name and different parameters
			if (fs.size() == 1) return fs.get(0).getType();
		}

		return null;
	}

	/**
	 * Resolve symbol in local symbol table (no recursion)
	 */
	Type resolveLocalOrThis(String name) {
		// Find 'name' in local table
		Type t = resolveLocal(name);
		if (t != null) return t;
		// Not found? Try 'this.name'
		return resolveThis(name);
	}

	/**
	 * Resolve 'this' symbol in local symbol table and find 'name' within the class (no recursion)
	 */
	public Type resolveThis(String name) {
		// If we are trying to resolve 'this', it makes no sense to try to find 'this.this'
		if (name.equals(ClassDeclaration.VAR_THIS)) return null;

		// Is variable 'this' defined? If so, it means we are within a class
		TypeClass typeThis = (TypeClass) getVariableTypeLocal(ClassDeclaration.VAR_THIS);
		if (typeThis == null) return null;

		return typeThis.resolve(name);
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

		// Show scope symbols
		StringBuilder sbThis = new StringBuilder();
		List<String> names = new ArrayList<>();
		names.addAll(variableTypes.keySet());
		Collections.sort(names);
		for (String name : names)
			sbThis.append(variableTypes.get(name) + " " + name + "\n");

		// Show scope functions
		if (showFunc && functions != null) {
			for (String fname : functions.keySet())
				for (ValueFunction vf : functions.get(fname))
					sbThis.append(vf.getType().getReturnType() + " " + fname + "(" + vf.getParameters() + ")" + "\n");
		}

		// Show header
		sb.append("\n---------- SymbolTable " + getName() + "  ----------\n" + sbThis.toString());

		// Show parent table
		SymbolTable parent = getParent();
		if (parent != null) sb.append(parent.toString(showFunc));

		return sb.toString();
	}
}
