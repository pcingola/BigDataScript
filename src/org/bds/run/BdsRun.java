package org.bds.run;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.bds.Bds;
import org.bds.BdsLog;
import org.bds.BdsLogger;
import org.bds.BdsParseArgs;
import org.bds.Config;
import org.bds.compile.BdsCompiler;
import org.bds.compile.BdsNodeWalker;
import org.bds.compile.CompilerMessages;
import org.bds.data.Data;
import org.bds.data.FtpConnectionFactory;
import org.bds.executioner.ExecutionerCloud;
import org.bds.executioner.ExecutionerFileSystem;
import org.bds.executioner.Executioners;
import org.bds.executioner.Executioners.ExecutionerType;
import org.bds.executioner.MonitorTaskQueue;
import org.bds.executioner.MonitorTasks;
import org.bds.executioner.QueueThreadAwsSqs;
import org.bds.executioner.TaskLogger;
import org.bds.lang.BdsNode;
import org.bds.lang.BdsNodeFactory;
import org.bds.lang.ProgramUnit;
import org.bds.lang.nativeFunctions.NativeLibraryFunctions;
import org.bds.lang.nativeMethods.string.NativeLibraryString;
import org.bds.lang.statement.FunctionDeclaration;
import org.bds.lang.statement.Statement;
import org.bds.lang.type.TypeClass;
import org.bds.lang.type.TypeClassException;
import org.bds.lang.type.TypeClassExceptionConcurrentModification;
import org.bds.lang.type.Types;
import org.bds.osCmd.CmdAws;
import org.bds.scope.GlobalScope;
import org.bds.scope.Scope;
import org.bds.symbol.GlobalSymbolTable;
import org.bds.task.TaskDependecies;
import org.bds.util.Timer;
import org.bds.vm.BdsVm;
import org.bds.vm.BdsVmAsm;

/**
 * Run a bds program
 *
 * @author pcingola
 */
public class BdsRun implements BdsLog {

	public enum BdsAction {
		ASSEMBLY // Only create assembly code and show it to STDOUT
		, CHECK_PID_REGEX // Check that PID regex works
		, COMPILE // Compile only. This is used to check if a program compiles (it does not run the program)
		, INFO_CHECKPOINT // Show information in a checkpoint file
		, RUN // Run a program
		, RUN_CHECKPOINT // Run from a checkpoint
		, RUN_TASK_IMPROPER // Run an improper task from a checkpoint
		, TEST // Run test cases in bds (i.e. compile and run all functions named `test*()`
		, ZZZ // Run the 'zzz()' method. This is only used for developing experimental code (undocumented option
	}

	public enum CompileCode {
		OK, // The code compiled OK
		OK_HELP, // The code compiled OK, '-h' option used so help was shown
		ERROR // There were compilation errors
	};

	boolean coverage; // Run coverage tests
	double coverageMin; // Minimum coverage required to pass a coverage test
	Coverage coverageCounter; // Keep track of coverage between test runs
	boolean debug; // debug mode
	boolean log; // Log everything (keep STDOUT, SDTERR and ExitCode files)
	boolean stackCheck; // Check stack size when thread finishes runnig (should be zero)
	boolean verbose; // Verbose mode
	int exitValue;
	String chekcpointRestoreFile; // Restore file
	String programFileName; // Program file name
	Config config;
	BdsAction bdsAction;
	BdsVm vm;
	BdsThread bdsThread;
	ProgramUnit programUnit; // Program (parsed nodes)
	List<String> programArgs; // Command line arguments for BigDataScript program

	/**
	 * Reset all singleton objects
	 */
	public static void reset() {
		BdsLogger.debug("Full reset");
		Config.reset();
		Executioners.reset();
		BdsThreads.reset();
		Types.reset();
		BdsNodeFactory.reset();
		GlobalSymbolTable.reset();
		GlobalScope.reset();
		TaskDependecies.reset();
		FtpConnectionFactory.kill();
	}

	public BdsRun() {
		bdsAction = BdsAction.RUN;
		programArgs = new ArrayList<>();
	}

	public void addArg(String arg) {
		programArgs.add(arg);
	}

