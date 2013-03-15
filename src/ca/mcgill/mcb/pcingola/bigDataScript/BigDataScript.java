package ca.mcgill.mcb.pcingola.bigDataScript;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.Tree;

import ca.mcgill.mcb.pcingola.bigDataScript.antlr.BigDataScriptLexer;
import ca.mcgill.mcb.pcingola.bigDataScript.antlr.BigDataScriptParser;
import ca.mcgill.mcb.pcingola.bigDataScript.exec.Executioner;
import ca.mcgill.mcb.pcingola.bigDataScript.exec.Executioners;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.BigDataScriptNodeFactory;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.ExpressionTask;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Literal;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.LiteralBool;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.LiteralInt;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.LiteralReal;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.LiteralString;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.ProgramUnit;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Statement;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Type;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.TypeList;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.VarDeclaration;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.VariableInit;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeFunctions.NativeLibraryFunctions;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.MethodNativeZzz;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.nativeMethods.NativeLibraryString;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.RunState;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.ScopeSymbol;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerializer;
import ca.mcgill.mcb.pcingola.bigDataScript.util.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * BigDataScript command line parser
 * 
 * TODO: Command line to see variables, scopes and stack-trace for a checkpoint
 * 
 * @author pcingola
 */
public class BigDataScript {

	public static final String SOFTWARE_NAME = BigDataScript.class.getSimpleName();
	public static final String BUILD = "2013-02-08";
	public static final String REVISION = "";
	public static final String VERSION_MAJOR = "0.7";
	public static final String VERSION_SHORT = VERSION_MAJOR + REVISION;
	public static final String VERSION = SOFTWARE_NAME + " " + VERSION_SHORT + " (build " + BUILD + "), by " + Pcingola.BY;

	boolean verbose;
	boolean debug;
	boolean log;
	String configFile = Config.DEFAULT_CONFIG_FILE; // Config file
	String chekcpointRestoreFile; // Restore file
	String programFileName; // Program file name
	Config config;
	ProgramUnit programUnit; // Program (parsed nodes)
	BigDataScriptThread bigDataScriptThread;
	ArrayList<String> programArgs; // Command line arguments for BigDataScript program

	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {
		// Create BigDataScript object and run it
		BigDataScript bigDataScript = new BigDataScript(args);
		bigDataScript.run();
		System.exit(0);
	}

	public BigDataScript(String args[]) {
		parse(args);
		initilaize();
	}

	/**
	 * Compile program
	 */
	public boolean compile() {
		if (debug) System.out.println("Loading file: '" + programFileName + "'");

		//---
		// Convert to AST / csTree
		//---
		if (debug) System.out.println("Creating AST.");
		ParseTree tree = createAst();

		if (debug) System.out.println("Creating BigDataScript tree.");
		CompilerMessages.reset();
		CompilerMessages.setFileName(programFileName);
		programUnit = (ProgramUnit) BigDataScriptNodeFactory.get().factory(null, tree); // Transform AST to BigDataScript tree

		// Any error messages?
		if (!CompilerMessages.get().isEmpty()) System.err.println("Compiler messages:\n" + CompilerMessages.get());
		if (CompilerMessages.get().hasErrors()) return false;

		//---
		// Type-checking
		//---
		if (debug) System.out.println("Type checking.");
		Scope programScope = new Scope();
		programUnit.typeChecking(programScope, CompilerMessages.get());

		// Any error messages?
		if (!CompilerMessages.get().isEmpty()) System.err.println("Compiler messages:\n" + CompilerMessages.get());
		if (CompilerMessages.get().hasErrors()) return false;

		// OK
		return true;
	}

