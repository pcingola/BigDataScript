package ca.mcgill.mcb.pcingola.bigDataScript;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import ca.mcgill.mcb.pcingola.bigDataScript.executioner.MonitorTask;
import ca.mcgill.mcb.pcingola.bigDataScript.executioner.TaskLogger;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Tail;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * Config file
 *
 * @author pcingola
 */
public class Config {

	public static final String DEFAULT_CONFIG_DIR = Gpr.HOME + "/.bds";
	public static final String DEFAULT_CONFIG_FILE = DEFAULT_CONFIG_DIR + "/bds.config";
	public static final String DEFAULT_INCLUDE_DIR = DEFAULT_CONFIG_DIR + "/include";

	public static final String BDS_INCLUDE_PATH = "BDS_PATH"; // BDS include path (colon separated list of directories to look for include files)
	public static final String PID_REGEX = "pidRegex"; // Regex used for PID
	public static final String PID_REGEX_CHECK_TASK_RUNNING = "pidRegexCheckTaskRunning"; // Regex used for checking PID
	public static final String PID_COLUMN_CHECK_TASK_RUNNING = "pidColumnCheckTaskRunning"; // Regex used for checking PID

	public static final String TASK_SHELL = "taskShell"; // Task's shell
	public static final String SYS_SHELL = "sysShell"; // Sys's shell

	// SGE parameters
	public static final String CLUSTER_SGE_PE = "sge.pe";
	public static final String CLUSTER_SGE_MEM = "sge.mem";
	public static final String CLUSTER_SGE_TIMEOUT = "sge.timeout";

	public static final String CLUSTER_RUN_ADDITIONAL_ARGUMENTS = "clusterRunAdditionalArgs"; // Cluster additional command line arguments (when running tasks)
	public static final String CLUSTER_KILL_ADDITIONAL_ARGUMENTS = "clusterKillAdditionalArgs"; // Cluster additional command line arguments (when killing tasks)
	public static final String CLUSTER_STAT_ADDITIONAL_ARGUMENTS = "clusterStatAdditionalArgs"; // Cluster additional command line arguments (when requesting information about all tasks)
	public static final String CLUSTER_POSTMORTEMINFO_ADDITIONAL_ARGUMENTS = "clusterPostMortemInfoAdditionalArgs"; // Cluster additional command line arguments (when requesting information about a failed task)

	// Generic cluster
	public static final String CLUSTER_GENERIC_RUN = "clusterGenericRun";
	public static final String CLUSTER_GENERIC_KILL = "clusterGenericKill";
	public static final String CLUSTER_GENERIC_STAT = "clusterGenericStat";
	public static final String CLUSTER_GENERIC_POSTMORTEMINFO = "clusterGenericPostMortemInfo";

	public static final String MAX_NUMBER_OF_RUNNING_THREADS = "maxThreads";
	public static int DEFAULT_MAX_NUMBER_OF_RUNNING_THREADS = 512;

	public static final String WAIT_AFTER_TASK_RUN = "waitAfterTaskRun";
	public static int DEFAULT_WAIT_AFTER_TASK_RUN = 0;

	private static Config configInstance = null; // Config is some kind of singleton because we want to make it accessible from everywhere

	public static final String[] EMPTY_STRING_ARRAY = new String[0];

	boolean debug = false; // Debug mode?
	boolean verbose = false; // Verbose mode?
	boolean log = false; // Log all commands?
	boolean dryRun = false; // Is this a dry run? (i.e. don't run commands, just show what they do).
	boolean noRmOnExit = false; // Avoid removing files on exit
	boolean createReport = true; // Create report when thread finishes
	boolean extractSource = false;
	int taskFailCount = 0; // Number of times a task is allowed to fail (i.e. number of re-tries)
	int maxThreads = -1; // Maximum number of simultaneous threads (e.g. when running 'qsub' commands)
	int waitAfterTaskRun = -1; // Wait some milisecs after task run
	String configFileName;
	String configDirName;
	String pidFile;
	Properties properties;
	ArrayList<String> includePath;
	TaskLogger taskLogger;
	MonitorTask monitorTask;
	Tail tail;