	/**
	 * Print assembly code to STDOUT
	 */
	int assembly() {
		// Compile, abort on errors
		if (!compileBds()) return 1;
		try {
			System.out.println(programUnit.toAsm());
		} catch (Throwable t) {
			if (verbose) t.printStackTrace();
			return 1;
		}
		return 0;
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

		ExecutionerFileSystem executioner = (ExecutionerFileSystem) Executioners.getInstance().get(ExecutionerType.CLUSTER);

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
	 * Compile program: bds -> ATS -> BdsNodes -> VM ASM -> VM OpCodes
	 * @returns: -1 on compile errors; 0 if run OK, 1 if run with errors
	 */
	public CompileCode compile() {
		// COmpile bds to VM ASM
		if (!compileBds()) return CompileCode.ERROR;

		// Parse command line args & show automatic help
		// Note: Command line arguments set variables by changing VarInit
		//       nodes, that's why we do command line parsing before ASM
		//       compilation.
		if (parseCmdLineArgs()) return CompileCode.OK_HELP;

		// Compile assembly
		vm = compileAsm(programUnit);
		return vm != null ? CompileCode.OK : CompileCode.ERROR;
	}

	/**
	 * Compile: BdsNodes -> VM ASM -> VM OpCodes
	 * @return A BdsVm with all compiled assembly code
	 */
	BdsVm compileAsm(ProgramUnit programUnit) {
		try {
			String asm = programUnit.toAsm();
			debug("Assembly code:\n" + asm);

			// Compile assembly
			BdsVmAsm vmasm = new BdsVmAsm(programUnit);
			vmasm.setDebug(debug);
			vmasm.setVerbose(verbose);
			vmasm.setCoverage(coverage);
			vmasm.setCode(asm);

			// Compile assembly
			return vmasm.compile();
		} catch (Throwable t) {
			t.printStackTrace();
			return null;
		}
	}

	/**
	 * Compile program to BdsNode tree: bds -> BdsNodes
	 * @return True if compiled OK
	 */
	boolean compileBds() {
		debug("Parsing");
		BdsCompiler compiler = new BdsCompiler(programFileName);
		programUnit = compiler.compile();

		// Show errors and warnings, if any
		if ((programUnit == null) && !CompilerMessages.get().isEmpty()) {
			System.err.println("Compiler messages:\n" + CompilerMessages.get());
		}

		return programUnit != null;
	}

	public BdsAction getBdsAction() {
		return bdsAction;
	}

	public BdsThread getBdsThread() {
		return bdsThread;
	}

	public Coverage getCoverageCounter() {
		return coverageCounter;
	}

	public double getCoverageMin() {
		return coverageMin;
	}

	public List<String> getProgramArgs() {
		return programArgs;
	}

	public ProgramUnit getProgramUnit() {
		return programUnit;
	}

	public Scope getScope() {
		return vm.getScope();
	}

	public BdsVm getVm() {
		return vm;
	}

	/**
	 * Show information from a checkpoint file
	 */
	int infoCheckpoint() {
		// Load checkpoint file
		BdsThread bdsThreadRoot = loadCheckpoint();

		for (BdsThread bdsThread : bdsThreadRoot.getBdsThreadsAll())
			bdsThread.print();

		return 0;
	}

	/**
	 * Initialize before running or type-checking
	 */
	void initialize() {
		Types.reset();

		// Reset node factory
		BdsNodeFactory.reset();

		// Startup message
		if (log || debug) Timer.showStdErr(Bds.VERSION);

		// Global scope
		GlobalSymbolTable.reset();
		GlobalScope.reset();
		GlobalScope.get().initilaize(config);

		// Initialize native classes
		initilaizeNativeClasses();

		// Libraries
		initilaizeNativeLibraries();
	}

	/**
	 * Initialize a base classes provided by 'bds'
	 */
	void initilaizeNativeClass(TypeClass typeClass) {
		debug("Native class: " + typeClass.getCanonicalName());
	}

	/**
	 * Initialize all base classes provided by 'bds'
	 */
	void initilaizeNativeClasses() {
		debug("Initialize standard classes.");

		initilaizeNativeClass(new TypeClassException());
		initilaizeNativeClass(new TypeClassExceptionConcurrentModification());
	}

	/**
	 * Initialize standard libraries
	 */
	void initilaizeNativeLibraries() {
		debug("Initialize standard libraries.");

		// Native functions
		NativeLibraryFunctions nativeLibraryFunctions = new NativeLibraryFunctions();
		debug("Native library: " + nativeLibraryFunctions.size());

		// Native library: String
		NativeLibraryString nativeLibraryString = new NativeLibraryString();
		debug("Native library: " + nativeLibraryString.size());
	}

	public boolean isCoverage() {
		return coverage;
	}

	@Override
	public boolean isDebug() {
		return debug;
	}

	@Override
	public boolean isLog() {
		return log;
	}

	@Override
	public boolean isVerbose() {
		return verbose;
	}

	/**
	 * Restore from checkpoint and run
	 */
	BdsThread loadCheckpoint() {
		// Load checkpoint file
		log("Loading checkpoint: " + chekcpointRestoreFile);
		BdsThread bdsThreadRoot;
		try {
			// If the checkpoint is remote, download it
			Data d = Data.factory(chekcpointRestoreFile);
			String localFile = d.isRemote() ? d.getLocalPath() : d.getAbsolutePath();
			if (d.isRemote()) d.download();
			// Load data from local file
			ObjectInputStream in = new ObjectInputStream(new GZIPInputStream(new FileInputStream(localFile)));
			bdsThreadRoot = (BdsThread) in.readObject();
			in.close();
		} catch (Exception e) {
			throw new RuntimeException("Error while reading checkpoint file '" + chekcpointRestoreFile + "'", e);
		}

		// Set main thread's programUnit running scope (mostly for debugging and test cases)
		// ProgramUnit's scope it the one before 'global'
		// BdsThread mainThread = bdsThreads.get(0);
		programUnit = bdsThreadRoot.getProgramUnit();

		// Add all nodes
		for (BdsNode n : BdsNodeWalker.findNodes(programUnit, null, true, true)) {
			BdsNodeFactory.get().addNode(n);
		}

		return bdsThreadRoot;
	}

	/**
	 * Parse command line arguments
	 * @return true if automatic help is shown and program should finish
	 */
	boolean parseCmdLineArgs() {
		debug("Initializing");
		BdsParseArgs bdsParseArgs = new BdsParseArgs(programUnit, programArgs);
		bdsParseArgs.setDebug(debug);
		bdsParseArgs.parse();

		// Show script's automatic help message
		if (bdsParseArgs.isShowHelp()) {
			debug("Showing automaic 'help'");
			HelpCreator hc = new HelpCreator(programUnit);
			System.out.println(hc);
			return true;
		}

		return false;
	}

	/**
	 * Run program
	 */
	public int run() {
		// Initialize
		initialize();
		Executioners executioners = Executioners.getInstance(config);
		TaskDependecies.reset();

		//---
		// Run
		//---
		switch (bdsAction) {
		case ASSEMBLY:
			exitValue = assembly();
			break;

		case CHECK_PID_REGEX:
			checkPidRegex();
			exitValue = 0;
			break;

		case COMPILE:
			exitValue = compileBds() ? 0 : 1;
			break;

		case INFO_CHECKPOINT:
			exitValue = infoCheckpoint();
			break;

		case RUN:
			exitValue = runCompile(); // Compile + Run
			break;

		case RUN_CHECKPOINT:
			exitValue = runCheckpoint();
			break;

		case RUN_TASK_IMPROPER:
			exitValue = runTaskImproper();
			break;

		case TEST:
			exitValue = runTests();
			break;

		case ZZZ:
			exitValue = zzz();
			break;

		default:
			throw new RuntimeException("Unimplemented action '" + bdsAction + "'");
		}

		debug("Finished. Exit code: " + exitValue);

		// Kill all executioners
		executioners.kill();

		// Kill other timer tasks
		FtpConnectionFactory.kill();

		config.kill(); // Kill 'tail' and 'monitor' threads

		return exitValue;
	}

	/**
	 * Create a BdsThread and run it
	 */
	int runBdsThread() {
		// Create & run thread
		BdsThread bdsThread = new BdsThread(programUnit, config, vm);
		debug("Process ID '" + bdsThread.getBdsThreadId() + "': Running");

		// Run and get exit code
		int exitCode = runThread(bdsThread);

		// Check stack
		if (stackCheck) bdsThread.sanityCheckStack();

		return exitCode;
	}

	/**
	 * Restore from checkpoint and run
	 */
	int runCheckpoint() {
		// Load checkpoint file
		bdsThread = loadCheckpoint();
		vm = bdsThread.getVm();
		vm.setRecoveredCheckpoint(true);

		// Set main thread's programUnit running scope (mostly for debugging and test cases)
		// ProgramUnit's scope it the one before 'global'
		programUnit = bdsThread.getProgramUnit();

		// Set state and recover tasks
		List<BdsThread> bdsThreads = bdsThread.getBdsThreadsAll();
		bdsThreads.add(bdsThread);
		for (BdsThread bdsThread : bdsThreads) {
			// Re-execute or add tasks (if thread is not finished)
			if (!bdsThread.getRunState().isFinished()) {
				bdsThread.unserializedTasksRestore();
			}
		}

		// All set, run main thread
		debug("Running from checkpoint");
		return runThread(bdsThread);
	}

	/**
	 * BdsCompiler and run
	 */
	int runCompile() {
		// Compile, abort on errors
		debug("Compiling");
		CompileCode ccode = compile();
		switch (ccode) {
		case OK:
			break;

		case OK_HELP:
			return 0;

		case ERROR:
			return 1;

		default:
			throw new RuntimeException("Unknown compile result code: '" + ccode + "'");
		}

		// Run thread
		return runBdsThread();
	}

	/**
	 * Restore from checkpoint and run
	 */
	int runTaskImproper() {
		// Load checkpoint file
		bdsThread = loadCheckpoint();
		vm = bdsThread.getVm();
		vm.setRecoveredCheckpoint(true);

		// Set main thread's programUnit running scope (mostly for debugging and test cases)
		// ProgramUnit's scope it the one before 'global'
		programUnit = bdsThread.getProgramUnit();

		// All set, run main thread
		debug("Running task improper");
		return runThread(bdsThread);
	}

	/**
	 * Compile and run tests
	 */
	int runTests() {
		// Compile, abort on errors
		CompileCode ccode = compile();
		switch (ccode) {
		case OK:
			break;

		case OK_HELP:
			return 0;

		case ERROR:
			return 1;

		default:
			throw new RuntimeException("Unknown compile result code: '" + ccode + "'");
		}

		// Run tests
		debug("Running tests");

		// For each "test*()" function in ProgramUnit, create a thread that executes the function's body
		List<FunctionDeclaration> testFuncs = programUnit.findTestsFunctions();
		if (coverage) coverageCounter = new Coverage();

		int exitCode = 0;
		int testOk = 0, testError = 0;
		for (FunctionDeclaration testFunc : testFuncs) {
			System.out.println("");

			// Run each function
			int exitValTest = runTests(testFunc);

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

		// Show coverage statistics
		if (coverage) {
			System.out.println(coverageCounter);
			if (coverageMin > 0 && coverageCounter.coverageRatio() < coverageMin) {
				exitCode = 1;
			}
		}

		return exitCode;
	}

	/**
	 * Run a single test function, return exit code
	 */
	int runTests(FunctionDeclaration testFunc) {
		// Add all 'declaration' statements
		BdsNodeWalker bwalker = new BdsNodeWalker(programUnit);
		List<Statement> statements = bwalker.findDeclarations();

		// Note: We execute the function's body (not the function declaration)
		statements.add(testFunc.getStatement());

		// Create a program unit having all variable declarations and the test function's statements
		ProgramUnit puTest = new ProgramUnit(programUnit, null);
		puTest.setStatements(statements.toArray(new Statement[0]));

		// Compile and create vm
		BdsVm vmtest = compileAsm(puTest);
		BdsThread bdsThreadTest = new BdsThread(puTest, config, vmtest);

		// Run thread and check exit code
		int exitValTest = runThread(bdsThreadTest);

		// Show coverage results
		if (coverage) coverageCounter.add(vmtest);

		return exitValTest;
	}

	/**
	 * Run a thread
	 */
	int runThread(BdsThread bdsThread) {
		this.bdsThread = bdsThread;
		if (bdsThread.getRunState().isFinished()) return 0;

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

	public void setBdsAction(BdsAction bdsAction) {
		this.bdsAction = bdsAction;
	}

	public void setChekcpointRestoreFile(String chekcpointRestoreFile) {
		this.chekcpointRestoreFile = chekcpointRestoreFile;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public void setCoverage(boolean coverage) {
		this.coverage = coverage;
	}

	public void setCoverageMin(double coverageMin) {
		this.coverageMin = coverageMin;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setLog(boolean log) {
		this.log = log;
	}

	public void setProgramArgs(ArrayList<String> programArgs) {
		this.programArgs = programArgs;
	}

	public void setProgramFileName(String programFileName) {
		this.programFileName = programFileName;
	}

	public void setProgramUnit(ProgramUnit programUnit) {
		this.programUnit = programUnit;
	}

	public void setStackCheck(boolean stackCheck) {
		this.stackCheck = stackCheck;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	/**
	 * Run some experimental code
	 * This is only used for developments (undocumented)
	 */
	private int zzz() {
		debug = false;
		verbose = true;
		CmdAws.DO_NOT_RUN_INSTANCE = true;
		QueueThreadAwsSqs.USE_QUEUE_NAME_DEBUG = true;
		String queueNamePrefix = ExecutionerCloud.EXECUTIONER_QUEUE_NAME_PREFIX_DEFAULT;
		String pidFile = "z.pid";

		// Config
		Config config = Config.get();
		config.setVerbose(verbose);
		config.setDebug(debug);

		// TaskLogger
		TaskLogger taskLogger = new TaskLogger(pidFile);

		// Monitor tasks for queues
		MonitorTaskQueue monitorTask = MonitorTasks.get().getMonitorTaskQueue();

		// Queue thread for AWS SQS
		QueueThreadAwsSqs queueThread = new QueueThreadAwsSqs(config, monitorTask, taskLogger, queueNamePrefix);
		queueThread.setVerbose(verbose);
		queueThread.setDebug(debug);

		// Start process and wait
		debug("Starting queue thread");
		queueThread.start();
		try {
			queueThread.join();
		} catch (InterruptedException e) {
			throw new RuntimeException();
		}

		return 0;
	}
}
