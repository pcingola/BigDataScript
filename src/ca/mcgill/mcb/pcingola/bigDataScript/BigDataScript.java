package ca.mcgill.mcb.pcingola.bigDataScript;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.LexerNoViableAltException;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.Tree;

import ca.mcgill.mcb.pcingola.bigDataScript.antlr.BigDataScriptLexer;
import ca.mcgill.mcb.pcingola.bigDataScript.antlr.BigDataScriptParser;
import ca.mcgill.mcb.pcingola.bigDataScript.antlr.BigDataScriptParser.IncludeFileContext;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompileErrorStrategy;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerErrorListener;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.TypeCheckedNodes;
import ca.mcgill.mcb.pcingola.bigDataScript.data.Data;
import ca.mcgill.mcb.pcingola.bigDataScript.executioner.Executioner;
import ca.mcgill.mcb.pcingola.bigDataScript.executioner.Executioners;
import ca.mcgill.mcb.pcingola.bigDataScript.executioner.Executioners.ExecutionerType;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.BigDataScriptNodeFactory;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.ExpressionTask;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.FunctionDeclaration;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Literal;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.LiteralBool;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.LiteralInt;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.LiteralListString;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.LiteralReal;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.LiteralString;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.ProgramUnit;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.StatementInclude;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeList;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.VarDeclaration;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.VariableInit;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions.NativeLibraryFunctions;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.string.NativeLibraryString;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.HelpCreator;
import ca.mcgill.mcb.pcingola.bigDataScript.run.RunState;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.ScopeSymbol;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BdsSerializer;
import ca.mcgill.mcb.pcingola.bigDataScript.task.TaskDependecies;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * BigDataScript command line 
 *
 * @author pcingola
 */
public class BigDataScript {

	enum BigDataScriptAction {
		RUN, RUN_CHECKPOINT, INFO_CHECKPOINT, TEST
	}

	public static final String SOFTWARE_NAME = BigDataScript.class.getSimpleName();
	public static final String BUILD = "2015-09-29";
	public static final String REVISION = "c";
	public static final String VERSION_MAJOR = "0.9999";
	public static final String VERSION_SHORT = VERSION_MAJOR + REVISION;

	public static final String VERSION = SOFTWARE_NAME + " " + VERSION_SHORT + " (build " + BUILD + "), by " + Pcingola.BY;

	boolean checkPidRegex; // Check PID regex (do not run program)
	boolean debug; // debug mode
	boolean dryRun; // Dry run (do not run tasks)
	boolean extractSource; // Extract source code form checkpoint (nly valid on recovery mode)
	boolean log; // Log everything (keep STDOUT, SDTERR and ExitCode files)
	Boolean noCheckpoint; // Do not create checkpoint files
	Boolean noRmOnExit; // Do not remove temp files on exit
	boolean quiet; // Quiet mode
	boolean showHelp; // Show bds's script help (provided on each variable definition). Do not run program
	boolean stackCheck; // Check stack size when thread finishes runnig (should be zero)
	boolean verbose; // Verbose mode
	boolean reportHtml; // Use HTML report style
	boolean reportYaml; // Use YAML report style
	int taskFailCount = -1;
	String configFile = Config.DEFAULT_CONFIG_FILE; // Configuration file
	String chekcpointRestoreFile; // Restore file
	String programFileName; // Program file name
	String pidFile; // File to store PIDs
	String system; // System type
	String queue; // Queue name
	BigDataScriptAction bigDataScriptAction;
	Config config;
	ProgramUnit programUnit; // Program (parsed nodes)
	BdsThread bdsThread;
	ArrayList<String> programArgs; // Command line arguments for BigDataScript program

