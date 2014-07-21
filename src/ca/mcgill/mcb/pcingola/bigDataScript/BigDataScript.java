package ca.mcgill.mcb.pcingola.bigDataScript;

import java.io.File;
import java.io.IOException;
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
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.TypeCheckedNodes;
import ca.mcgill.mcb.pcingola.bigDataScript.executioner.Executioner;
import ca.mcgill.mcb.pcingola.bigDataScript.executioner.Executioners;
import ca.mcgill.mcb.pcingola.bigDataScript.executioner.Executioners.ExecutionerType;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.BigDataScriptNodeFactory;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.ExpressionTask;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Literal;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.LiteralBool;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.LiteralInt;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.LiteralListString;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.LiteralReal;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.LiteralString;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.ProgramUnit;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Statement;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.StatementInclude;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeList;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.VarDeclaration;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.VariableInit;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions.NativeLibraryFunctions;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.NativeLibraryString;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.RunState;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.ScopeSymbol;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerializer;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * BigDataScript command line parser
 *
 * @author pcingola
 */
public class BigDataScript {

	enum BigDataScriptAction {
		RUN, RUN_CHECKPOINT, INFO_CHECKPOINT
	}

	public static final String SOFTWARE_NAME = BigDataScript.class.getSimpleName();
	public static final String BUILD = "2014-07-16";
	public static final String REVISION = "b";
	public static final String VERSION_MAJOR = "0.98";
	public static final String VERSION_SHORT = VERSION_MAJOR + REVISION;

	public static final String VERSION = SOFTWARE_NAME + " " + VERSION_SHORT + " (build " + BUILD + "), by " + Pcingola.BY;

	boolean verbose;
	boolean debug;
	boolean log;
	boolean dryRun;
	boolean noRmOnExit;
	int taskFailCount = 0;
	String configFile = Config.DEFAULT_CONFIG_FILE; // Config file
	String chekcpointRestoreFile; // Restore file
	String programFileName; // Program file name
	String pidFile; // File to store PIDs
	String system; // System type
	String queue; // Queue name
	BigDataScriptAction bigDataScriptAction;
	Config config;
	ProgramUnit programUnit; // Program (parsed nodes)
	BigDataScriptThread bigDataScriptThread;
	ArrayList<String> programArgs; // Command line arguments for BigDataScript program

