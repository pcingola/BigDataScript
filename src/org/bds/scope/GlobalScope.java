package org.bds.scope;

import java.io.File;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.util.Map;

import org.bds.Config;
import org.bds.executioner.ExecutionerCloud;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueList;
import org.bds.symbol.GlobalSymbolTable;
import org.bds.util.Gpr;

public class GlobalScope extends Scope {

	private static final long serialVersionUID = 2390988552900770372L;

	// Global variable names: Units and constants
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

	// Global variable names: Command line arguments are available in this list
	public static final String GLOBAL_VAR_ARGS_LIST = "args";

	// Global variable names: Program name, path, pid
	public static final String GLOBAL_VAR_PROGRAM_NAME = "programName";
	public static final String GLOBAL_VAR_PROGRAM_PATH = "programPath";
	public static final String GLOBAL_VAR_PROGRAM_PID = "programPid";

	// Global variable names: Task options
	public static final String GLOBAL_VAR_TASK_OPTION_ALLOW_EMPTY = "allowEmpty";
	public static final String GLOBAL_VAR_TASK_OPTION_CAN_FAIL = "canFail";
	public static final String GLOBAL_VAR_TASK_OPTION_CPUS = "cpus";
	public static final String GLOBAL_VAR_TASK_OPTION_DETACHED = "detached";
	public static final String GLOBAL_VAR_TASK_OPTION_MEM = "mem";
	public static final String GLOBAL_VAR_TASK_OPTION_NODE = "node";
	public static final String GLOBAL_VAR_TASK_OPTION_PHYSICAL_PATH = "ppwd";
	public static final String GLOBAL_VAR_TASK_OPTION_QUEUE = "queue";
	public static final String GLOBAL_VAR_TASK_OPTION_RETRY = "retry";
	public static final String GLOBAL_VAR_TASK_OPTION_SYSTEM = "system";
	public static final String GLOBAL_VAR_TASK_OPTION_TASKNAME = "taskName";
	public static final String GLOBAL_VAR_TASK_OPTION_TIMEOUT = "timeout";
	public static final String GLOBAL_VAR_TASK_OPTION_WALL_TIMEOUT = "walltimeout";
	public static final String GLOBAL_VAR_TASK_OPTION_RESOURCES = "taskResources";

	// Global variable names: Cloud AWS
	public static final String GLOBAL_VAR_AWS_REGION = "awsRegion";
	public static final String GLOBAL_VAR_EXECUTIONER_QUEUE_NAME_PREFIX = "cloudQueueNamePrefix";

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

	@Override
	public synchronized void add(String name, Object val) {
		super.add(name, val);
		addToGlobalSymbolTable(name);
	}

	@Override
	public synchronized void add(String name, Value value) {
		values.put(name, value);
		addToGlobalSymbolTable(name);
	}

	/**
	 * Add value and mark it as constant in global symbol table
	 */
	void addConstant(String name, Object val) {
		// Add value
		add(name, val);

		// Add type to global SymbolTable and flag it as 'constant'
		GlobalSymbolTable.get().setConstant(name);
	}

	public void addToGlobalSymbolTable(String name) {
		// Add type to global SymbolTable
		Value value = getValue(name);
		GlobalSymbolTable.get().addVariable(name, value.getType());
	}

	@Override
	public Scope getParent() {
		return null;
	}

	@Override
	public String getScopeName() {
		return "Global";
	}

