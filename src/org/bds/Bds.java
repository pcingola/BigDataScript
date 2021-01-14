package org.bds;

import org.bds.data.Data;
import org.bds.run.BdsRun;
import org.bds.run.BdsRun.BdsAction;
import org.bds.util.Gpr;

/**
 * BDS command line
 *
 * @author pcingola
 */
public class Bds implements BdsLog {

	public static final String BUILD = Gpr.compileTimeStamp(Bds.class);
	public static final String REVISION = "b";
	public static final String SOFTWARE_NAME = Bds.class.getSimpleName();
	public static final String VERSION_MAJOR = "3.0";
	public static final String VERSION_SHORT = VERSION_MAJOR + REVISION;
	public static final String VERSION = SOFTWARE_NAME + " " + VERSION_SHORT + " (build " + BUILD + "), by " + Pcingola.BY;

	String args[];
	BdsRun bdsRun;
	String chekcpointRestoreFile; // Restore file
	Config config;
	String configFile = Config.DEFAULT_CONFIG_FILE; // Configuration file
	boolean coverage;
	double coverageMin = -1; // Min coverage ratio
	boolean debug; // debug mode
	boolean dryRun; // Dry run (do not run tasks)
	boolean extractSource; // Extract source code form checkpoint (only valid on recovery mode)
	boolean log; // Log everything (keep STDOUT, SDTERR and ExitCode files)
	Boolean noCheckpoint; // Do not create checkpoint files
	Boolean noRmOnExit; // Do not remove temp files on exit
	String pidFile; // File to store PIDs
	String programFileName; // Program file name
	String queue; // Queue name
	boolean quiet; // Quiet mode
	String reportFileName;
	Boolean reportHtml; // Use HTML report style
	Boolean reportYaml; // Use YAML report style
	String system; // System type
	int taskFailCount = -1;
	boolean verbose; // Verbose mode

	/**
	 * Main
	 */
	public static void main(String[] args) {
		// Create BigDataScript object and run it
		Bds bigDataScript = new Bds(args);
		int exitValue = bigDataScript.run();
		System.exit(exitValue);
	}

	public Bds(String args[]) {
		this.args = args;
		init();
	}

	/**
	 * Load configuration file
	 */
	protected Config config() {
		config = new Config(configFile);
		config.setDebug(debug);
		config.load();
		config.setCoverage(coverage);
		config.setExtractSource(extractSource);
		config.setLog(log);
		config.setDryRun(dryRun);
		config.setQuiet(quiet);
		config.setReportFileName(reportFileName);
		config.setTaskFailCount(taskFailCount);
		config.setVerbose(verbose);

		// Override config file by command line option
		if (noCheckpoint != null) config.setNoCheckpoint(noCheckpoint);
		if (noRmOnExit != null) config.setNoRmOnExit(noRmOnExit);
		if (queue != null) config.setQueue(queue);
		if (reportHtml != null) config.setReportHtml(reportHtml);
		if (reportYaml != null) config.setReportYaml(reportYaml);
		if (system != null) config.setSystem(system);
		if (taskFailCount > 0) config.setTaskFailCount(taskFailCount);

		if (pidFile == null) {
			if (programFileName != null) pidFile = programFileName + ".pid";
			else pidFile = chekcpointRestoreFile + ".pid";
		}
		config.setPidFile(pidFile);
		return config;
	}

	/**
	 * Download a URL to a local file
	 * @return true if successful
	 */
	public boolean download(String url, String fileName) {
		Data remote = Data.factory(url);
		Data local = Data.factory(fileName);

		// Sanity checks
		if (!remote.isRemote()) {
			error("Cannot download non-remote URL: " + url);
			return false;
		}

		if (!remote.isFile()) {
			error("Cannot download non-file: " + url);
			return false;
		}

		// Already downloaded? Nothing to do
		if (remote.isDownloaded(local)) {
			debug("Local file is up to date, no download required: " + fileName);
			return true;
		}

		return remote.download(local);
	}

	/**
	 * This is used in test cases only
	 */
	public BdsRun getBdsRun() {
		return bdsRun;
	}

	public Config getConfig() {
		return config;
	}

	/**
	 * Get default settings
	 */
	void init() {
		reportFileName = null;
		reportHtml = null;
		reportYaml = null;
		bdsRun = new BdsRun();
	}

	@Override
	public boolean isDebug() {
		return debug;
	}

	@Override
	public boolean isLog() {
		return log;
	}

	/**
	 * Is this a command line option (e.g. "-tfam" is a command line option, but "-" means STDIN)
	 */
	protected boolean isOpt(String arg) {
		return arg.startsWith("-") && (arg.length() > 1);
	}

	@Override
	public boolean isVerbose() {
		return verbose;
	}

	/**
	 * Parse command line arguments
	 */
	public void parse(String[] args) {
		// Nothing? Show command line options
		if (args.length <= 0) usage(null);

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];

