package org.bds.symbol;

import java.io.ObjectStreamException;
import java.util.HashMap;
import java.util.Map;

import org.bds.lang.type.Type;

/**
 * Global SymboTable: A table of variables, functions and classes
 *
 * @author pcingola
 */
public class GlobalSymbolTable extends SymbolTable {

	private static final long serialVersionUID = 7118398522311869258L;

	private static GlobalSymbolTable globalSymbolTable = new GlobalSymbolTable();

	Map<String, Type> types; // Type definitions are all GlobalSymbolTable (stored using canonical name)

	public static GlobalSymbolTable get() {
		if (globalSymbolTable == null) reset();
		return globalSymbolTable;
	}

	public static void reset() {
		globalSymbolTable = new GlobalSymbolTable();
	}

	private GlobalSymbolTable() {
		super(null);
		types = new HashMap<>();
	}

	/**
	 * Add a type definition
	 * @param type
	 */
	@Override
	public void addType(Type type) {
		types.put(type.getCanonicalName(), type);
	}

	@Override
	protected String getName() {
		return "Global";
	}

	@Override
	public SymbolTable getParent() {
		return null; // GlobalSYmbolTable has no parent
	}

	/**
	 * Get type definition
	 */
	@Override
	public Type getType(String typeCanonicalName) {
		return types.get(typeCanonicalName);
	}

	/**
	 * Resolve un-serialization
	 */
	private Object readResolve() throws ObjectStreamException {
		globalSymbolTable = this;
		return this;
	}

}