	/**
	 * Create an AST from a program (using ANTLR lexer & parser)
	 * Returns null if error
	 * Use 'alreadyIncluded' to keep track of from 'include' statements
	 */
	public static ParseTree createAst(File file, boolean debug, Set<String> alreadyIncluded) {
		alreadyIncluded.add(Gpr.getCanonicalFileName(file));
		String fileName = file.toString();
		String filePath = fileName;

		BigDataScriptLexer lexer = null;
		BigDataScriptParser parser = null;

		try {
			filePath = file.getCanonicalPath();

			// Input stream
			if (!Gpr.canRead(filePath)) {
				CompilerMessages.get().addError("Can't read file '" + filePath + "'");
				return null;
			}

			// Create a CharStream that reads from standard input
			ANTLRFileStream input = new ANTLRFileStream(fileName);

			//---
			// Lexer: Create a lexer that feeds off of input CharStream
			//---
			lexer = new BigDataScriptLexer(input) {
				@Override
				public void recover(LexerNoViableAltException e) {
					throw new RuntimeException(e); // Bail out
				}
			};

			//---
			// Parser
			//---
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			parser = new BigDataScriptParser(tokens);

			// Parser error handling
			parser.setErrorHandler(new CompileErrorStrategy()); // Bail out with exception if errors in parser
			parser.addErrorListener(new CompilerErrorListener()); // Catch some other error messages that 'CompileErrorStrategy' fails to catch

			// Begin parsing at main rule
			ParseTree tree = parser.programUnit();

			// Error loading file?
			if (tree == null) {
				System.err.println("Can't parse file '" + filePath + "'");
				return null;
			}

			// Show main nodes
			if (debug) {
				Timer.showStdErr("AST:");
				for (int childNum = 0; childNum < tree.getChildCount(); childNum++) {
					Tree child = tree.getChild(childNum);
					System.err.println("\t\tChild " + childNum + ":\t" + child + "\tTree:'" + child.toStringTree() + "'");
				}
			}

			// Included files
			boolean resolveIncludePending = true;
			while (resolveIncludePending)
				resolveIncludePending = resolveIncludes(tree, debug, alreadyIncluded);

			return tree;
		} catch (Exception e) {
			String msg = e.getMessage();
			CompilerMessages.get().addError("Could not compile " + filePath //
					+ (msg != null ? " :" + e.getMessage() : "") //
			);
			return null;
		}
	}

	/**
	 * Main
	 */
	public static void main(String[] args) {
		// Create BigDataScript object and run it
		BigDataScript bigDataScript = new BigDataScript(args);
		int exitValue = bigDataScript.run();
		System.exit(exitValue);
	}

	/**
	 * Resolve include statements
	 */
	private static boolean resolveIncludes(ParseTree tree, boolean debug, Set<String> alreadyIncluded) {
		boolean changed = false;
		if (tree instanceof IncludeFileContext) {
			// Parent file: The one that is including the other file
			File parentFile = new File(((IncludeFileContext) tree).getStart().getInputStream().getSourceName());

			// Included file name
			String includedFilename = StatementInclude.includeFileName(tree.getChild(1).getText());

			// Find file (look into all include paths)
			File includedFile = StatementInclude.includeFile(includedFilename, parentFile);
			if (includedFile == null) {
				CompilerMessages.get().add(tree, parentFile, "\n\tIncluded file not found: '" + includedFilename + "'\n\tSearch path: " + Config.get().getIncludePath(), MessageType.ERROR);
				return false;
			}

			// Already included? don't bother
			String canonicalFileName = Gpr.getCanonicalFileName(includedFile);
			if (alreadyIncluded.contains(canonicalFileName)) {
				if (debug) Gpr.debug("File already included: '" + includedFilename + "'\tCanonical path: '" + canonicalFileName + "'");
				return false;
			}
			if (!includedFile.canRead()) {
				CompilerMessages.get().add(tree, parentFile, "\n\tCannot read included file: '" + includedFilename + "'", MessageType.ERROR);
				return false;
			}

			// Parse
			ParseTree treeinc = createAst(includedFile, debug, alreadyIncluded);
			if (treeinc == null) {
				CompilerMessages.get().add(tree, parentFile, "\n\tFatal error including file '" + includedFilename + "'", MessageType.ERROR);
				return false;
			}

			// Is a child always a RuleContext?
			for (int i = 0; i < treeinc.getChildCount(); i++) {
				((IncludeFileContext) tree).addChild((RuleContext) treeinc.getChild(i));
			}
		} else {
			for (int i = 0; i < tree.getChildCount(); i++)
				changed |= resolveIncludes(tree.getChild(i), debug, alreadyIncluded);
		}

		return changed;
	}