			if (programFileName != null) {
				// Everything after 'programFileName' is an command line
				// argument for the BigDataScript program
				bdsRun.addArg(arg);
			} else if (isOpt(arg)) {
				// Note: Options sorted by 'long' option name
				switch (arg.toLowerCase()) {
				case "-asm":
					bdsRun.setBdsAction(BdsAction.ASSEMBLY);
					break;

				case "-checkpidregex":
					bdsRun.setBdsAction(BdsAction.CHECK_PID_REGEX);
					break;

				case "-compile":
					bdsRun.setBdsAction(BdsAction.COMPILE);
					break;

				case "-c":
				case "-config":
					// Checkpoint restore
					if ((i + 1) < args.length) configFile = args[++i];
					else usage("Option '-c' without restore file argument");
					break;

				case "-coverage":
					coverage = true;
					break;

				case "-coveragemin":
					coverageMin = Gpr.parseDoubleSafe(args[++i]);;
					break;

				case "-d":
				case "-debug":
					debug = verbose = true; // Debug implies verbose
					quiet = false;
					break;

				case "-download":
					if ((i + 2) < args.length) {
						config();
						boolean ok = download(args[++i], args[++i]);
						System.exit(ok ? 0 : 1);
					} else usage("Option '-download' requires two parameters (URL and file)");
					break;

				case "-dryrun":
					dryRun = true;
					noRmOnExit = true; // Not running, so don't delete files
					reportHtml = reportYaml = false; // Don't create reports
					break;

				case "-extractsource":
					extractSource = true;
					break;

				case "-h":
				case "-help":
				case "--help":
					usage(null);
					break;

				case "-i":
				case "-info":
					// Checkpoint info
					if ((i + 1) < args.length) chekcpointRestoreFile = args[++i];
					else usage("Option '-i' without checkpoint file argument");
					bdsRun.setBdsAction(BdsAction.INFO_CHECKPOINT);
					break;

				case "-l":
				case "-log":
					log = true;
					break;

				case "-nochp":
					noCheckpoint = true;
					break;

				case "-noreport":
					reportHtml = reportYaml = false;
					break;

				case "-noreporthtml":
					reportHtml = false;
					break;

				case "-noreportyaml":
					reportYaml = false;
					break;

				case "-normonexit":
					noRmOnExit = true;
					break;

				case "-pid":
					// PID file
					if ((i + 1) < args.length) pidFile = args[++i];
					else usage("Option '-pid' without file argument");
					break;

				case "-q":
				case "-queue":
					// Queue name
					if ((i + 1) < args.length) queue = args[++i];
					else usage("Option '-queue' without file argument");
					break;

				case "-quiet":
					verbose = false;
					debug = false;
					quiet = true;
					break;

				case "-r":
				case "-restore":
					// Checkpoint restore
					if ((i + 1) < args.length) chekcpointRestoreFile = args[++i];
					else usage("Option '-r' without checkpoint file argument");
					bdsRun.setBdsAction(BdsAction.RUN_CHECKPOINT);
					break;

				case "-reporthtml":
					reportHtml = true;
					break;

				case "-reportname":
					if ((i + 1) < args.length) reportFileName = args[++i];
					else usage("Option '-reportName' without name argument");
					break;

				case "-reportyaml":
				case "-yaml":
					reportYaml = true;
					break;

				case "-s":
				case "-system":
					// System type
					if ((i + 1) < args.length) system = args[++i];
					else usage("Option '-system' without file argument");
					break;

				case "-task":
					// Checkpoint restore
					if ((i + 1) < args.length) chekcpointRestoreFile = args[++i];
					else usage("Option '-task' without checkpoint file argument");
					bdsRun.setBdsAction(BdsAction.RUN_TASK_IMPROPER);
					reportHtml = reportYaml = false;
					break;

				case "-t":
				case "-test":
					bdsRun.setBdsAction(BdsAction.TEST);
					break;

				case "-upload":
					if ((i + 2) < args.length) {
						config();
						boolean ok = upload(args[++i], args[++i]);
						System.exit(ok ? 0 : 1);
					} else usage("Option '-upload' requires two parameters (file and URL)");
					break;

				case "-v":
				case "-verbose":
					quiet = false;
					verbose = true;
					break;

				case "-version":
					System.out.println(VERSION);
					System.exit(0);
					break;

				case "-wall":
					// Nothing to do yet
					break;

				case "-y":
				case "-retry":
					// Number of retries
					if ((i + 1) < args.length) taskFailCount = Gpr.parseIntSafe(args[++i]);
					else usage("Option '-t' without number argument");
					break;

				case "-zzz":
					bdsRun.setBdsAction(BdsAction.ZZZ);
					break;

				default:
					usage("Unknown command line option " + arg);
				}
			} else if (programFileName == null) programFileName = arg; // Get program file name

		}

		// Sanity checks
		if (bdsRun.getBdsAction() == BdsAction.CHECK_PID_REGEX) {
			// OK: Nothing to check
		} else if (bdsRun.getBdsAction() == BdsAction.ZZZ) {
			// OK: Nothing to check
		} else if ((programFileName == null) && (chekcpointRestoreFile == null)) {
			// No file name => Error
			usage("Missing program file name.");
		}
	}

	public int run() {
		parse(args);
		config();

		// Set running parameters
		bdsRun.setDebug(debug);
		bdsRun.setLog(log);
		bdsRun.setVerbose(verbose);
		bdsRun.setChekcpointRestoreFile(chekcpointRestoreFile);
		bdsRun.setProgramFileName(programFileName);
		bdsRun.setConfig(config);
		bdsRun.setCoverage(coverage);
		bdsRun.setCoverageMin(coverageMin);

		// Run and return program's exit code
		return bdsRun.run();
	}

	/**
	 * Upload a local file to a URL
	 * @return true if successful
	 */
	public boolean upload(String fileName, String url) {
		Data remote = Data.factory(url);
		Data local = Data.factory(fileName);

		// Sanity checks
		if (!remote.isRemote()) {
			error("Cannot upload to non-remote URL: " + url);
			return false;
		}

		if (!local.isFile()) {
			error("Cannot upload non-file: " + fileName);
			return false;
		}

		if (!local.exists()) {
			error("Local file does not exists: " + fileName);
			return false;
		}

		if (!local.canRead()) {
			error("Cannot read local file : " + fileName);
			return false;
		}

		// Already uploaded? Nothing to do
		if (remote.isUploaded(local)) {
			debug("Remote file is up to date, no upload required: " + url);
			return true;
		}

		return remote.upload(local);
	}

	void usage(String err) {
		if (err != null) System.err.println("Error: " + err);

		System.out.println(VERSION + "\n");
		System.err.println("Usage: " + Bds.class.getSimpleName() + " [options] file.bds");
		System.err.println("\nAvailable options: ");
		System.err.println("  [-c | -config ] bds.config     : Config file. Default : " + configFile + ".");
		System.err.println("  [-compile]                     : Compile only, do not run.");
		System.err.println("  [-coverage]                    : Calculate cofe coverate. Only valid when '-test' is active.");
		System.err.println("  [-coverageMin] ratio           : Fail if coverage is lower than 'ratio' (this is a ratio, so it should be 0.8 instead of 80%).");
		System.err.println("  [-checkPidRegex]               : Check configuration's 'pidRegex' by matching stdin.");
		System.err.println("  [-d | -debug  ]                : Show debug info.");
		System.err.println("  -download url file             : Download 'url' to local 'file'. Note: Used by 'taks'.");
		System.err.println("  -dryRun                        : Do not run any task, just show what would be run. Default: " + dryRun + ".");
		System.err.println("  [-extractSource]               : Extract source code files from checkpoint (only valid combined with '-info').");
		System.err.println("  [-i | -info   ] checkpoint.chp : Show state information in checkpoint file.");
		System.err.println("  [-l | -log    ]                : Log all tasks (do not delete tmp files). Default: " + log + ".");
		System.err.println("  -noChp                         : Do not create any checkpoint files.");
		System.err.println("  -noReport                      : Do not create reports.");
		System.err.println("  -noReportHtml                  : Do not create HTML reports.");
		System.err.println("  -noReportYaml                  : Do not create YAML reports.");
		System.err.println("  -noRmOnExit                    : Do not remove files marked for deletion on exit (rmOnExit).");
		System.err.println("  [-q | -queue  ] queueName      : Set default queue name.");
		System.err.println("  -quiet                         : Do not show any messages or tasks outputs on STDOUT. Default: " + quiet + ".");
		System.err.println("  -reportHtml                    : Create HTML report.");
		System.err.println("  -reportName <name>             : Set base-name for report files.");
		System.err.println("  -reportYaml                    : Create YAML report.");
		System.err.println("  [-r | -restore] checkpoint.chp : Restore state from checkpoint file.");
		System.err.println("  [-s | -system ] type           : Set system type.");
		System.err.println("  [-t | -test   ]                : Run user test cases (runs all test* functions).");
		System.err.println("  -task checkpoint.chp           : Execute an 'improper taks' from checkpoint file.");
		System.err.println("  -upload file url               : Upload local file to 'url'. Note: Used by 'taks'.");
		System.err.println("  [-v | -verbose]                : Be verbose.");
		System.err.println("  -version                       : Show version and exit.");
		System.err.println("  -wall                          : Show all compile time warnings.");
		System.err.println("  [-y | -retry  ] num            : Number of times to retry a failing tasks.");
		System.err.println("  -pid <file>                    : Write local processes PIDs to 'file'.");

		if (err != null) System.exit(1);
		System.exit(0);
	}
}
