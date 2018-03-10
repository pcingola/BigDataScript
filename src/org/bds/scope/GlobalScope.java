package org.bds.scope;

import org.bds.Config;
import org.bds.lang.expression.ExpressionTask;
import org.bds.lang.value.Value;
import org.bds.symbol.SymbolTable;
import org.bds.util.Gpr;

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

	public static GlobalScope get() {
		if (globalScope == null) reset();
		return globalScope;
	}

	/**
	 * Reset Global Scope
	 */
	public static void reset() {
		globalScope = new GlobalScope();
		globalScope.initConstants();
	}

	public static void set(GlobalScope newGlobalScope) {
		globalScope = newGlobalScope;
	}

	private GlobalScope() {
		super(null, null);
	}

	/**
	 * Add value and mark it as constant in global symbol table
	 */
	void addConstant(String name, Object val) {
		// Add value
		add(name, val);

		// Add scope symbol and flag it as 'constant'
		Value value = getValue(name);
		SymbolTable.get().add(name, value.getType());
		SymbolTable.get().setConstant(name);
	}

	public void init(Config config) {
		// Add global symbols
		add(GLOBAL_VAR_PROGRAM_NAME, ""); // Now is empty, but they are assigned later
		add(GLOBAL_VAR_PROGRAM_PATH, "");

		// CPUS
		long cpusLocal = Gpr.parseLongSafe(config.getString(GLOBAL_VAR_LOCAL_CPUS, "" + Gpr.NUM_CORES));
		add(GLOBAL_VAR_LOCAL_CPUS, cpusLocal);

		String cpusStr = config.getString(ExpressionTask.TASK_OPTION_CPUS, "1"); // Default number of cpus: 1
		long cpus = Gpr.parseIntSafe(cpusStr);
		if (cpus <= 0) throw new RuntimeException("Number of cpus must be a positive number ('" + cpusStr + "')");

		long mem = Gpr.parseMemSafe(config.getString(ExpressionTask.TASK_OPTION_MEM, "-1")); // Default amount of memory: -1 (unrestricted)
		String node = config.getString(ExpressionTask.TASK_OPTION_NODE, "");

		long oneDay = 1L * 24 * 60 * 60;
		long timeout = Gpr.parseLongSafe(config.getString(ExpressionTask.TASK_OPTION_TIMEOUT, "" + oneDay));
		long wallTimeout = Gpr.parseLongSafe(config.getString(ExpressionTask.TASK_OPTION_WALL_TIMEOUT, "" + oneDay));

		// Task related variables: Default values
		add(ExpressionTask.TASK_OPTION_CPUS, cpus); // Default number of cpus
		add(ExpressionTask.TASK_OPTION_MEM, mem); // Default amount of memory (unrestricted)
		add(ExpressionTask.TASK_OPTION_NODE, node); // Default node: none
		add(ExpressionTask.TASK_OPTION_CAN_FAIL, false); // Task fail triggers checkpoint & exit (a task cannot fail)
		add(ExpressionTask.TASK_OPTION_ALLOW_EMPTY, false); // Tasks are allowed to have empty output file/s
		add(ExpressionTask.TASK_OPTION_TIMEOUT, timeout); // Task default timeout
		add(ExpressionTask.TASK_OPTION_WALL_TIMEOUT, wallTimeout); // Task default wall-timeout
	}

	/**
	 * Initialize constants
	 */
	protected void initConstants() {
		// Number of local CPUs
		// Kilo, Mega, Giga, Tera, Peta.
		addConstant(GLOBAL_VAR_K, 1024L);
		addConstant(GLOBAL_VAR_M, 1024L * 1024L);
		addConstant(GLOBAL_VAR_G, 1024L * 1024L * 1024L);
		addConstant(GLOBAL_VAR_T, 1024L * 1024L * 1024L * 1024L);
		addConstant(GLOBAL_VAR_P, 1024L * 1024L * 1024L * 1024L * 1024L);
		addConstant(GLOBAL_VAR_MINUTE, 60L);
		addConstant(GLOBAL_VAR_HOUR, (long) (60 * 60));
		addConstant(GLOBAL_VAR_DAY, (long) (24 * 60 * 60));
		addConstant(GLOBAL_VAR_WEEK, (long) (7 * 24 * 60 * 60));

		// Math constants
		addConstant(GLOBAL_VAR_E, Math.E);
		addConstant(GLOBAL_VAR_PI, Math.PI);
	}
}