	public BigDataScript(String args[]) {
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
	 * Compile program
	 */
	public boolean compile() {
		if (debug) log("Loading file: '" + programFileName + "'");

		//---
		// Convert to AST
		//---
		if (debug) log("Creating AST.");
		CompilerMessages.reset();
		ParseTree tree = null;

		try {
			tree = createAst();
		} catch (Exception e) {
			System.err.println("Fatal error cannot continue - " + e.getMessage());
			return false;
		}

		// No tree produced? Fatal error
		if (tree == null) {
			if (CompilerMessages.get().isEmpty()) {
				CompilerMessages.get().addError("Fatal error: Could not compile");
			}
			return false;
		}

		// Any error? Do not continue
		if (!CompilerMessages.get().isEmpty()) return false;

		//---
		// Convert to BigDataScriptNodes
		//---
		if (debug) log("Creating BigDataScript tree.");
		CompilerMessages.reset();
		programUnit = (ProgramUnit) BigDataScriptNodeFactory.get().factory(null, tree); // Transform AST to BigDataScript tree
		if (debug) log("AST:\n" + programUnit.toString());
		// Any error messages?
		if (!CompilerMessages.get().isEmpty()) System.err.println("Compiler messages:\n" + CompilerMessages.get());
		if (CompilerMessages.get().hasErrors()) return false;

		//---
		// Type-checking
		//---
		if (debug) log("Type checking.");
		CompilerMessages.reset();

		Scope programScope = new Scope();
		programUnit.typeChecking(programScope, CompilerMessages.get());

		// Any error messages?
		if (!CompilerMessages.get().isEmpty()) System.err.println("Compiler messages:\n" + CompilerMessages.get());
		if (CompilerMessages.get().hasErrors()) return false;

		// Free some memory by reseting structure we won't use any more
		TypeCheckedNodes.get().reset();

		// OK
		return true;
	}

	/**
	 * Load configuration file
	 */
	protected void config() {
		//---
		// Config
		//---
		config = new Config(configFile);
		config.setQuiet(quiet);
		config.setVerbose(verbose);
		config.setDebug(debug);
		config.setLog(log);
		config.setDryRun(dryRun);
		config.setTaskFailCount(taskFailCount);
		config.setReportHtml(reportHtml);
		config.setReportYaml(reportYaml);
		config.setExtractSource(extractSource);
		config.setVerbose(verbose);

		// Override config file by command line option
		if (noRmOnExit != null) config.setNoRmOnExit(noRmOnExit);

		if (noCheckpoint != null) config.setNoCheckpoint(noCheckpoint);

		if (pidFile == null) {
			if (programFileName != null) pidFile = programFileName + ".pid";
			else pidFile = chekcpointRestoreFile + ".pid";
		}

		config.setPidFile(pidFile);
	}

	/**
	 * Create an AST from a program file
	 * @return A parsed tree
	 */
	ParseTree createAst() {
		File file = new File(programFileName);
		return createAst(file, debug, new HashSet<String>());
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
			if (verbose) System.err.println("Local file is up to date, no download required: " + fileName);
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

	public ProgramUnit getProgramUnit() {
		return programUnit;
	}

	/**
	 * Show information from a checkpoint file
	 */
	int infoCheckpoint() {
		// Load checkpoint file
		BdsSerializer bdsSerializer = new BdsSerializer(chekcpointRestoreFile, config);
		List<BdsThread> bdsThreads = bdsSerializer.load();

		for (BdsThread bdsThread : bdsThreads)
			bdsThread.print();

		return 0;
	}

	/**
	 * Get default settings
	 */
	void initDefaults() {
		reportHtml = true;
		reportYaml = false;
		dryRun = false;
		log = false;
		//		useDoneFile = false;
	}

	/**
	 * Initialize before running or type-checking
	 */
	void initialize() {
		Type.reset();

		// Reset node factory
		BigDataScriptNodeFactory.reset();

		// Startup message
		if (verbose || debug) Timer.showStdErr(VERSION);

		// Load config file
		config();

		// Global scope
		initilaizeGlobalScope();

		// Libraries
		initilaizeLibraries();
	}

	/**
	 * Set command line arguments as global variables
	 *
	 * How it works: - Program is executes as something like:
	 *
	 * java -jar BigDataScript.jar [options] programFile.bds [programOptions]
	 *
	 * - Any command line argument AFTER "programFile.bds" is considered a
	 * command line argument for the BigDataScript program. E.g.
	 *
	 * java -jar BigDataScript.jar -v program.bds -file myFile.txt -verbose -num
	 * 7
	 *
	 * So our program "program.bds" has command line options: -file myFile.txt
	 * -verbose -num 7 (notice that "-v" is a command line option for
	 * loudScript.jar and not for "program.bds")
	 *
	 * - We look for variables in ProgramUnit that match the name of these
	 * command line arguments
	 *
	 * - Then we add those values to the variable initialization. Thus
	 * overriding any initialization values provided in the program. E.g.
	 *
	 * Our program has the following variable declarations: string file =
	 * "default_file.txt" int num = 3 bool verbose = false
	 *
	 * We execute the program: java -jar BigDataScript.jar -v program.bds -file
	 * myFile.txt -verbose -num 7
	 *
	 * The variable declarations are replaced as follows: string file =
	 * "myFile.txt" int num = 7 bool verbose = true
	 *
	 * - Note: Only primitive types are supported (i.e.: string, bool, int &
	 * real)
	 *
	 * - Note: Unmatched variables names will be silently ignored (same for
	 * variables that match, but are non-primitive)
	 *
	 * - Note: If a variable is matched, is primitive, but cannot be converted.
	 * An error is thrown. E.g.: Program: int num = 1
	 *
	 * Command line: java -jar BigDataScript.jar program.bds -num "hello" <-
	 * This is an error because 'num' is an int
	 *
	 * - Note: Unprocessed arguments will be available to the program as an
	 * 'args' list
	 */
	void initializeArgs() {
		// Set program arguments as global variables
		for (int argNum = 0; argNum < programArgs.size(); argNum++) {
			String arg = programArgs.get(argNum);

			// Parse '-OPT' option
			if (arg.equalsIgnoreCase("-h") || arg.equalsIgnoreCase("-help") || arg.equalsIgnoreCase("--help")) {
				if (debug) Timer.showStdErr("Activating 'show help' mode");
				showHelp = true;
			} else if (arg.startsWith("-")) {
				// Get variable name and value
				String varName = arg.substring(1);

				// Find all variable declarations that match this command line argument
				for (VarDeclaration varDecl : programUnit.varDeclarations(true)) {
					Type varType = varDecl.getType();

					// Is is a primitive variable or a primitive list?
					if (varType.isPrimitiveType() || varType.isList()) {

						// Find an initialization that matches the command line argument
						for (VariableInit varInit : varDecl.getVarInit())
							if (varInit.getVarName().equals(varName)) { // Name matches?
								int argNumOri = argNum;
								boolean useVal = false;

								if (varType.isList()) {
									// Create a list of arguments and use them to initialize the variable (list)
									ArrayList<String> vals = new ArrayList<String>();
									for (int i = argNum + 1; i < programArgs.size(); i++)
										if (programArgs.get(i).startsWith("-")) break;
										else vals.add(programArgs.get(i));

									useVal = initializeArgs(varType, varInit, vals); // Found variable, try to replace or add LITERAL to this VarInit
								} else if (varType.isBool()) {
									String valStr = "true";

									// Booleans may not have a value (just '-varName' sets them to 'true')
									if (programArgs.size() > (argNum + 1)) {
										// Is the next argument 'true' or 'false'? => Set argument
										String boolVal = programArgs.get(argNum + 1);
										if (valStr.equalsIgnoreCase("true") || valStr.equalsIgnoreCase("false")) valStr = boolVal;
									}

									initializeArgs(varType, varInit, valStr);
								} else {
									String val = (argNum < programArgs.size() ? programArgs.get(++argNum) : ""); // Get one argument and use it to initialize the variable
									useVal = initializeArgs(varType, varInit, val); // Found variable, try to replace or add LITERAL to this VarInit
								}

								if (!useVal) argNum = argNumOri; // We did not use the arguments
							}
					}
				}
			}
		}

		// Make all unprocessed arguments available for the program (in 'args' list)
		Scope.getGlobalScope().add(new ScopeSymbol(Scope.GLOBAL_VAR_ARGS_LIST, TypeList.get(Type.STRING), programArgs));

		// Initialize program name
		String programPath = programUnit.getFileName();
		String progName = Gpr.baseName(programPath);
		Scope.getGlobalScope().add(new ScopeSymbol(Scope.GLOBAL_VAR_PROGRAM_NAME, Type.STRING, progName));
		Scope.getGlobalScope().add(new ScopeSymbol(Scope.GLOBAL_VAR_PROGRAM_PATH, Type.STRING, programPath));
	}

	/**
	 * Add or replace initialization statement in this VarInit
	 *
	 * Note: We create a Literal node (of the appropriate type) and add it to
	 * "varInit.expression"
	 *
	 * @param varType
	 *            : Variable type
	 * @param varInit
	 *            : Variable initialization
	 * @param vals
	 *            : Value to assign
	 */
	boolean initializeArgs(Type varType, VariableInit varInit, ArrayList<String> vals) {
		boolean usedVal = true;

		try {
			Literal literal = null;

			if (varType.isList(Type.STRING)) {
				// Create literal
				LiteralListString lit = new LiteralListString(varInit, null);
				literal = lit;
				lit.setValue(vals); // Set literal value
			} else throw new RuntimeException("Cannot convert command line argument to variable type '" + varType + "'");

			// Set varInit to literal
			varInit.setExpression(literal);
		} catch (Exception e) {
			// Error parsing 'val'?
			throw new RuntimeException("Cannot convert argument '" + vals + "' to type " + varType);
		}

		return usedVal;
	}

	/**
	 * Add or replace initialization statement in this VarInit
	 *
	 * Note: We create a Literal node (of the appropriate type) and add it to
	 * "varInit.expression"
	 *
	 * @param varType
	 *            : Variable type
	 * @param varInit
	 *            : Variable initialization
	 * @param valStr
	 *            : Value to assign
	 */
	boolean initializeArgs(Type varType, VariableInit varInit, String valStr) {
		boolean usedVal = true;

		try {
			Literal literal = null;

			// Create a different literal for each primitive type
			if (varType.isBool()) {
				// Create literal
				LiteralBool lit = new LiteralBool(varInit, null);
				literal = lit;

				// Set literal value
				boolean valBool = true; // Default value is 'true'
				if (valStr != null) {
					// Parse boolean
					valStr = valStr.toLowerCase();
					if (valStr.equals("true") || valStr.equals("t") || valStr.equals("1")) valBool = true;
					else if (valStr.equals("false") || valStr.equals("f") || valStr.equals("0")) valBool = false;
					else usedVal = false; // Not any valid value? => This
					// argument is not used
				}

				lit.setValue(valBool);
			} else if (varType.isInt()) {
				// Create literal
				LiteralInt lit = new LiteralInt(varInit, null);
				literal = lit;

				// Set literal value
				long valInt = Long.parseLong(valStr);
				lit.setValue(valInt);
			} else if (varType.isReal()) {
				// Create literal
				LiteralReal lit = new LiteralReal(varInit, null);
				literal = lit;

				// Set literal value
				double valReal = Double.parseDouble(valStr);
				lit.setValue(valReal);
			} else if (varType.isString()) {
				// Create literal
				LiteralString lit = new LiteralString(varInit, null);
				literal = lit;

				// Set literal value
				if (valStr == null) valStr = ""; // We should never have 'null' values
				lit.setValue(valStr);
			} else throw new RuntimeException("Cannot convert command line argument to variable type '" + varType + "'");

			// Set varInit to literal
			varInit.setExpression(literal);
		} catch (Exception e) {
			// Error parsing 'val'?
			throw new RuntimeException("Cannot convert argument '" + valStr + "' to type " + varType);
		}

		return usedVal;
	}

	/**
	 * Add symbols to global scope
	 */
	void initilaizeGlobalScope() {
		if (debug) log("Initialize global scope.");

		// Reset Global scope
		Scope.resetGlobalScope();
		Scope globalScope = Scope.getGlobalScope();

		//--
		// Get default veluas from command line or config file
		//---

		// Command line parameters override defaults
		String cpusStr = config.getString(ExpressionTask.TASK_OPTION_CPUS, "1"); // Default number of cpus: 1
		long cpus = Gpr.parseIntSafe(cpusStr);
		if (cpus <= 0) throw new RuntimeException("Number of cpus must be a positive number ('" + cpusStr + "')");

		long mem = Gpr.parseMemSafe(config.getString(ExpressionTask.TASK_OPTION_MEM, "-1")); // Default amount of memory: -1 (unrestricted)
		String node = config.getString(ExpressionTask.TASK_OPTION_NODE, "");
		if (queue == null) queue = config.getString(ExpressionTask.TASK_OPTION_QUEUE, "");
		if (system == null) system = config.getString(ExpressionTask.TASK_OPTION_SYSTEM, ExecutionerType.LOCAL.toString().toLowerCase());
		if (taskFailCount < 0) taskFailCount = Gpr.parseIntSafe(config.getString(ExpressionTask.TASK_OPTION_RETRY, "0"));

		long oneDay = 1L * 24 * 60 * 60;
		long timeout = Gpr.parseLongSafe(config.getString(ExpressionTask.TASK_OPTION_TIMEOUT, "" + oneDay));
		long wallTimeout = Gpr.parseLongSafe(config.getString(ExpressionTask.TASK_OPTION_WALL_TIMEOUT, "" + oneDay));

		// ---
		// Add global symbols
		// ---
		globalScope.add(new ScopeSymbol(Scope.GLOBAL_VAR_PROGRAM_NAME, Type.STRING, "")); // Now is empty, but they are assigned later
		globalScope.add(new ScopeSymbol(Scope.GLOBAL_VAR_PROGRAM_PATH, Type.STRING, ""));

		// Task related variables: Default values
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_SYSTEM, Type.STRING, system)); // System type: "local", "ssh", "cluster", "aws", etc.
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_CPUS, Type.INT, cpus)); // Default number of cpus
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_MEM, Type.INT, mem)); // Default amount of memory (unrestricted)
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_QUEUE, Type.STRING, queue)); // Default queue: none
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_NODE, Type.STRING, node)); // Default node: none
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_CAN_FAIL, Type.BOOL, false)); // Task fail triggers checkpoint & exit (a task cannot fail)
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_ALLOW_EMPTY, Type.BOOL, false)); // Tasks are allowed to have empty output file/s
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_RETRY, Type.INT, (long) taskFailCount)); // Task fail can be re-tried (re-run) N times before considering failed.
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_TIMEOUT, Type.INT, timeout)); // Task default timeout
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_WALL_TIMEOUT, Type.INT, wallTimeout)); // Task default wall-timeout

		// Number of local CPUs
		// Kilo, Mega, Giga, Tera, Peta.
		LinkedList<ScopeSymbol> constants = new LinkedList<ScopeSymbol>();
		constants.add(new ScopeSymbol(Scope.GLOBAL_VAR_LOCAL_CPUS, Type.INT, (long) Gpr.NUM_CORES));
		constants.add(new ScopeSymbol(Scope.GLOBAL_VAR_K, Type.INT, 1024L));
		constants.add(new ScopeSymbol(Scope.GLOBAL_VAR_M, Type.INT, 1024L * 1024L));
		constants.add(new ScopeSymbol(Scope.GLOBAL_VAR_G, Type.INT, 1024L * 1024L * 1024L));
		constants.add(new ScopeSymbol(Scope.GLOBAL_VAR_T, Type.INT, 1024L * 1024L * 1024L * 1024L));
		constants.add(new ScopeSymbol(Scope.GLOBAL_VAR_P, Type.INT, 1024L * 1024L * 1024L * 1024L * 1024L));
		constants.add(new ScopeSymbol(Scope.GLOBAL_VAR_MINUTE, Type.INT, 60L));
		constants.add(new ScopeSymbol(Scope.GLOBAL_VAR_HOUR, Type.INT, (long) (60 * 60)));
		constants.add(new ScopeSymbol(Scope.GLOBAL_VAR_DAY, Type.INT, (long) (24 * 60 * 60)));
		constants.add(new ScopeSymbol(Scope.GLOBAL_VAR_WEEK, Type.INT, (long) (7 * 24 * 60 * 60)));

		// Math constants
		constants.add(new ScopeSymbol(Scope.GLOBAL_VAR_E, Type.REAL, Math.E));
		constants.add(new ScopeSymbol(Scope.GLOBAL_VAR_PI, Type.REAL, Math.PI));

		// Add all constants
		for (ScopeSymbol ss : constants) {
			ss.setConstant(true);
			globalScope.add(ss);
		}

		// Set "physical" path
		String path;
		try {
			path = new File(".").getCanonicalPath();
		} catch (IOException e) {
			throw new RuntimeException("Cannot get cannonical path for current dir");
		}
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_PHYSICAL_PATH, Type.STRING, path));

		// Set all environment variables
		Map<String, String> envMap = System.getenv();
		for (String varName : envMap.keySet()) {
			String varVal = envMap.get(varName);
			globalScope.add(new ScopeSymbol(varName, Type.STRING, varVal));
		}

		// Command line arguments (default: empty list)
		// This is properly set in 'initializeArgs()' method, but
		// we have to set something now, otherwise we'll get a "variable
		// not found" error at compiler time, if the program attempts
		// to use 'args'.
		Scope.getGlobalScope().add(new ScopeSymbol(Scope.GLOBAL_VAR_ARGS_LIST, TypeList.get(Type.STRING), new ArrayList<String>()));
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

	void log(String msg) {
		Timer.showStdErr(getClass().getSimpleName() + ": " + msg);
	}

	/**
	 * Parse command line arguments
	 *
	 * @param args
	 */
	public void parse(String[] args) {
		// Nothing? Show command line options
		if (args.length <= 0) usage(null);

		programArgs = new ArrayList<String>();
		bigDataScriptAction = BigDataScriptAction.RUN;

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
					reportHtml = reportYaml = false;
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
					bigDataScriptAction = BigDataScriptAction.INFO_CHECKPOINT;
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
					bigDataScriptAction = BigDataScriptAction.RUN_CHECKPOINT;
					break;

				case "-reporthtml":
					reportHtml = true;
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
					bigDataScriptAction = BigDataScriptAction.TEST;
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
					verbose = true;
					break;

				case "-version":
					System.out.println(VERSION);
					System.exit(0);
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
			// OK: Nothing to chek
		} else if ((programFileName == null) && (chekcpointRestoreFile == null)) {
			// No file name => Error
			usage("Missing program file name.");
		}
	}

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
		switch (bigDataScriptAction) {
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
			exitValue = runCompile(); // Compile & run
		}
		if (verbose) Timer.showStdErr("Finished. Exit code: " + exitValue);

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
		BdsSerializer bdsSerializer = new BdsSerializer(chekcpointRestoreFile, config);
		List<BdsThread> bdsThreads = bdsSerializer.load();

		// Set main thread's programUnit running scope (mostly for debugging and test cases)
		// ProgramUnit's scope it the one before 'global'
		BdsThread mainThread = bdsThreads.get(0);
		programUnit = mainThread.getProgramUnit();

		// Set state and recover tasks
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
	 * Compile and run
	 */
	int runCompile() {
		// Compile, abort on errors
		if (verbose) Timer.showStdErr("Parsing");
		if (!compile()) {
			// Show errors and warnings, if any
			if (!CompilerMessages.get().isEmpty()) System.err.println("Compiler messages:\n" + CompilerMessages.get());
			return 1;
		}

		if (verbose) Timer.showStdErr("Initializing");
		initializeArgs();

		// Run the program
		BdsThread bdsThread = new BdsThread(programUnit, config);
		if (verbose) Timer.showStdErr("Process ID: " + bdsThread.getBdsThreadId());

		// Show script's automatic help message
		if (showHelp) {
			if (verbose) Timer.showStdErr("Showing automaic 'help'");
			HelpCreator hc = new HelpCreator(programUnit);
			System.out.println(hc);
			return 0;
		}

		if (verbose) Timer.showStdErr("Running");
		int exitCode = runThread(bdsThread);

		// Check stack
		if (stackCheck) bdsThread.sanityCheckStack();

		return exitCode;
	}

	/**
	 * Compile and run
	 */
	int runTests() {
		// Compile, abort on errors
		if (verbose) Timer.showStdErr("Parsing");
		if (!compile()) {
			// Show errors and warnings, if any
			if (!CompilerMessages.get().isEmpty()) System.err.println("Compiler messages:\n" + CompilerMessages.get());
			return 1;
		}

		if (verbose) Timer.showStdErr("Initializing");
		initializeArgs();

		// Run the program
		BdsThread bdsThread = new BdsThread(programUnit, config);
		if (verbose) Timer.showStdErr("Process ID: " + bdsThread.getBdsThreadId());

		if (verbose) Timer.showStdErr("Running tests");
		ProgramUnit pu = bdsThread.getProgramUnit();
		List<FunctionDeclaration> testFuncs = pu.testsFunctions();

		// For each test function, create a thread that executes the function's body
		int exitCode = 0;
		int testOk = 0, testError = 0;
		for (FunctionDeclaration testFunc : testFuncs) {
			System.out.println("");
			BdsThread bdsTestThread = new BdsThread(testFunc.getStatement(), bdsThread); // Note: We execute the function's body (not the function declaration)
			int exitValTest = runThread(bdsTestThread);

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
			if (verbose) System.err.println("Remote file is up to date, no upload required: " + url);
			return true;
		}

		return remote.upload(fileName);
	}

	void usage(String err) {
		if (err != null) System.err.println("Error: " + err);

		System.out.println(VERSION + "\n");
		System.err.println("Usage: " + BigDataScript.class.getSimpleName() + " [options] file.bds");
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
		System.err.println("  -noReport                      : Do not create any report (neither HTML nor YAML).");
		System.err.println("  -noReportHtml                  : Do not create HTML report.");
		System.err.println("  -noRmOnExit                    : Do not remove files marked for deletion on exit (rmOnExit). Default: " + noRmOnExit);
		System.err.println("  [-q | -queue  ] queueName      : Set default queue name.");
		System.err.println("  -quiet                         : Do not show any messages or tasks outputs on STDOUT. Default: " + quiet);
		System.err.println("  -reportHtml                    : Create HTML report. Default: " + reportHtml);
		System.err.println("  -reportYaml                    : Create YAML report. Default: " + reportYaml);
		System.err.println("  [-r | -restore] checkpoint.chp : Restore state from checkpoint file.");
		System.err.println("  [-s | -system ] type           : Set system type.");
		System.err.println("  [-t | -test   ]                : Run user test cases (runs all test* functions).");
		System.err.println("  -upload file url               : Upload local file to 'url'. Note: Used by 'taks'");
		System.err.println("  [-v | -verbose]                : Be verbose.");
		System.err.println("  -version                       : Show version and exit.");
		System.err.println("  [-y | -retry  ] num            : Number of times to retry a failing tasks.");
		System.err.println("  -pid <file>                    : Write local processes PIDs to 'file'");

		if (err != null) System.exit(1);
		System.exit(0);
	}

}
