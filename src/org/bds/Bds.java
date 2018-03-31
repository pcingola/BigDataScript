package org.bds;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.bds.compile.CompilerMessages;
import org.bds.data.Data;
import org.bds.executioner.Executioner;
import org.bds.executioner.Executioners;
import org.bds.executioner.Executioners.ExecutionerType;
import org.bds.lang.BdsNodeFactory;
import org.bds.lang.ProgramUnit;
import org.bds.lang.nativeFunctions.NativeLibraryFunctions;
import org.bds.lang.nativeMethods.string.NativeLibraryString;
import org.bds.lang.statement.FunctionDeclaration;
import org.bds.lang.statement.Statement;
import org.bds.lang.statement.VarDeclaration;
import org.bds.lang.type.Types;
import org.bds.run.BdsThread;
import org.bds.run.HelpCreator;
import org.bds.run.RunState;
import org.bds.scope.GlobalScope;
import org.bds.symbol.GlobalSymbolTable;
import org.bds.task.TaskDependecies;
import org.bds.util.Gpr;
import org.bds.util.Timer;

/**
 * BDS command line
 *
 * @author pcingola
 */
public class Bds {

	enum BdsAction {
		RUN, RUN_CHECKPOINT, INFO_CHECKPOINT, TEST
	}

	public static final String SOFTWARE_NAME = Bds.class.getSimpleName();
	public static final String BUILD = Gpr.compileTimeStamp(Bds.class);
	public static final String REVISION = "";
	public static final String VERSION_MAJOR = "1.0";
	public static final String VERSION_SHORT = VERSION_MAJOR + REVISION;

	public static final String VERSION = SOFTWARE_NAME + " " + VERSION_SHORT + " (build " + BUILD + "), by " + Pcingola.BY;