	public static Config get() {
		if (configInstance == null) configInstance = new Config();
		return configInstance;
	}

	public Config() {
		read(DEFAULT_CONFIG_FILE); // Read config file
		configInstance = this;
	}

	/**
	 * Create a configuration from 'configFileName'
	 */
	public Config(String configFileName) {
		read(configFileName); // Read config file
		configInstance = this;
	}

	public String getConfigDirName() {
		return configDirName;
	}

	public String getConfigFileName() {
		return configFileName;
	}

	/**
	 * Get a property as a double
	 */
	protected double getDouble(String propertyName, double defaultValue) {
		String val = getString(propertyName);
		if (val == null) return defaultValue;
		return Gpr.parseDoubleSafe(val);
	}

	/**
	 * A collection of strings showing where to search for include files
	 *
	 * TODO: Add path from config file
	 * TODO: Add default system-wide include path ("/usr/local/bds/include")
	 *
	 * @return
	 */
	public Collection<String> getIncludePath() {
		// Create array if needed
		if (includePath == null) {
			includePath = new ArrayList<String>();

			// Add by search order
			includePath.add("."); // Current dir (obviously)
			includePath.add(DEFAULT_INCLUDE_DIR); // Default include path ($HOME/.bds/include)

			// Add BDS_PATH environment variable
			String bdsPath = System.getenv(BDS_INCLUDE_PATH);
			if ((bdsPath != null) && !bdsPath.isEmpty()) {
				String incPaths[] = bdsPath.split(":");
				for (String incPath : incPaths) {
					if (!incPath.isEmpty()) includePath.add(incPath);
				}
			}
		}

		return includePath;
	}

	/**
	 * Get a property as a long
	 */
	public long getLong(String propertyName, long defaultValue) {
		String val = getString(propertyName);
		if (val == null) return defaultValue;
		return Gpr.parseLongSafe(val);
	}

	/**
	 * Max number of concurrent threads
	 */
	public int getMaxThreads() {
		if (maxThreads <= 0) {
			// Parse property
			maxThreads = (int) getLong(MAX_NUMBER_OF_RUNNING_THREADS, DEFAULT_MAX_NUMBER_OF_RUNNING_THREADS);
			if (debug) Timer.showStdErr("Config: Setting 'maxThreads' to " + maxThreads);
		}

		return maxThreads;
	}

	public MonitorTask getMonitorTask() {
		if (monitorTask == null) {
			monitorTask = new MonitorTask();
			monitorTask.setDebug(isDebug());
			monitorTask.setVerbose(isVerbose());
		}
		return monitorTask;
	}

	public String getPidFile() {
		return pidFile;
	}

	public String getPidRegex(String defaultPidRegex) {
		String pidRegex = getString(PID_REGEX, defaultPidRegex).trim();

		// Remove leading and trailing quotes
		if (pidRegex.startsWith("\"") && pidRegex.endsWith("\"") && pidRegex.length() > 2) {
			pidRegex = pidRegex.substring(1, pidRegex.length() - 1);
		}

		return pidRegex;
	}

	public String getPidRegexCheckTasksRunning(String defaultPidRegex) {
		String pidRegex = getString(PID_REGEX_CHECK_TASK_RUNNING, defaultPidRegex).trim();

		// Remove leading and trailing quotes
		if (pidRegex.startsWith("\"") && pidRegex.endsWith("\"") && pidRegex.length() > 2) {
			pidRegex = pidRegex.substring(1, pidRegex.length() - 1);
		}

		return pidRegex;
	}

	/**
	 * Get a property as a string
	 */
	protected String getString(String propertyName) {
		return properties.getProperty(propertyName);
	}

	public String getString(String propertyName, String defaultValue) {
		String val = getString(propertyName);
		return val != null ? val.trim() : defaultValue;
	}

	public String[] getStringArray(String propertyName) {
		return getStringArray(propertyName, false);
	}

