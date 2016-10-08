package org.bds;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;

import org.bds.executioner.MonitorTask;
import org.bds.executioner.TaskLogger;
import org.bds.task.Tail;
import org.bds.task.TailFile;
import org.bds.task.Task;
import org.bds.util.Gpr;
import org.bds.util.Timer;

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

	// Amazon AWS parameters
	public static final String AWS_REGION = "awsRegion";

	// PID regular expressions
	public static final String PID_REGEX = "pidRegex"; // Regex used for PID
	public static final String PID_CHECK_TASK_RUNNING_REGEX = "pidRegexCheckTaskRunning"; // Regex used for checking PID
	public static final String PID_CHECK_TASK_RUNNING_COLUMN = "pidColumnCheckTaskRunning"; // Regex used for checking PID

	// Shells used to invoke 'sys' and 'task'
	public static final String TASK_SHELL = "taskShell"; // Task's shell
	public static final String TASK_SHELL_DEFAULT = "/bin/bash -e"; // Use '-e' so that shell script stops after first error

	public static final String SYS_SHELL = "sysShell"; // Sys's shell
	public static String SYS_SHELL_DEFAULT = "/bin/bash -e -c"; // Note: This executes a script, so it requires the "-c" right before script name

	// Temporary directory
	public static final String TMP_DIR = "tmpDir";
	public static final String DEFAULT_TMP_DIR = "/tmp";

	// Disable checkpoint creation
	public static final String DISABLE_CHECKPOINT_CREATE = "disableCheckpoint";
	public static final String DISABLE_RM_ON_EXIT = "disableRmOnExit";
	public static final String TAIL_LINES = "tailLines"; // Number of lie to use in 'tail'
	public static final String FILTER_OUT_TASK_HINT = "filterOutTaskHint"; // Lines to filter out from task hint
	public static final String SHOW_TASK_CODE = "showTaskCode"; // Always show task's code (sys commands)

	// SGE parameters
	public static final String CLUSTER_SGE_PE = "sge.pe";
	public static final String CLUSTER_SGE_MEM = "sge.mem";
	public static final String CLUSTER_SGE_TIMEOUT = "sge.timeout";
	public static final String CLUSTER_SGE_TIMEOUT2 = "sge.timeout2";

	// Cluster parameters
	public static final String CLUSTER_RUN_ADDITIONAL_ARGUMENTS = "clusterRunAdditionalArgs"; // Cluster additional command line arguments (when running tasks)
	public static final String CLUSTER_KILL_ADDITIONAL_ARGUMENTS = "clusterKillAdditionalArgs"; // Cluster additional command line arguments (when killing tasks)
	public static final String CLUSTER_STAT_ADDITIONAL_ARGUMENTS = "clusterStatAdditionalArgs"; // Cluster additional command line arguments (when requesting information about all tasks)
	public static final String CLUSTER_POSTMORTEMINFO_ADDITIONAL_ARGUMENTS = "clusterPostMortemInfoAdditionalArgs"; // Cluster additional command line arguments (when requesting information about a failed task)
	public static final String CLUSTER_POSTMORTEMINFO_DISABLED = "clusterPostMortemDisabled"; // Some clusters do not provide information after the process dies

	// Generic cluster
	public static final String CLUSTER_GENERIC_RUN = "clusterGenericRun";
	public static final String CLUSTER_GENERIC_KILL = "clusterGenericKill";
	public static final String CLUSTER_GENERIC_STAT = "clusterGenericStat";
	public static final String CLUSTER_GENERIC_POSTMORTEMINFO = "clusterGenericPostMortemInfo";

	public static final String MAX_NUMBER_OF_RUNNING_THREADS = "maxThreads";
	public static final int MAX_NUMBER_OF_RUNNING_THREADS_MIN_VALUE = 50; // If maxThreads in configuration file is too small, we'll consider it an error and use this number
	public static final int DEFAULT_MAX_NUMBER_OF_RUNNING_THREADS = 512;

	public static final String WAIT_AFTER_TASK_RUN = "waitAfterTaskRun";
	public static int DEFAULT_WAIT_AFTER_TASK_RUN = 0;

	public static final String WAIT_TEXT_FILE_BUSY = "waitTextFileBusy";
	public static int DEFAULT_WAIT_TEXT_FILE_BUSY = 1;

	public static final String TASK_MAX_HINT_LEN = "taskMaxHintLen";

	private static Config configInstance = null; // Config is some kind of singleton because we want to make it accessible from everywhere

	public static final String[] EMPTY_STRING_ARRAY = new String[0];

	boolean debug = false; // Debug mode?
	boolean verbose = false; // Verbose mode?
	boolean quiet = false; // Quiet mode?
	boolean log = false; // Log all commands?
	boolean dryRun = false; // Is this a dry run? (i.e. don't run commands, just show what they do).
	boolean noCheckpoint; // Do not create checkpoint files
	boolean noRmOnExit; // Avoid removing files on exit
	boolean extractSource = false; // Extract source code from checkpoint file
	boolean reportYaml = false; // Use YAML report format
	boolean reportHtml = true; // Use HTML report format
	boolean showTaskCode; // Always show task's code (sys statements)
	int taskFailCount = 0; // Number of times a task is allowed to fail (i.e. number of re-tries)
	int maxThreads = -1; // Maximum number of simultaneous threads (e.g. when running 'qsub' commands)
	int waitAfterTaskRun = -1; // Wait some milisec after task run
	int waitTextFileBusy = -1; // Wait some milisecs after writing a shell file to disk (before execution)
	int tailLines; // Number of lines to use in 'tail'
	Integer taskMaxHintLen; // Max number of characters to use in tasks's "hint"
	String configFileName;
	String configDirName;
	String pidFile = "pidFile" + (new Date()).getTime() + ".txt"; // Default PID file
	String reportFileName; // Preferred file name to use for progress and final report
	Properties properties;
	ArrayList<String> includePath;
	ArrayList<String> filterOutTaskHint;
	TaskLogger taskLogger;
	MonitorTask monitorTask;
	Tail tail;

	/**
	 * Get singleton
	 */
	public static Config get() {
		if (configInstance == null) configInstance = new Config();
		return configInstance;
	}

	/**
	 * Reset singleton
	 */
	public static void reset() {
		configInstance = null;
	}

	public Config() {
		this(DEFAULT_CONFIG_FILE);
	}

	/**
	 * Create a configuration from 'configFileName'
	 */
	public Config(String configFileName) {
		read(configFileName); // Read config file
		parse(); // Parse values form properties
		configInstance = this;
	}

	/**
	 * Get a property as a boolean
	 */
	public boolean getBool(String propertyName, boolean defaultValue) {
		String val = getString(propertyName);
		if (val == null) return defaultValue;
		return Gpr.parseBoolSafe(val.trim());
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

	public ArrayList<String> getFilterOutTaskHint() {
		return filterOutTaskHint;
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
		return Gpr.parseLongSafe(val.trim());
	}

	/**
	 * Max number of concurrent threads
	 */
	public int getMaxThreads() {
		if (maxThreads <= 0) {
			// Parse property
			maxThreads = (int) getLong(MAX_NUMBER_OF_RUNNING_THREADS, DEFAULT_MAX_NUMBER_OF_RUNNING_THREADS);
			if (debug) Timer.showStdErr("Config: Setting 'maxThreads' to " + maxThreads);

			if (maxThreads < MAX_NUMBER_OF_RUNNING_THREADS_MIN_VALUE) {
				Timer.showStdErr("Config: Attempt to set 'maxThreads' to " + maxThreads + ". Too small, using " + MAX_NUMBER_OF_RUNNING_THREADS_MIN_VALUE + " inseatd.");
				maxThreads = MAX_NUMBER_OF_RUNNING_THREADS_MIN_VALUE;
			}
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
		String pidRegex = getString(PID_CHECK_TASK_RUNNING_REGEX, defaultPidRegex).trim();

		// Remove leading and trailing quotes
		if (pidRegex.startsWith("\"") && pidRegex.endsWith("\"") && pidRegex.length() > 2) {
			pidRegex = pidRegex.substring(1, pidRegex.length() - 1);
		}

		return pidRegex;
	}

	public String getReportFileName() {
		return reportFileName;
	}

	/**
	 * Get a property as a string
	 */
	protected String getString(String propertyName) {
		return properties.getProperty(propertyName);
	}

	public String getString(String propertyName, String defaultValue) {
		String val = getString(propertyName);
		if (val == null) return defaultValue;
		val = val.trim();
		if (val.startsWith("\"") && val.endsWith("\"")) val = val.substring(1, val.length() - 1);
		return val;
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

	public String getSysShell() {
		return getString(Config.SYS_SHELL, Config.SYS_SHELL_DEFAULT);
	}

	public Tail getTail() {
		if (tail == null) {
			tail = new Tail();
			tail.setDebug(isDebug());
			tail.setVerbose(isVerbose());
			tail.setQuiet(isQuiet());
			tail.start(); // Create a 'tail' process (to show STDOUT & STDERR from all processes)
		}
		return tail;
	}

	public int getTailLines() {
		return tailLines;
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

	public int getTaskMaxHintLen() {
		if (taskMaxHintLen == null) {
			taskMaxHintLen = Gpr.parseIntSafe(properties.getProperty(TASK_MAX_HINT_LEN, Task.MAX_HINT_LEN + ""));

			// Negative number means 'unlimited' (technically 2G)
			if (taskMaxHintLen < 0) taskMaxHintLen = Integer.MAX_VALUE;
		}

		return taskMaxHintLen;
	}

	public String getTaskShell() {
		return getString(Config.TASK_SHELL, Config.TASK_SHELL_DEFAULT);
	}

	public String getTmpDir() {
		return getString(TMP_DIR, DEFAULT_TMP_DIR);
	}

	public int getWaitAfterTaskRun() {
		if (waitAfterTaskRun < 0) {
			// Parse property
			waitAfterTaskRun = (int) getLong(WAIT_AFTER_TASK_RUN, DEFAULT_WAIT_AFTER_TASK_RUN);
			if (debug) Timer.showStdErr("Config: Setting 'waitAfterTaskRun' to " + waitAfterTaskRun);
		}

		return waitAfterTaskRun;
	}

	public int getWaitTextFileBusy() {
		if (waitTextFileBusy < 0) {
			// Parse property
			waitTextFileBusy = (int) getLong(WAIT_TEXT_FILE_BUSY, DEFAULT_WAIT_TEXT_FILE_BUSY);
			if (debug) Timer.showStdErr("Config: Setting 'waitTextFileBusy' to " + waitTextFileBusy);
		}

		return waitTextFileBusy;
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

	public boolean isNoCheckpoint() {
		return noCheckpoint;
	}

	public boolean isNoRmOnExit() {
		return noRmOnExit;
	}

	public boolean isQuiet() {
		return quiet;
	}

	public boolean isReportHtml() {
		return reportHtml;
	}

	public boolean isReportYaml() {
		return reportYaml;
	}

	public boolean isShowTaskCode() {
		return showTaskCode;
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
	 * Parse some values
	 */
	void parse() {
		noCheckpoint = getBool(DISABLE_CHECKPOINT_CREATE, false);
		noRmOnExit = getBool(DISABLE_RM_ON_EXIT, false);
		showTaskCode = getBool(SHOW_TASK_CODE, false);
		tailLines = (int) getLong(TAIL_LINES, TailFile.DEFAULT_TAIL);

		// Split and add all items
		filterOutTaskHint = new ArrayList<String>();
		for (String foth : getString(FILTER_OUT_TASK_HINT, "").split(" ")) {
			foth = foth.trim();
			if (!foth.isEmpty()) filterOutTaskHint.add(foth);
		}
	}

	/**
	 * Read configuration file
	 */
	private void read(String configFileName) {
		this.configFileName = configFileName;
		properties = new Properties();

		if (!Gpr.exists(configFileName)) {
			// The user specified a configuration file (different than default)
			if (!configFileName.equals(DEFAULT_CONFIG_FILE)) // => This should be a fatal error.
				throw new RuntimeException("Config file '" + configFileName + "' not found");

			// User did not specify a config file? => OK
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

	public void setNoCheckpoint(boolean noCheckpoint) {
		this.noCheckpoint = noCheckpoint;
	}

	public void setNoRmOnExit(boolean noRmOnExit) {
		this.noRmOnExit = noRmOnExit;
	}

	public void setPidFile(String pidFile) {
		this.pidFile = pidFile;
	}

	public void setQuiet(boolean quiet) {
		this.quiet = quiet;
	}

	public void setReportFileName(String reportFileName) {
		this.reportFileName = reportFileName;
	}

	public void setReportHtml(boolean reportHtml) {
		this.reportHtml = reportHtml;
	}

	public void setReportYaml(boolean yamlReport) {
		reportYaml = yamlReport;
	}

	public void setShowTaskCode(boolean showTaskCode) {
		this.showTaskCode = showTaskCode;
	}

	public void setTailLines(int tailLines) {
		this.tailLines = tailLines;
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

		sb.append("Config file '" + configFileName + "':\n");

		ArrayList<String> keys = new ArrayList<String>();
		for (Object key : properties.keySet())
			keys.add(key.toString());

		Collections.sort(keys);

		for (String key : keys)
			sb.append("\t'" + key + "' : '" + properties.get(key) + "'\n");

		return sb.toString();
	}
}
