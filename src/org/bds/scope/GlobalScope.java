package org.bds.scope;

import java.util.LinkedList;

import org.bds.lang.expression.ExpressionTask;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.lang.value.ValueString;
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

	void init() {
		// Add global symbols
		globalScope.add(new ScopeSymbol(GLOBAL_VAR_PROGRAM_NAME, Types.STRING, "")); // Now is empty, but they are assigned later
		globalScope.add(new ScopeSymbol(GLOBAL_VAR_PROGRAM_PATH, new ValueString()));

		// Task related variables: Default values
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_SYSTEM, Types.STRING, system)); // System type: "local", "ssh", "cluster", "aws", etc.
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_CPUS, Types.INT, cpus)); // Default number of cpus
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_MEM, Types.INT, mem)); // Default amount of memory (unrestricted)
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_QUEUE, Types.STRING, queue)); // Default queue: none
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_NODE, Types.STRING, node)); // Default node: none
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_CAN_FAIL, Types.BOOL, false)); // Task fail triggers checkpoint & exit (a task cannot fail)
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_ALLOW_EMPTY, Types.BOOL, false)); // Tasks are allowed to have empty output file/s
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_RETRY, Types.INT, (long) taskFailCount)); // Task fail can be re-tried (re-run) N times before considering failed.
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_TIMEOUT, Types.INT, timeout)); // Task default timeout
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_WALL_TIMEOUT, Types.INT, wallTimeout)); // Task default wall-timeout
		globalScope.add(new ScopeSymbol(Scope.GLOBAL_VAR_LOCAL_CPUS, Types.INT, cpusLocal));

		// Number of local CPUs
		// Kilo, Mega, Giga, Tera, Peta.
		LinkedList<ScopeSymbol> constants = new LinkedList<>();
		constants.add(new ScopeSymbol(Scope.GLOBAL_VAR_K, Types.INT, 1024L));
		constants.add(new ScopeSymbol(Scope.GLOBAL_VAR_M, Types.INT, 1024L * 1024L));
		constants.add(new ScopeSymbol(Scope.GLOBAL_VAR_G, Types.INT, 1024L * 1024L * 1024L));
		constants.add(new ScopeSymbol(Scope.GLOBAL_VAR_T, Types.INT, 1024L * 1024L * 1024L * 1024L));
		constants.add(new ScopeSymbol(Scope.GLOBAL_VAR_P, Types.INT, 1024L * 1024L * 1024L * 1024L * 1024L));
		constants.add(new ScopeSymbol(Scope.GLOBAL_VAR_MINUTE, Types.INT, 60L));
		constants.add(new ScopeSymbol(Scope.GLOBAL_VAR_HOUR, Types.INT, (long) (60 * 60)));
		constants.add(new ScopeSymbol(Scope.GLOBAL_VAR_DAY, Types.INT, (long) (24 * 60 * 60)));
		constants.add(new ScopeSymbol(Scope.GLOBAL_VAR_WEEK, Types.INT, (long) (7 * 24 * 60 * 60)));

		// Math constants
		constants.add(new ScopeSymbol(Scope.GLOBAL_VAR_E, Types.REAL, Math.E));
		constants.add(new ScopeSymbol(Scope.GLOBAL_VAR_PI, Types.REAL, Math.PI));
	}

}
