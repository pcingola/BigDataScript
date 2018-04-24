package org.bds.symbol;

import java.io.ObjectStreamException;

/**
 * Global SymboTable: A table of variables, functions and classes
 *
 * @author pcingola
 */
public class GlobalSymbolTable extends SymbolTable {

	private static final long serialVersionUID = 7118398522311869258L;

	private static GlobalSymbolTable globalSymbolTable = new GlobalSymbolTable();

	public static GlobalSymbolTable get() {
		if (globalSymbolTable == null) reset();
		return globalSymbolTable;
	}

	public static void reset() {
		globalSymbolTable = new GlobalSymbolTable();
	}

	private GlobalSymbolTable() {
		super(null);
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
	 * Resolve un-serialization
	 */
	private Object readResolve() throws ObjectStreamException {
		globalSymbolTable = this;
		return this;
	}

}