	boolean checkPidRegex; // Check PID regex (do not run program)
	boolean debug; // debug mode
	boolean dryRun; // Dry run (do not run tasks)
	boolean extractSource; // Extract source code form checkpoint (only valid on recovery mode)
	boolean log; // Log everything (keep STDOUT, SDTERR and ExitCode files)
	Boolean noCheckpoint; // Do not create checkpoint files
	Boolean noRmOnExit; // Do not remove temp files on exit
	boolean quiet; // Quiet mode
	boolean stackCheck; // Check stack size when thread finishes runnig (should be zero)
	boolean verbose; // Verbose mode
	boolean warnUnusedFunctionsAnyFile; // Warn if there are unused functions in all any (included) file
	Boolean reportHtml; // Use HTML report style
	Boolean reportYaml; // Use YAML report style
	int taskFailCount = -1;
	String configFile = Config.DEFAULT_CONFIG_FILE; // Configuration file
	String chekcpointRestoreFile; // Restore file
	String programFileName; // Program file name
	String pidFile; // File to store PIDs
	String reportFileName;
	String system; // System type
	String queue; // Queue name
	BdsAction bdsAction;
	Config config;
	ProgramUnit programUnit; // Program (parsed nodes)
	BdsThread bdsThread;
	ArrayList<String> programArgs; // Command line arguments for BigDataScript program

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
		initDefaults();
		parse(args);
		initialize();
	}

	/**
	 * Check 'pidRegex'
	 */
	public void checkPidRegex() {
		// PID regex matcher
		String pidPatternStr = config.getPidRegex("");

		if (pidPatternStr.isEmpty()) {
			System.err.println("Cannot find 'pidRegex' entry in config file.");
			System.exit(1);
		}

		Executioner executioner = Executioners.getInstance().get(ExecutionerType.CLUSTER);

		// Show pattern
		System.out.println("Matching pidRegex '" + pidPatternStr + "'");

		// Read STDIN and check pattern
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String line;
			while ((line = in.readLine()) != null) {
				String pid = executioner.parsePidLine(line);
				System.out.println("Input line:\t'" + line + "'\tMatched: '" + pid + "'");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		executioner.kill(); // Kill executioner
	}

	/**
	 * Load configuration file
	 */
	protected void config() {
		//---
		// Config
		//---
		config = new Config(configFile);
		config.setDebug(debug);
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
	}

	/**
	 * Download a URL to a local file
	 * @return true if successful
	 */
	public boolean download(String url, String fileName) {
		Data remote = Data.factory(url);

		// Sanity checks
		if (!remote.isRemote()) {
			System.err.println("Cannot download non-remote URL: " + url);
			return false;
		}

		if (!remote.isFile()) {
			System.err.println("Cannot download non-file: " + url);
			return false;
		}

		// Already downloaded? Nothing to do
		if (remote.isDownloaded(fileName)) {
			if (debug) System.err.println("Local file is up to date, no download required: " + fileName);
			return true;
		}

		return remote.download(fileName);
	}

	public BdsThread getBigDataScriptThread() {
		return bdsThread;
	}

	public CompilerMessages getCompilerMessages() {
		return CompilerMessages.get();
	}

	public Config getConfig() {
		return config;
	}

	public ArrayList<String> getProgramArgs() {
		return programArgs;
	}

	public ProgramUnit getProgramUnit() {
		return programUnit;
	}

	/**
	 * Show information from a checkpoint file
	 */
	int infoCheckpoint() {
		// Load checkpoint file

		// TODO: LOAD FROM CHECKLPOINT  !!!!!!!!!!!!!

		//		for (BdsThread bdsThread : bdsThreads)
		//			bdsThread.print();

		return 0;
	}

	/**
	 * Get default settings
	 */
	void initDefaults() {
		reportFileName = null;
		reportHtml = null;
		reportYaml = null;
		dryRun = false;
		log = false;
	}

	/**
	 * Initialize before running or type-checking
	 */
	void initialize() {
		Types.reset();

		// Reset node factory
		BdsNodeFactory.reset();

		// Startup message
		if (log || debug) Timer.showStdErr(VERSION);

		// Load config file
		config();

		// Global scope
		GlobalSymbolTable.reset();
		GlobalScope.reset();
		GlobalScope.get().initilaize(config);

		// Libraries
		initilaizeLibraries();
	}

	/**
	 * Initialize standard libraries
	 */
	void initilaizeLibraries() {
		if (debug) log("Initialize standard libraries.");

		// Native functions
		NativeLibraryFunctions nativeLibraryFunctions = new NativeLibraryFunctions();
		if (debug) log("Native library:\n" + nativeLibraryFunctions);

		// Native library: String
		NativeLibraryString nativeLibraryString = new NativeLibraryString();
		if (debug) log("Native library:\n" + nativeLibraryString);
	}

	/**
	 * Is this a command line option (e.g. "-tfam" is a command line option, but "-" means STDIN)
	 */
	protected boolean isOpt(String arg) {
		return arg.startsWith("-") && (arg.length() > 1);
	}

	//	void log(String msg) {
	//		Timer.showStdErr(getClass().getSimpleName() + ": " + msg);
	//	}

	/**
	 * Parse command line arguments
	 */
	public void parse(String[] args) {
		// Nothing? Show command line options
		if (args.length <= 0) usage(null);

		programArgs = new ArrayList<>();
		bdsAction = BdsAction.RUN;

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];

			if (programFileName != null) {
				// Everything after 'programFileName' is an command line
				// argument for the BigDataScript program
				programArgs.add(arg);
			} else if (isOpt(arg)) {

				switch (arg.toLowerCase()) {
				case "-checkpidregex":
					checkPidRegex = true;
					break;

				case "-c":
				case "-config":
					// Checkpoint restore
					if ((i + 1) < args.length) configFile = args[++i];
					else usage("Option '-c' without restore file argument");
					break;

				case "-d":
				case "-debug":
					debug = verbose = true; // Debug implies verbose
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
					bdsAction = BdsAction.INFO_CHECKPOINT;
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
					bdsAction = BdsAction.RUN_CHECKPOINT;
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

				case "-t":
				case "-test":
					bdsAction = BdsAction.TEST;
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
					warnUnusedFunctionsAnyFile = true;
					break;

				case "-y":
				case "-retry":
					// Number of retries
					if ((i + 1) < args.length) taskFailCount = Gpr.parseIntSafe(args[++i]);
					else usage("Option '-t' without number argument");
					break;

				default:
					usage("Unknown command line option " + arg);
				}
			} else if (programFileName == null) programFileName = arg; // Get program file name

		}

		// Sanity checks
		if (checkPidRegex) {
			// OK: Nothing to check
		} else if ((programFileName == null) && (chekcpointRestoreFile == null)) {
			// No file name => Error
			usage("Missing program file name.");
		}
	}

	//	/**
	//	 * Lex, parse and create Abstract syntax tree (AST)
	//	 */
	//	ParseTree parseProgram() {
	//		if (debug) log("Creating AST.");
	//		CompilerMessages.reset();
	//		ParseTree tree = null;
	//
	//		try {
	//			tree = createAst();
	//		} catch (Exception e) {
	//			System.err.println("Fatal error cannot continue - " + e.getMessage());
	//			return null;
	//		}
	//
	//		// No tree produced? Fatal error
	//		if (tree == null) {
	//			if (CompilerMessages.get().isEmpty()) {
	//				CompilerMessages.get().addError("Fatal error: Could not compile");
	//			}
	//			return null;
	//		}
	//
	//		// Any error? Do not continue
	//		if (!CompilerMessages.get().isEmpty()) return null;
	//		return tree;
	//	}

	/**
	 * Run script
	 */
	public int run() {
		// Initialize
		Executioners executioners = Executioners.getInstance(config);
		TaskDependecies.reset();

		// Check PID regex
		if (checkPidRegex) {
			checkPidRegex();
			return 0;
		}

		//---
		// Run
		//---
		int exitValue = 0;
		switch (bdsAction) {
		case RUN_CHECKPOINT:
			exitValue = runCheckpoint();
			break;

		case INFO_CHECKPOINT:
			exitValue = infoCheckpoint();
			break;

		case TEST:
			exitValue = runTests();
			break;

		default:
			exitValue = runCompile(); // BdsCompiler & run
		}
		if (debug) Timer.showStdErr("Finished. Exit code: " + exitValue);

		//---
		// Kill all executioners
		//---
		for (Executioner executioner : executioners.getAll())
			executioner.kill();

		config.kill(); // Kill 'tail' and 'monitor' threads

		return exitValue;
	}

	/**
	 * Restore from checkpoint and run
	 */
	int runCheckpoint() {
		// Load checkpoint file

		// TODO: REMOVE BdsSerializer
		//		BdsSerializer bdsSerializer = new BdsSerializer(chekcpointRestoreFile, config);
		//		List<BdsThread> bdsThreads = bdsSerializer.load();

		BdsThread bdsThreadRoot;
		try {
			ObjectInputStream in = new ObjectInputStream(new GZIPInputStream(new FileInputStream(chekcpointRestoreFile)));
			bdsThreadRoot = (BdsThread) in.readObject();
			in.close();
		} catch (Exception e) {
			throw new RuntimeException("Error while serializing to file '" + chekcpointRestoreFile + "'", e);
		}

		// Set main thread's programUnit running scope (mostly for debugging and test cases)
		// ProgramUnit's scope it the one before 'global'
		// BdsThread mainThread = bdsThreads.get(0);
		BdsThread mainThread = bdsThreadRoot;
		programUnit = mainThread.getProgramUnit();

		// Set state and recover tasks
		List<BdsThread> bdsThreads = bdsThreadRoot.getBdsThreads();
		bdsThreads.add(bdsThreadRoot);
		for (BdsThread bdsThread : bdsThreads) {
			if (bdsThread.isFinished()) {
				// Thread finished before serialization: Nothing to do
			} else {
				bdsThread.setRunState(RunState.CHECKPOINT_RECOVER); // Set run state to recovery
				bdsThread.restoreUnserializedTasks(); // Re-execute or add tasks
			}
		}

		// All set, run main thread
		return runThread(mainThread);
	}

	/**
	 * BdsCompiler and run
	 */
	int runCompile() {
		// BdsCompiler, abort on errors
		if (debug) Timer.showStdErr("Parsing");
		if (!compile()) {
			// Show errors and warnings, if any
			if (!CompilerMessages.get().isEmpty()) System.err.println("Compiler messages:\n" + CompilerMessages.get());
			return 1;
		}

		if (debug) Timer.showStdErr("Initializing");
		BdsParseArgs bdsParseArgs = new BdsParseArgs(this);
		bdsParseArgs.setDebug(debug);
		bdsParseArgs.parse();

		// Show script's automatic help message
		if (bdsParseArgs.isShowHelp()) {
			if (debug) Timer.showStdErr("Showing automaic 'help'");
			HelpCreator hc = new HelpCreator(programUnit);
			System.out.println(hc);
			return 0;
		}

		// Run the program
		BdsThread bdsThread = new BdsThread(programUnit, config);
		if (debug) {
			Timer.showStdErr("Process ID: " + bdsThread.getBdsThreadId());
			Timer.showStdErr("Running");
		}
		int exitCode = runThread(bdsThread);

		// Check stack
		if (stackCheck) bdsThread.sanityCheckStack();

		return exitCode;
	}

	/**
	 * BdsCompiler and run
	 */
	int runTests() {
		// BdsCompiler, abort on errors
		if (debug) Timer.showStdErr("Parsing");
		if (!compile()) {
			// Show errors and warnings, if any
			if (!CompilerMessages.get().isEmpty()) System.err.println("Compiler messages:\n" + CompilerMessages.get());
			return 1;
		}

		if (debug) Timer.showStdErr("Initializing");
		BdsParseArgs bdsParseArgs = new BdsParseArgs(this);
		bdsParseArgs.setDebug(debug);
		bdsParseArgs.parse();

		// Run the program
		BdsThread bdsThread = new BdsThread(programUnit, config);
		if (debug) Timer.showStdErr("Process ID: " + bdsThread.getBdsThreadId());

		if (debug) Timer.showStdErr("Running tests");
		ProgramUnit pu = bdsThread.getProgramUnit();
		return runTests(pu);
	}

	/**
	 * For each "test*()" function in ProgramUnit, create a thread
	 * that executes the function's body
	 */
	int runTests(ProgramUnit progUnit) {
		// We need to execute all variable declarations in order to be able to use global vairables in 'test*()' functions"
		List<VarDeclaration> varDecls = programUnit.varDeclarations(false);
		List<FunctionDeclaration> testFuncs = progUnit.testsFunctions();

		int exitCode = 0;
		int testOk = 0, testError = 0;
		for (FunctionDeclaration testFunc : testFuncs) {
			System.out.println("");

			// Run each function
			int exitValTest = runTests(progUnit, testFunc, varDecls);

			// Show test result
			if (exitValTest == 0) {
				Timer.show("Test '" + testFunc.getFunctionName() + "': OK");
				testOk++;
			} else {
				Timer.show("Test '" + testFunc.getFunctionName() + "': FAIL");
				exitCode = 1;
				testError++;
			}
		}

		// Show results
		System.out.println("");
		Timer.show("Totals"//
				+ "\n                  OK    : " + testOk //
				+ "\n                  ERROR : " + testError //
		);

		return exitCode;
	}

	/**
	 * Run a single test function, return exit code
	 */
	int runTests(ProgramUnit progUnit, FunctionDeclaration testFunc, List<VarDeclaration> varDecls) {
		List<Statement> statements = new ArrayList<>();

		// Add all variable declarations
		for (VarDeclaration varDecl : varDecls)
			statements.add(varDecl);

		// Note: We execute the function's body (not the function declaration)
		statements.add(testFunc.getStatement());

		// Create a program unit having all variable declarations and the test function's statements
		ProgramUnit puTest = new ProgramUnit(progUnit, null);
		puTest.setStatements(statements.toArray(new Statement[0]));

		BdsThread bdsTestThread = new BdsThread(puTest, config);
		int exitValTest = runThread(bdsTestThread);
		return exitValTest;
	}

	/**
	 * Run a thread
	 */
	int runThread(BdsThread bdsThread) {
		this.bdsThread = bdsThread;
		if (bdsThread.isFinished()) return 0;

		bdsThread.start();

		try {
			bdsThread.join();
		} catch (InterruptedException e) {
			// Nothing to do?
			// May be checkpoint?
			return 1;
		}

		// Check stack
		if (stackCheck) bdsThread.sanityCheckStack();

		// OK, we are done
		return bdsThread.getExitValue();
	}

	public void setStackCheck(boolean stackCheck) {
		this.stackCheck = stackCheck;
	}

	//	/**
	//	 * Type checking
	//	 */
	//	boolean typeChecking() {
	//		if (debug) log("Type checking.");
	//		CompilerMessages.reset();
	//		GlobalSymbolTable globalSymbolTable = GlobalSymbolTable.get();
	//		if (debug) log("Global SymbolTable:\n" + globalSymbolTable);
	//		programUnit.typeChecking(globalSymbolTable, CompilerMessages.get());
	//
	//		// Any error messages?
	//		if (!CompilerMessages.get().isEmpty()) System.err.println("Compiler messages:\n" + CompilerMessages.get());
	//		if (CompilerMessages.get().hasErrors()) return true;
	//		return false;
	//	}

	/**
	 * Upload a local file to a URL
	 * @return true if successful
	 */
	public boolean upload(String fileName, String url) {
		Data remote = Data.factory(url);
		Data local = Data.factory(fileName);

		// Sanity checks
		if (!remote.isRemote()) {
			System.err.println("Cannot upload to non-remote URL: " + url);
			return false;
		}

		if (!local.isFile()) {
			System.err.println("Cannot upload non-file: " + fileName);
			return false;
		}

		if (!local.exists()) {
			System.err.println("Local file does not exists: " + fileName);
			return false;
		}

		if (!local.canRead()) {
			System.err.println("Cannot read local file : " + fileName);
			return false;
		}

		// Already uploaded? Nothing to do
		if (remote.isUploaded(fileName)) {
			if (debug) System.err.println("Remote file is up to date, no upload required: " + url);
			return true;
		}

		return remote.upload(fileName);
	}

	void usage(String err) {
		if (err != null) System.err.println("Error: " + err);

		System.out.println(VERSION + "\n");
		System.err.println("Usage: " + Bds.class.getSimpleName() + " [options] file.bds");
		System.err.println("\nAvailable options: ");
		System.err.println("  [-c | -config ] bds.config     : Config file. Default : " + configFile);
		System.err.println("  [-checkPidRegex]               : Check configuration's 'pidRegex' by matching stdin.");
		System.err.println("  [-d | -debug  ]                : Debug mode.");
		System.err.println("  -download url file             : Download 'url' to local 'file'. Note: Used by 'taks'");
		//		System.err.println("  -done                          : Use 'done' files: Default: " + useDoneFile);
		System.err.println("  -dryRun                        : Do not run any task, just show what would be run. Default: " + dryRun);
		System.err.println("  [-extractSource]               : Extract source code files from checkpoint (only valid combined with '-info').");
		System.err.println("  [-i | -info   ] checkpoint.chp : Show state information in checkpoint file.");
		System.err.println("  [-l | -log    ]                : Log all tasks (do not delete tmp files). Default: " + log);
		System.err.println("  -noChp                         : Do not create any checkpoint files.");
		System.err.println("  -noRmOnExit                    : Do not remove files marked for deletion on exit (rmOnExit). Default: " + noRmOnExit);
		System.err.println("  [-q | -queue  ] queueName      : Set default queue name.");
		System.err.println("  -quiet                         : Do not show any messages or tasks outputs on STDOUT. Default: " + quiet);
		System.err.println("  -reportHtml                    : Create HTML report. Default: " + reportHtml);
		System.err.println("  -reportName <name>             : Set base-name for report files.");
		System.err.println("  -reportYaml                    : Create YAML report. Default: " + reportYaml);
		System.err.println("  [-r | -restore] checkpoint.chp : Restore state from checkpoint file.");
		System.err.println("  [-s | -system ] type           : Set system type.");
		System.err.println("  [-t | -test   ]                : Run user test cases (runs all test* functions).");
		System.err.println("  -upload file url               : Upload local file to 'url'. Note: Used by 'taks'");
		System.err.println("  [-v | -verbose]                : Be verbose.");
		System.err.println("  -version                       : Show version and exit.");
		System.err.println("  -wall                          : Show all compile time warnings.");
		System.err.println("  [-y | -retry  ] num            : Number of times to retry a failing tasks.");
		System.err.println("  -pid <file>                    : Write local processes PIDs to 'file'");

		if (err != null) System.exit(1);
		System.exit(0);
	}

}