	public void init(Config config) {
		// Add global symbols
		add(GLOBAL_VAR_PROGRAM_NAME, ""); // Now is empty, but they are assigned later
		add(GLOBAL_VAR_PROGRAM_PATH, "");
		add(GLOBAL_VAR_PROGRAM_PID, -1);

		ValueList vargs = new ValueList(TypeList.get(Types.STRING));
		add(GLOBAL_VAR_ARGS_LIST, vargs);

		// Number of CPUs in local computer
		long cpusLocal = Gpr.parseLongSafe(config.getString(GLOBAL_VAR_LOCAL_CPUS, "" + Gpr.NUM_CORES));
		add(GLOBAL_VAR_LOCAL_CPUS, cpusLocal);

		// Task CPUs
		String cpusStr = config.getString(GLOBAL_VAR_TASK_OPTION_CPUS, "1"); // Default number of cpus: 1
		long cpus = Gpr.parseIntSafe(cpusStr);
		if (cpus <= 0) throw new RuntimeException("Number of cpus must be a positive number ('" + cpusStr + "')");

		// Task memory
		long mem = Gpr.parseMemSafe(config.getString(GLOBAL_VAR_TASK_OPTION_MEM, "-1")); // Default amount of memory: -1 (unrestricted)
		String node = config.getString(GLOBAL_VAR_TASK_OPTION_NODE, "");

		// Task wall time and timeout
		long oneDay = 1L * 24 * 60 * 60;
		long timeout = Gpr.parseLongSafe(config.getString(GLOBAL_VAR_TASK_OPTION_TIMEOUT, "" + oneDay));
		long wallTimeout = Gpr.parseLongSafe(config.getString(GLOBAL_VAR_TASK_OPTION_WALL_TIMEOUT, "" + oneDay));

		// Task related variables: Default values
		add(GLOBAL_VAR_TASK_OPTION_ALLOW_EMPTY, false); // Tasks are allowed to have empty output file/s
		add(GLOBAL_VAR_TASK_OPTION_CAN_FAIL, false); // Task fail triggers checkpoint & exit (a task cannot fail)
		add(GLOBAL_VAR_TASK_OPTION_CPUS, cpus); // Default number of cpus
		add(GLOBAL_VAR_TASK_OPTION_DETACHED, false); // Tasks are running detached
		add(GLOBAL_VAR_TASK_OPTION_MEM, mem); // Default amount of memory (unrestricted)
		add(GLOBAL_VAR_TASK_OPTION_NODE, node); // Default node: none
		add(GLOBAL_VAR_TASK_OPTION_TIMEOUT, timeout); // Task default timeout
		add(GLOBAL_VAR_TASK_OPTION_WALL_TIMEOUT, wallTimeout); // Task default wall-timeout

		// Cloud AWS
		add(GLOBAL_VAR_EXECUTIONER_QUEUE_NAME_PREFIX, ExecutionerCloud.EXECUTIONER_QUEUE_NAME_PREFIX_DEFAULT); // Default prefix for cloud queue names
		add(GLOBAL_VAR_AWS_REGION, ""); // Default AWS region. Empty means that it would use default from AWS SDK (typically "$HOME/.aws/config" and "$HOME/.aws/credentials"
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

	/**
	 * Add symbols to global scope
	 */
	public void initilaize(Config config) {
		// Initialize config-based global variables
		init(config);

		// Add global symbols
		// Get default values from command line or config file
		add(GLOBAL_VAR_TASK_OPTION_SYSTEM, config.getSystem()); // System type: "local", "ssh", "cluster", "aws", etc.
		add(GLOBAL_VAR_TASK_OPTION_QUEUE, config.getQueue()); // Default queue: none
		add(GLOBAL_VAR_TASK_OPTION_RETRY, (long) config.getTaskFailCount()); // Task fail can be re-tried (re-run) N times before considering failed.

		// Set "physical" path
		String path;
		try {
			path = new File(".").getCanonicalPath();
		} catch (IOException e) {
			throw new RuntimeException("Cannot get cannonical path for current dir");
		}
		globalScope.add(GLOBAL_VAR_TASK_OPTION_PHYSICAL_PATH, path);

		// Set all environment variables
		Map<String, String> envMap = System.getenv();
		for (String varName : envMap.keySet()) {
			String varVal = envMap.get(varName);
			globalScope.add(varName, varVal);
		}

		// Command line arguments (default: empty list)
		// This is properly set in 'initializeArgs()' method, but
		// we have to set something now, otherwise we'll get a "variable
		// not found" error at compiler time, if the program attempts
		// to use 'args'.
		globalScope.add(GlobalScope.GLOBAL_VAR_ARGS_LIST, TypeList.get(Types.STRING));
	}

	/**
	 * Resolve un-serialization
	 */
	private Object readResolve() throws ObjectStreamException {
		globalScope = this;
		return this;
	}

}