	/**
	 * Get a configuration value and split is into an array (using regex '\\s+')
	 */
	public String[] getStringArray(String propertyName, boolean required) {
		String val = getString(propertyName);
		if (val == null) {
			if (required) throw new RuntimeException("Cannot find configuration parameter '" + propertyName + "'" + (configFileName != null ? " in config file '" + configFileName + "'" : ""));
			return EMPTY_STRING_ARRAY;
		}

		// Parse and add to list
		ArrayList<String> vals = new ArrayList<String>();
		for (String v : val.split("\\s+")) {
			v = v.trim();
			if (!v.isEmpty()) vals.add(v.trim());
		}

		// Convert to string array
		String valsArray[] = vals.toArray(EMPTY_STRING_ARRAY);

		// Show
		if (isDebug()) {
			Timer.showStdErr("Config: Parsing '" + propertyName + "'. Number of additional arguments: " + valsArray.length);
			for (int i = 0; i < valsArray.length; i++) {
				System.err.println("\t\t\t" + i + "\t'" + valsArray[i] + "'");
			}
		}

		return valsArray;
	}

	public Tail getTail() {
		if (tail == null) {
			tail = new Tail();
			tail.setDebug(isDebug());
			tail.setVerbose(isVerbose());
			tail.start(); // Create a 'tail' process (to show STDOUT & STDERR from all processes)
		}
		return tail;
	}

	public int getTaskFailCount() {
		return taskFailCount;
	}

	public TaskLogger getTaskLogger() {
		if (taskLogger == null) {
			taskLogger = new TaskLogger(getPidFile());
			taskLogger.setDebug(isDebug());
		}
		return taskLogger;
	}

	public int getWaitAfterTaskRun() {
		if (waitAfterTaskRun <= 0) {
			// Parse property
			maxThreads = (int) getLong(WAIT_AFTER_TASK_RUN, DEFAULT_WAIT_AFTER_TASK_RUN);
			if (debug) Timer.showStdErr("Config: Setting 'waitAfterTaskRun' to " + waitAfterTaskRun);
		}

		return waitAfterTaskRun;
	}

	public boolean isCreateReport() {
		return createReport;
	}

	public boolean isDebug() {
		return debug;
	}

	public boolean isDryRun() {
		return dryRun;
	}

	public boolean isExtractSource() {
		return extractSource;
	}

	public boolean isLog() {
		return log;
	}

	public boolean isNoRmOnExit() {
		return noRmOnExit;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void kill() {
		if (tail != null) {
			tail.kill(); // Kill tail process
			tail = null;
		}

		if (monitorTask != null) {
			// monitorTask.kill();
			monitorTask = null;
		}
	}

	/**
	 * Read configuration file
	 */
	private void read(String configFileName) {
		this.configFileName = configFileName;
		properties = new Properties();

		if (!Gpr.exists(configFileName)) {
			if (verbose) Timer.showStdErr("Config file '" + configFileName + "' not found");
			return;
		}

		//---
		// Read properties file
		//---
		try {
			properties.load(new FileReader(new File(configFileName)));
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Cannot find config file '" + configFileName + "'");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		// Config directory
		configDirName = "";
		try {
			File configDir = new File(configFileName).getAbsoluteFile().getParentFile();
			configDirName = configDir.getCanonicalPath();
		} catch (IOException e1) {
			// OK: May be there is no config file.
		}
	}

	public void set(String propertyName, String value) {
		properties.setProperty(propertyName, value);
	}

	public void setCreateReport(boolean createReport) {
		this.createReport = createReport;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setDryRun(boolean dryRun) {
		this.dryRun = dryRun;
	}

	public void setExtractSource(boolean extractSource) {
		this.extractSource = extractSource;
	}

	public void setLog(boolean log) {
		this.log = log;
	}

	public void setNoRmOnExit(boolean noRmOnExit) {
		this.noRmOnExit = noRmOnExit;
	}

	public void setPidFile(String pidFile) {
		this.pidFile = pidFile;
	}

	public void setTaskFailCount(int taskFailCount) {
		this.taskFailCount = taskFailCount;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		return sb.toString();
	}
}