	/**
	 * Create an AST from a program (using ANTLR lexer & parser)
	 */
	ParseTree createAst() {
		try {
			// Input stream
			FileInputStream fis = new FileInputStream(programFileName);

			// Create a CharStream that reads from standard input
			ANTLRInputStream input = new ANTLRInputStream(fis);

			// Create a lexer that feeds off of input CharStream
			BigDataScriptLexer lexer = new BigDataScriptLexer(input);

			CommonTokenStream tokens = new CommonTokenStream(lexer);
			BigDataScriptParser parser = new BigDataScriptParser(tokens);
			ParseTree tree = parser.programUnit(); // Begin parsing at main rule

			// Error loading file?
			if (tree == null) {
				System.err.println("Can't parse file '" + programFileName + "'.");
				return null;
			}

			// Show main nodes
			if (debug) {
				for (int childNum = 0; childNum < tree.getChildCount(); childNum++) {
					Tree child = tree.getChild(childNum);
					System.out.println("\tChild " + childNum + ":\t" + child + "\tTree:'" + child.toStringTree() + "'");
				}
			}
			return tree;
		} catch (Exception e) {
			throw new RuntimeException("Error parsing input.", e);
		}
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
	 * Get default settings
	 */
	void initDefaults() {
		log = true;
	}

	/**
	 * Set command line arguments as global variables
	 * 
	 * How it works: 
	 * 		- Program is executes as something like:
	 * 
	 * 				java -jar BigDataScript.jar [options] programFile.bds [programOptions]
	 * 
	 * 		- Any command line argument AFTER "programFile.bds" is considered a command 
	 * 		  line argument for the BigDataScript program. E.g.
	 * 
	 * 				java -jar BigDataScript.jar -v program.bds -file myFile.txt -verbose -num 7
	 * 
	 * 			So our program "program.bds" has command line options: -file myFile.txt -verbose -num 7
	 * 			(notice that "-v" is a command line option for loudScript.jar and not for "program.bds")
	 * 
	 * 		- We look for variables in ProgramUnit that match the name of these command line arguments
	 * 
	 * 		- Then we add those values to the variable initialization. Thus overriding any 
	 * 		  initialization values provided in the program. 
	 * 		  E.g.
	 * 
	 *  		Our program has the following variable declarations:
	 *  			string file = "default_file.txt"
	 *  			int num = 3
	 *  			bool verbose = false
	 *  
	 *  		We execute the program:
	 *  			java -jar BigDataScript.jar -v program.bds -file myFile.txt -verbose -num 7
	 *  
	 *  		The variable declarations are replaced as follows:
	 *  			string file = "myFile.txt"
	 *  			int num = 7
	 *  			bool verbose = true
	 *  			
	 *  	- Note: Only primitive types are supported (i.e.: string, bool, int & real)
	 *  
	 *  	- Note: Unmatched variables names will be silently ignored (same for variables that match, but are non-primitive)
	 *  
	 *  	- Note: If a variable is matched, is primitive, but cannot be converted. An error is thrown.
	 *  			E.g.:
	 *  				Program:
	 *  					int num = 1
	 *  
	 *  				Command line:
	 *  					java -jar BigDataScript.jar program.bds -num "hello"		<- This is an error because 'num' is an int
	 *  
	 *  	- Note: Unprocessed arguments will be available to the program as an 'args' list
	 */
	void initializeArgs() {
		ArrayList<String> args = new ArrayList<String>();

		// Set program arguments as global variables
		for (int argNum = 0; argNum < programArgs.size(); argNum++) {
			String arg = programArgs.get(argNum);

			boolean usedArgument = false;

			// Parse '-XXX' option
			if (arg.startsWith("-")) {
				// Get variable name and value
				String varName = arg.substring(1);
				String val = "";
				if (argNum < programArgs.size()) val = programArgs.get(++argNum);

				// Find all variable declarations that match this command line argument
				for (Statement s : programUnit.getStatements()) {
					// Is it a variable declaration?
					if (s instanceof VarDeclaration) {
						VarDeclaration varDecl = (VarDeclaration) s;
						Type varType = varDecl.getType();

						// We can only set primitive types
						if (varType.isPrimitiveType()) {
							for (VariableInit varInit : varDecl.getVarInit())
								if (varInit.getVarName().equals(varName)) {
									usedArgument = initializeArgs(varType, varInit, val); // Found variable, try to replace or add LITERAL to this VarInit
									if (!usedArgument) arg = val; // We did not use this argument
								}
						}
					}
				}
			}

			// Argument not used? Add to 'args' 
			if (!usedArgument) args.add(arg);
		}

		// Make all unprocessed arguments available for the program (in 'args' list) 
		Scope.getGlobalScope().add(new ScopeSymbol(Scope.VAR_ARGS_LIST, TypeList.get(Type.STRING), args));

		// Initialie program name
		String progName = Gpr.baseName(programUnit.getFileName());
		Scope.getGlobalScope().add(new ScopeSymbol(Scope.VAR_PROGRAM_NAME, Type.STRING, progName));
	}

	/**
	 * Add or replace initialization statement in this VarInit
	 * 
	 * Note: We create a Literal node (of the appropriate type) and add it to "varInit.expression" 
	 * 
	 * @param varType : Variable type
	 * @param varInit : Variable initialization
	 * @param val : Value to assign
	 */
	boolean initializeArgs(Type varType, VariableInit varInit, String val) {
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
				if (val != null) {
					// Parse boolean 
					val = val.toLowerCase();
					if (val.equals("true") || val.equals("t")) valBool = true;
					else if (val.equals("false") || val.equals("f")) valBool = false;
					else usedVal = false; // Not any valid value? => This argument is not used
				}

				lit.setValue(valBool);
			} else if (varType.isInt()) {
				// Create literal
				LiteralInt lit = new LiteralInt(varInit, null);
				literal = lit;

				// Set literal value
				long valInt = Long.parseLong(val);
				lit.setValue(valInt);
			} else if (varType.isReal()) {
				// Create literal
				LiteralReal lit = new LiteralReal(varInit, null);
				literal = lit;

				// Set literal value
				double valReal = Double.parseDouble(val);
				lit.setValue(valReal);
			} else if (varType.isString()) {
				// Create literal
				LiteralString lit = new LiteralString(varInit, null);
				literal = lit;

				// Set literal value
				if (val == null) val = ""; // We should never have 'null' values
				lit.setValue(val);
			} else throw new RuntimeException("Cannot convert command line argument to variable type '" + varType + "'");

			// Set varInit to literal
			varInit.setExpression(literal);
		} catch (Exception e) {
			// Error parsing 'val'?
			throw new RuntimeException("Cannot convert argument '" + val + "' to type " + varType);
		}