	/**
	 * Create an AST from a program (using ANTLR lexer & parser)
	 * Returns null if error
	 * Use 'alreadyIncluded' to keep track of from 'include' statements
	 */
	public static ParseTree createAst(File file, boolean debug, Set<File> alreadyIncluded) {
		alreadyIncluded.add(Gpr.getCanonicalFile(file));
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

			// Create a lexer that feeds off of input CharStream
			lexer = new BigDataScriptLexer(input) {
				@Override
				public void recover(LexerNoViableAltException e) {
					throw new RuntimeException(e); // Bail out
				}
			};

			CommonTokenStream tokens = new CommonTokenStream(lexer);
			parser = new BigDataScriptParser(tokens);
			parser.setErrorHandler(new CompileErrorStrategy()); // bail out with exception if errors in parser

			ParseTree tree = parser.programUnit(); // Begin parsing at main rule

			// Error loading file?
			if (tree == null) {
				System.err.println("Can't parse file '" + filePath + "'");
				return null;
			}

			// Show main nodes
			if (debug) {
				for (int childNum = 0; childNum < tree.getChildCount(); childNum++) {
					Tree child = tree.getChild(childNum);
					System.out.println("\tChild " + childNum + ":\t" + child + "\tTree:'" + child.toStringTree() + "'");
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
	private static boolean resolveIncludes(ParseTree tree, boolean debug, Set<File> alreadyIncluded) {
		boolean changed = false;
		if (tree instanceof IncludeFileContext) {
			// Parent file: The one that is including the other file
			File parentFile = new File(((IncludeFileContext) tree).getStart().getInputStream().getSourceName());

			// Included file name
			String includedFilename = StatementInclude.includedFileName(tree.getChild(1).getText());

			// Find file (look into all include paths)
			File includedFile = StatementInclude.includedFile(includedFilename, parentFile);
			if (includedFile == null) {
				CompilerMessages.get().add(tree, parentFile, "\n\tIncluded file not found: '" + includedFilename + "'\n\tSearch path: " + Config.get().getIncludePath(), MessageType.ERROR);
				return false;
			}

			// Already included? don't bother
			if (alreadyIncluded.contains(Gpr.getCanonicalFile(includedFile))) return false;
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
		parse(args);
		initialize();
	}

	/**
	 * Compile program
	 */
	public boolean compile() {
		if (debug) System.out.println("Loading file: '" + programFileName + "'");

		//---
		// Convert to AST
		//---
		if (debug) System.out.println("Creating AST.");
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
		if (debug) System.out.println("Creating BigDataScript tree.");
		CompilerMessages.reset();
		programUnit = (ProgramUnit) BigDataScriptNodeFactory.get().factory(null, tree); // Transform AST to BigDataScript tree
		if (debug) System.err.println("AST:\n" + programUnit.toString());
		// Any error messages?
		if (!CompilerMessages.get().isEmpty()) System.err.println("Compiler messages:\n" + CompilerMessages.get());
		if (CompilerMessages.get().hasErrors()) return false;

		//---
		// Type-checking
		//---
		if (debug) System.out.println("Type checking.");
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
	 * Create an AST from a program file
	 * @return A parsed tree
	 */
	ParseTree createAst() {
		File file = new File(programFileName);
		return createAst(file, debug, new HashSet<File>());
	}

	public BigDataScriptThread getBigDataScriptThread() {
		return bigDataScriptThread;
	}

	public CompilerMessages getCompilerMessages() {
		return CompilerMessages.get();
	}

	public ProgramUnit getProgramUnit() {
		return programUnit;
	}

	/**
	 * Show information from a checkpoint file
	 * @return
	 */
	int infoCheckpoint() {
		// Load checkpoint file
		BigDataScriptSerializer csSerializer = new BigDataScriptSerializer(chekcpointRestoreFile, config);
		List<BigDataScriptThread> bdsThreads = csSerializer.load();

		Gpr.debug("INFO CHECKPOINT:");
		for (BigDataScriptThread bdsThread : bdsThreads)
			bdsThread.print();

		return 0;
	}

	/**
	 * Get default settings
	 */
	void initDefaults() {
		log = true;
		dryRun = false;
	}

	/**
	 * Initialize before running or type-checking
	 */
	void initialize() {
		Type.reset();

		// Reset node factory
		BigDataScriptNodeFactory.reset();

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
			if (arg.startsWith("-")) {
				// Get variable name and value
				String varName = arg.substring(1);

				// Find all variable declarations that match this command line argument
				for (Statement s : programUnit.getStatements()) {
					// Is it a variable declaration?
					if (s instanceof VarDeclaration) {
						VarDeclaration varDecl = (VarDeclaration) s;
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
		if (debug) System.out.println("Initialize global scope.");

		// Reset Global scope
		Scope.resetGlobalScope();
		Scope globalScope = Scope.getGlobalScope();

		// ---
		// Add global symbols
		// ---
		globalScope.add(new ScopeSymbol(Scope.GLOBAL_VAR_PROGRAM_NAME, Type.STRING, ""));
		globalScope.add(new ScopeSymbol(Scope.GLOBAL_VAR_PROGRAM_PATH, Type.STRING, ""));

		// Command line parameters override defaults
		if (system == null) system = ExecutionerType.LOCAL.toString().toLowerCase();
		if (queue == null) queue = "";

		// Task related variables: Default values
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_SYSTEM, Type.STRING, system)); // System type: "local", "ssh", "cluster", "aws", etc.
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_CPUS, Type.INT, 1L)); // Default number of cpus
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_MEM, Type.INT, -1L)); // Default amount of memory (unrestricted)
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_QUEUE, Type.STRING, queue)); // Default queue: none
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_NODE, Type.STRING, "")); // Default node: none
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_CAN_FAIL, Type.BOOL, false)); // Task fail triggers checkpoint & exit (a task cannot fail)
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_RETRY, Type.INT, (long) taskFailCount)); // Task fail can be re-tried (re-run) N times before considering failed.
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_TIMEOUT, Type.INT, 1L * 24 * 60 * 60)); // Task default timeout(1 day)
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_WALL_TIMEOUT, Type.INT, 1L * 24 * 60 * 60)); // Task default wall-timeout(1 day)

		// Number of local CPUs
		// Kilo, Mega, Giga, Tera, Peta.
		LinkedList<ScopeSymbol> constants = new LinkedList<ScopeSymbol>();
		constants.add(new ScopeSymbol(Scope.GLOBAL_VAR_PROGRAM_NAME, Type.STRING, "")); // Program name, now is empty, but it is filled later
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
		if (debug) System.out.println("Initialize standard libraries.");

		// Native functions
		NativeLibraryFunctions nativeLibraryFunctions = new NativeLibraryFunctions();
		if (debug) Timer.showStdErr("Native library:\n" + nativeLibraryFunctions);

		// Native library: String
		NativeLibraryString nativeLibraryString = new NativeLibraryString();
		if (debug) Timer.showStdErr("Native library:\n" + nativeLibraryString);
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
			if (programFileName != null) programArgs.add(args[i]); // Everything after 'programFileName' is an command line argument for the BigDataScript program
			else if (args[i].equals("-c") || args[i].equalsIgnoreCase("-config")) {
				// Checkpoint restore
				if ((i + 1) < args.length) configFile = args[++i];
				else usage("Option '-c' without restore file argument");
			} else if (args[i].equals("-d") || args[i].equalsIgnoreCase("-debug")) debug = verbose = true; // Debug implies verbose
			else if (args[i].equals("-l") || args[i].equalsIgnoreCase("-log")) log = true;
			else if (args[i].equals("-h") || args[i].equalsIgnoreCase("-help") || args[i].equalsIgnoreCase("--help")) usage(null);
			else if (args[i].equalsIgnoreCase("-dryRun")) {
				dryRun = true;
				noRmOnExit = true; // Not running, so don't delete files
			} else if (args[i].equalsIgnoreCase("-noRmOnExit")) noRmOnExit = true;
			else if (args[i].equals("-i") || args[i].equalsIgnoreCase("-info")) {
				// Checkpoint info
				if ((i + 1) < args.length) chekcpointRestoreFile = args[++i];
				else usage("Option '-i' without checkpoint file argument");
				bigDataScriptAction = BigDataScriptAction.INFO_CHECKPOINT;
			} else if (args[i].equals("-pid")) {
				// PID file
				if ((i + 1) < args.length) pidFile = args[++i];
				else usage("Option '-pid' without file argument");
			} else if (args[i].equals("-q") || args[i].equalsIgnoreCase("-queue")) {
				// Queue name
				if ((i + 1) < args.length) queue = args[++i];
				else usage("Option '-queue' without file argument");
			} else if (args[i].equals("-r") || args[i].equalsIgnoreCase("-restore")) {
				// Checkpoint restore
				if ((i + 1) < args.length) chekcpointRestoreFile = args[++i];
				else usage("Option '-r' without checkpoint file argument");
				bigDataScriptAction = BigDataScriptAction.RUN_CHECKPOINT;
			} else if (args[i].equals("-s") || args[i].equalsIgnoreCase("-system")) {
				// System type
				if ((i + 1) < args.length) system = args[++i];
				else usage("Option '-system' without file argument");
			} else if (args[i].equals("-t") || args[i].equalsIgnoreCase("-retry")) {
				// Number of retries
				if ((i + 1) < args.length) taskFailCount = Gpr.parseIntSafe(args[++i]);
				else usage("Option '-t' without number argument");
			} else if (args[i].equals("-v") || args[i].equalsIgnoreCase("-verbose")) verbose = true;
			else if (programFileName == null) programFileName = args[i]; // Get program file name
		}

		// Sanity checks
		if ((programFileName == null) && (chekcpointRestoreFile == null)) usage("Missing program file name.");
	}

	/**
	 * Run script
	 */
	public int run() {
		// Startup message
		if (verbose) Timer.showStdErr(VERSION);

		// ---
		// Config
		// ---
		config = new Config(configFile);
		config.setVerbose(verbose);
		config.setDebug(debug);
		config.setLog(log);
		config.setDryRun(dryRun);
		config.setTaskFailCount(taskFailCount);
		config.setNoRmOnExit(noRmOnExit);
		if (pidFile == null) {
			if (programFileName != null) pidFile = programFileName + ".pid";
			else pidFile = chekcpointRestoreFile + ".pid";
		}
		config.setPidFile(pidFile);
		Executioners executioners = Executioners.getInstance(config); // Initialize executioners

		// ---
		// Run
		// ---
		int exitValue = 0;
		switch (bigDataScriptAction) {
		case RUN_CHECKPOINT:
			exitValue = runCheckpoint();
			break;

		case INFO_CHECKPOINT:
			exitValue = infoCheckpoint();
			break;

		default:
			exitValue = runCompile(); // Compile & run
		}
		if (verbose) Timer.showStdErr("Finished running. Exit code: " + exitValue);

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
		BigDataScriptSerializer csSerializer = new BigDataScriptSerializer(chekcpointRestoreFile, config);
		List<BigDataScriptThread> bdsThreads = csSerializer.load();

		// Show
		int exitValue = 0;
		for (BigDataScriptThread bdsThread : bdsThreads) {
			//---
			// Run (traverse tree in 'CHECKPOINT_RECOVER' mode) until we find exactly the instruction where we left
			//---

			// Set run state, program
			bdsThread.setRunState(RunState.CHECKPOINT_RECOVER);
			programUnit = (ProgramUnit) bdsThread.getProgramUnit();

			// Set programUnit's scope (mostly for debugging and test cases)
			// ProgramUnit's scope it the one before 'global'
			for (Scope scope = bdsThread.getScope(); (scope != null) && (scope.getParent() != Scope.getGlobalScope()); scope = scope.getParent())
				programUnit.setScope(scope);

			//---
			// Re-execute or add tasks
			//---
			bdsThread.restoreUnserializedTasks();

			//---
			// All set, run thread
			//---
			int exitVal = runThread(bdsThread);
			exitValue = Math.max(exitValue, exitVal);
		}

		return exitValue;
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
		BigDataScriptThread bdsThread = new BigDataScriptThread(programUnit, config);
		if (verbose) Timer.showStdErr("Process ID: " + bdsThread.getBigDataScriptThreadId());

		if (verbose) Timer.showStdErr("Running");
		int exitCode = runThread(bdsThread);

		return exitCode;
	}

	/**
	 * Run a thread
	 * @param bdsThread
	 */
	int runThread(BigDataScriptThread bdsThread) {
		bigDataScriptThread = bdsThread;
		bdsThread.start();

		try {
			bdsThread.join();
		} catch (InterruptedException e) {
			// Nothing to do?
			// May be checkpoint?
			return 1;
		}

		// Create report
		bdsThread.createReport();

		// OK, we are done
		return bdsThread.getExitValue();
	}

	void usage(String err) {
		if (err != null) System.err.println("Error: " + err);

		System.out.println(VERSION + "\n");
		System.err.println("Usage: " + BigDataScript.class.getSimpleName() + " [options] file.bds");
		System.err.println("\nAvailable options: ");
		System.err.println("  [-c | -config ] bds.config     : Config file. Default : " + configFile);
		System.err.println("  [-d | -debug  ]                : Debug mode.");
		System.err.println("  -dryRun                        : Do not run any task, just show what would be run.");
		System.err.println("  [-i | -info   ] checkpoint.chp : Show state information in checkpoint file.");
		System.err.println("  [-l | -log    ]                : Log all tasks (do not delete tmp files).");
		System.err.println("  -noRmOnExit                    : Do not remove files marked for deletion on exit (rmOnExit).");
		System.err.println("  [-q | -queue  ] queueName      : Set default queue name.");
		System.err.println("  [-r | -restore] checkpoint.chp : Restore state from checkpoint file.");
		System.err.println("  [-s | -system ] type           : Set system type.");
		System.err.println("  [-t | -reTry  ] num            : Number of times to re-try a task that failed.");
		System.err.println("  [-v | -verbose]                : Be verbose.");
		System.err.println("  -pid <file>                    : Write local processes PIDs to 'file'");
		System.err.println("  -noLog                         : Do not log stats.");

		if (err != null) System.exit(1);
		System.exit(0);
	}
}
