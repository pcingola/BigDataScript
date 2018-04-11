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
import org.bds.BdsParseArgs;
import org.bds.Config;
import org.bds.compile.BdsCompiler;
import org.bds.compile.CompilerMessages;
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
import org.bds.scope.GlobalScope;
import org.bds.symbol.GlobalSymbolTable;
import org.bds.task.TaskDependecies;
import org.bds.util.Gpr;
import org.bds.util.Timer;
import org.bds.vm.BdsVm;
import org.bds.vm.VmAsm;

/**
 * Run a bds program
 *
 * @author pcingola
 */
public class BdsRun {

	public enum BdsAction {
		RUN, RUN_CHECKPOINT, ASSEMBLY, COMPILE, INFO_CHECKPOINT, TEST, CHECK_PID_REGEX
	}

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

	public boolean compile() {
		if (!compileBds()) return false;

		try {
			// Get assembly code
			String asm = programUnit.toAsm();
			Gpr.debug("Assembly:\n" + asm);

			// Compile assembly
			VmAsm vmasm = new VmAsm(programUnit);
			vmasm.setDebug(debug);
			vmasm.setVerbose(verbose);
			vmasm.setCode(asm);

			// Compile assembly
			vm = vmasm.compile();
			return true;
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return false;
	}

	/**
	 * Compile program to BdsNode tree
	 * @return True if compiled OK
	 */
	boolean compileBds() {
		if (debug) Timer.showStdErr("Parsing");
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

	public List<String> getProgramArgs() {
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

		// Libraries
		initilaizeNativeLibraries();
	}

	/**
	 * Initialize standard libraries
	 */
	void initilaizeNativeLibraries() {
		if (debug) log("Initialize standard libraries.");

		// Native functions
		NativeLibraryFunctions nativeLibraryFunctions = new NativeLibraryFunctions();
		if (debug) log("Native library:\n" + nativeLibraryFunctions);

		// Native library: String
		NativeLibraryString nativeLibraryString = new NativeLibraryString();
		if (debug) log("Native library:\n" + nativeLibraryString);
	}

	void log(String msg) {
		Timer.showStdErr(getClass().getSimpleName() + ": " + msg);
	}

	/**
	 * Parse command line arguments
	 * @return true if automatic help is shown and program should finish
	 */
	boolean parseCmdLineArgs() {
		if (debug) Timer.showStdErr("Initializing");
		BdsParseArgs bdsParseArgs = new BdsParseArgs(programUnit, programArgs);
		bdsParseArgs.setDebug(debug);
		bdsParseArgs.parse();

		// Show script's automatic help message
		if (bdsParseArgs.isShowHelp()) {
			if (debug) Timer.showStdErr("Showing automaic 'help'");
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

		case TEST:
			exitValue = runTests();
			break;

		case RUN:
			exitValue = runCompile(); // Compile + Run
			break;

		case RUN_CHECKPOINT:
			exitValue = runCheckpoint();
			break;

		default:
			throw new RuntimeException("Unimplemented action '" + bdsAction + "'");
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
	 * Create a BdsThread and run it
	 */
	int runBdsThread() {
		// Create & run thread
		BdsThread bdsThread = new BdsThread(programUnit, config, vm);
		if (debug) {
			Timer.showStdErr("Process ID: " + bdsThread.getBdsThreadId());
			Timer.showStdErr("Running");
		}

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
		// Compile, abort on errors
		if (!compile()) return 1;

		// Parse command line args & show automatic help
		if (parseCmdLineArgs()) return 0;

		// Run thread
		return runBdsThread();
	}

	/**
	 * Compile and run tests
	 */
	int runTests() {
		// Compile, abort on errors
		if (!compile()) return 1;

		// Parse command line args & show automatic help
		if (parseCmdLineArgs()) return 0;

		// Create thread
		BdsThread bdsThread = new BdsThread(programUnit, config);
		if (debug) Timer.showStdErr("Process ID: " + bdsThread.getBdsThreadId());

		// Run tests
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

	public void setBdsAction(BdsAction bdsAction) {
		this.bdsAction = bdsAction;
	}

	public void setChekcpointRestoreFile(String chekcpointRestoreFile) {
		this.chekcpointRestoreFile = chekcpointRestoreFile;
	}

	public void setConfig(Config config) {
		this.config = config;
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

}