		return usedVal;
	}

	/**
	 * Initialize before running or type-checking
	 */
	void initilaize() {
		//---
		// This is just to make sure we create primitive Types first
		// This is important for serialization process
		//---
		@SuppressWarnings("unused")
		Type type = Type.INT;
		Type.reset();

		// Reset node factory
		BigDataScriptNodeFactory.reset();

		// Global scope
		initilaizeGlobalScope();

		// Libraries
		initilaizeLibraries();
	}

	/**
	 * Add symbols to global scope
	 */
	void initilaizeGlobalScope() {
		if (debug) System.out.println("Initialize global scope.");

		// Reset Global scope
		Scope.resetGlobalScope();
		Scope globalScope = Scope.getGlobalScope();

		//---
		// Add global symbols
		//---
		globalScope.add(new ScopeSymbol(Scope.VAR_PROGRAM_NAME, Type.STRING, "")); // Task fail triggers checkpoint & exit (a task cannot fail)

		// Task related variables: Default values
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_SYSTEM, Type.STRING, "local")); // System type: "local", "ssh", "cluster", "aws", etc.
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_CPUS, Type.INT, 1L)); // Default number of cpus
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_CPUS_LOCAL, Type.INT, Gpr.NUM_CORES)); // Default number of local cpus
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_QUEUE, Type.STRING, "")); // Default queue: none
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_NODE, Type.STRING, "")); // Default node: none
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_CAN_FAIL, Type.BOOL, false)); // Task fail triggers checkpoint & exit (a task cannot fail)
		globalScope.add(new ScopeSymbol(ExpressionTask.TASK_OPTION_TIMEOUT, Type.INT, 0L)); // Task fail triggers checkpoint & exit (a task cannot fail)

		// Number of local CPUs
		globalScope.add(new ScopeSymbol(Scope.GLOBAL_VAR_LOCAL_CPUS, Type.INT, (long) Gpr.NUM_CORES));

		// Kile, Mega, Giga, Tera, Peta.
		globalScope.add(new ScopeSymbol(Scope.GLOBAL_VAR_K, Type.INT, 1024L));
		globalScope.add(new ScopeSymbol(Scope.GLOBAL_VAR_M, Type.INT, 1024L * 1024L));
		globalScope.add(new ScopeSymbol(Scope.GLOBAL_VAR_G, Type.INT, 1024L * 1024L * 1024L));
		globalScope.add(new ScopeSymbol(Scope.GLOBAL_VAR_T, Type.INT, 1024L * 1024L * 1024L * 1024L));
		globalScope.add(new ScopeSymbol(Scope.GLOBAL_VAR_P, Type.INT, 1024L * 1024L * 1024L * 1024L * 1024L));

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
		Scope.getGlobalScope().add(new ScopeSymbol(Scope.VAR_ARGS_LIST, TypeList.get(Type.STRING), new ArrayList<String>()));
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

		// Load test native methods
		MethodNativeZzz nmz = new MethodNativeZzz();
		if (debug) Timer.showStdErr("Native test method:\n" + nmz.signature());
	}

	/**
	 * Parse command line arguments
	 * @param args
	 */
	public void parse(String[] args) {
		// Nothing? Show command line options
		if (args.length <= 0) usage(null);

		programArgs = new ArrayList<String>();

		for (int i = 0; i < args.length; i++) {
			if (programFileName != null) programArgs.add(args[i]); // Everything after 'programFileName' is an command line argument for the BigDataScript program
			else if (args[i].equalsIgnoreCase("-noLog")) log = false;
			else if (args[i].equals("-v") || args[i].equalsIgnoreCase("-verbose")) verbose = true;
			else if (args[i].equals("-d") || args[i].equalsIgnoreCase("-debug")) debug = true;
			else if (args[i].equals("-r") || args[i].equalsIgnoreCase("-restore")) {
				// Checkpoint restore
				if ((i + 1) < args.length) chekcpointRestoreFile = args[++i];
				else usage("Option '-r' without restore file argument");

			} else if (args[i].equals("-c") || args[i].equalsIgnoreCase("-config")) {
				// Checkpoint restore
				if ((i + 1) < args.length) configFile = args[++i];
				else usage("Option '-c' without restore file argument");

			} else if (programFileName == null) programFileName = args[i]; // Get program file name
		}

		// Sanity checks
		if ((programFileName == null) && (chekcpointRestoreFile == null)) usage("Missing program file name");
	}

	/**
	 * Run script
	 */
	public void run() {
		// Startup message
		if (verbose) System.out.println(VERSION + "\n");

		//---
		// Config
		//---
		config = new Config(configFile, verbose);
		config.setDebug(debug);
		Executioners executioners = Executioners.getInstance(config); // Initialize executioners

		//---
		// Run
		//---
		if (chekcpointRestoreFile != null) runCheckpoint(); // Are we recovering (or loading) from a checkpoint?
		else runCompile(); // Compile & run

		//---
		// Kill all Executioners
		//---
		for (Executioner executioner : executioners.getAll())
			executioner.kill();

		if (debug) System.out.println("Finished running.\n");
	}

	/**
	 * Restore from checkpoint and run
	 */
	void runCheckpoint() {
		// Load checkpoint file
		BigDataScriptSerializer csSerializer = new BigDataScriptSerializer(chekcpointRestoreFile);
		List<BigDataScriptThread> csthreads = csSerializer.load();

		// Show
		for (BigDataScriptThread csthread : csthreads) {
			//			if (debug) System.err.println("Checkpoint data:\n" + csthread);

			// Set run state, program 
			csthread.setRunState(RunState.CHECKPOINT_RECOVER);
			programUnit = (ProgramUnit) csthread.getProgramUnit();

			// Set programUnit's scope (mostly for debugging and test cases)
			// ProgramUnit's scope it the one before 'global'
			for (Scope scope = csthread.getScope(); (scope != null) && (scope.getParent() != Scope.getGlobalScope()); scope = scope.getParent())
				programUnit.setScope(scope);

			// All set, run thread
			runThread(csthread);
		}
	}

	/**
	 * Compile and run
	 */
	void runCompile() {
		// Compile, abort on errors
		if (!compile()) return;

		initializeArgs();

		// Run the program
		BigDataScriptThread csthread = new BigDataScriptThread(programUnit);
		runThread(csthread);
	}

	/**
	 * Run a thread
	 * @param csthread
	 */
	void runThread(BigDataScriptThread csthread) {
		bigDataScriptThread = csthread;
		csthread.start();

		try {
			csthread.join();
		} catch (InterruptedException e) {
			// Nothing to do?
			// May be checkpoint?
			Gpr.debug("Interrupted!");
		}
	}

	void usage(String err) {
		if (err != null) System.err.println("Error: " + err);

		System.out.println(VERSION + "\n");
		System.err.println("Usage: " + BigDataScript.class.getSimpleName() + " [options] file.bds");
		System.err.println("\nAvailable options: ");
		System.err.println("  [-c | -config] file    : Config file. Default : " + configFile);
		System.err.println("  [-d | -debug]          : Debug mode.");
		System.err.println("  -noLog                 : Do not log stats.");
		System.err.println("  [-r | -restore] file   : Restore from checkpoint file.");
		System.err.println("  [-v | -verbose]        : Be verbose.");
		if (err != null) System.exit(1);
		System.exit(0);
	}

}
