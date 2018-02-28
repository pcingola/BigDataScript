package org.bds.scope;

import org.bds.lang.type.Type;
import org.bds.util.AutoHashMap;

public class GlobalScope extends Scope {

	public static final String GLOBAL_VAR_K = "K"; // Kilo = 2^10
	public static final String GLOBAL_VAR_M = "M"; // Mega = 2^20
	public static final String GLOBAL_VAR_G = "G"; // Giga = 2^30
	public static final String GLOBAL_VAR_T = "T"; // Tera = 2^40
	public static final String GLOBAL_VAR_P = "P"; // Peta = 2^50
	public static final String GLOBAL_VAR_E = "E"; // Euler's constant
	public static final String GLOBAL_VAR_PI = "PI"; // Pi
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
	private static GlobalScope globalScope = new GlobalScope();
	private static AutoHashMap<String, Scope> classScope = new AutoHashMap<>(new Scope());

	public static GlobalScope get() {
		return globalScope;
	}

	/**
	 * Class scope
	 */
	public static Scope getClassScope(Type type) {
		if (type == null) return null;
		return classScope.getOrCreate(type.toString());
	}

	/**
	 * Reset Global Scope
	 */
	public static void reset() {
		globalScope = new GlobalScope();
	}

	public static void set(GlobalScope newGlobalScope) {
		globalScope = newGlobalScope;
	}

	private GlobalScope() {
		super(null, null);
	}

}
