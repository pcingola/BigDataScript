package org.bds;

import java.util.ArrayList;

import org.bds.lang.ProgramUnit;
import org.bds.lang.statement.VarDeclaration;
import org.bds.lang.statement.VariableInit;
import org.bds.lang.type.Type;
import org.bds.lang.type.TypeList;
import org.bds.lang.type.Types;
import org.bds.lang.value.Literal;
import org.bds.lang.value.LiteralBool;
import org.bds.lang.value.LiteralInt;
import org.bds.lang.value.LiteralListString;
import org.bds.lang.value.LiteralReal;
import org.bds.lang.value.LiteralString;
import org.bds.scope.Scope;
import org.bds.scope.ScopeSymbol;
import org.bds.util.Gpr;
import org.bds.util.Timer;

/**
 * Parse command line arguments for a BDS program
 *
 * Set command line arguments as program's variables
 *
 * How it works:
 *
 * 	- Program is executes as something like:
 *
 * 		java -jar BigDataScript.jar [options] programFile.bds [programOptions]
 *
 * 	- Any command line argument AFTER "programFile.bds" is considered a
 * 	  command line option for the BDS program. E.g.
 *
 * 		java -jar Bds.jar -v program.bds -file myFile.txt -verbose -num 7
 *
 *	  So our program "program.bds" has command line options:
 *		-file myFile.txt
 *		-verbose
 *		-num 7 (notice that "-v" is a command line option for bds.jar and not for "program.bds")
 *
 *	- We look for variables in ProgramUnit that match the name of these command line arguments
 *
 *	- Then we add those values to the variable initialization. Thus
 *	  overriding any initialization values provided in the program. E.g.
 *
 * 	  So, if our program has the following variable declarations:
 * 		string file = "default_file.txt"
 * 		int num = 3
 * 		bool verbose = false
 *
 * 	  When we execute the program:
 * 		java -jar BigDataScript.jar -v program.bds -file myFile.txt -verbose -num 7
 *
 * 	  The variable declarations are replaced as follows:
 * 		string file = "myFile.txt"
 * 		int num = 7
 * 		bool verbose = true
 *
 * 	- Note: Only primitive and list types are supported (i.e.: string, bool, int,
 * 	  real and list of strings)
 *
 *	- Note: Unmatched variables names will be silently ignored (same for variables
 *    that match, but are non-primitive)
 *
 *	- Note: If a variable is matched, is primitive, but cannot be converted.
 *	  An error is thrown.
 *	  E.g.:
 *		Program:
 *			int num = 1
 * 		Command line:
 * 			java -jar BigDataScript.jar program.bds -num "hello" <- This is an error because 'num' is an int
 *
 *	- Note: Unprocessed arguments will be available to the program as an 'args' list
 *
 * @author pcingola
 */
public class BdsParseArgs {

	boolean debug;
	boolean showHelp;
	int argNum = 0;
	ProgramUnit programUnit;
	ArrayList<String> programArgs; // Command line arguments for BigDataScript program

	public BdsParseArgs(Bds bds) {
		programUnit = bds.programUnit;
		programArgs = bds.getProgramArgs();
	}

	/**
	 * Parse all command line arguments
	 * Note: argNum may be modified by other methods
	 */
	void initializeArgs() {
		for (argNum = 0; argNum < programArgs.size(); argNum++) {
			String arg = programArgs.get(argNum);

			// Parse '-OPT' option
			if (arg.equalsIgnoreCase("-h") || arg.equalsIgnoreCase("-help") || arg.equalsIgnoreCase("--help")) {
				if (debug) Timer.showStdErr("Activating 'show help' mode");
				showHelp = true;
			} else if (arg.startsWith("-")) {
				// Get variable name and value
				String varName = arg.substring(1);
				initializeArgs(varName);
			}
		}

		// Make all unprocessed arguments available for the program (in 'args' list)
		Scope.getGlobalScope().add(new ScopeSymbol(Scope.GLOBAL_VAR_ARGS_LIST, TypeList.get(Types.STRING), programArgs));

		// Initialize program name
		String programPath = programUnit.getFileName();
		String progName = Gpr.baseName(programPath);
		Scope.getGlobalScope().add(new ScopeSymbol(Scope.GLOBAL_VAR_PROGRAM_NAME, Types.STRING, progName));
		Scope.getGlobalScope().add(new ScopeSymbol(Scope.GLOBAL_VAR_PROGRAM_PATH, Types.STRING, programPath));
	}

	/**
	 * Initialize a variable with argument values
	 * Note that the variable could be a global variable (defined in the scope)
	 * or a variable defined by the program
	 */
	void initializeArgs(String varName) {
		//---
		// Initialize variable declarations
		//---
		// Find all variable declarations that match this command line argument
		for (VarDeclaration varDecl : programUnit.varDeclarations(true)) {
			Type varType = varDecl.getType();
			// Is is a primitive variable or a primitive list?
			if (varType.isPrimitiveType() || varType.isList()) {

				// Find an initialization that matches the command line argument
				for (VariableInit varInit : varDecl.getVarInit())
					if (varInit.getVarName().equals(varName)) { // Name matches?
						setVarInit(varName, varType, varInit);
						return;
					}
			}
		}

		//---
		// Initialize scope variables
		// Note: Only does this if the variable was not found in the program
		//---
		ScopeSymbol ssym = Scope.getGlobalScope().getSymbol(varName);
		if (ssym != null) {
			Object value = parseArgs(ssym.getType());
			ssym.setValue(value);
		}

	}

	/**
	 * Is this a command line option (e.g. "-tfam" is a command line option, but "-" means STDIN)
	 */
	protected boolean isOpt(String arg) {
		return arg.startsWith("-") && (arg.length() > 1);
	}

	public boolean isShowHelp() {
		return showHelp;
	}

	void log(String msg) {
		Timer.showStdErr(getClass().getSimpleName() + ": " + msg);
	}

	public void parse() {
		initializeArgs();
	}

	/**
	 * Parse an argument and return an object with the
	 */
	Object parseArgs(Type varType) {
		if (varType.isList()) {
			// Create a list of arguments and use them to initialize the variable (list)
			ArrayList<String> vals = new ArrayList<String>();
			for (argNum++; argNum < programArgs.size(); argNum++) {
				String val = programArgs.get(argNum);
				if (isOpt(val)) { // Stop if another argument is found
					argNum--; // This value is not used
					break;
				} else vals.add(val);
			}

			return vals;
		} else if (varType.isBool()) {
			// Booleans may not have a value (just '-varName' sets them to 'true')
			if (programArgs.size() > (argNum + 1)) {
				// Is the next argument 'true' or 'false'? => Set argument
				String valStr = programArgs.get(++argNum).toLowerCase();
				if (valStr.equals("true") || valStr.equals("t") || valStr.equals("1")) return true;
				else if (valStr.equals("false") || valStr.equals("f") || valStr.equals("0")) return false;

				// Not any valid value? => This argument is not used
				argNum--;
			}
			return true; // Default value
		}

		// Default parsing
		String val = (argNum < programArgs.size() ? programArgs.get(++argNum) : ""); // Get one argument and use it to initialize the variable
		return varType.parse(val);
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * Set a program's variable initialization part (of a variable declaration)
	 */
	void setVarInit(String varName, Type varType, VariableInit varInit) {
		int argNumOri = argNum;
		boolean useVal = false;

		if (varType.isList()) {
			// Create a list of arguments and use them to initialize the variable (list)
			ArrayList<String> vals = new ArrayList<String>();
			for (argNum++; argNum < programArgs.size(); argNum++) {
				String val = programArgs.get(argNum);
				if (isOpt(val)) { // Stop if another argument is found
					argNum--; // This value is not used
					break;
				} else vals.add(val);
			}

			useVal = setVarInit(varType, varInit, vals); // Found variable, try to replace or add LITERAL to this VarInit
		} else if (varType.isBool()) {
			String valStr = "true";

			// Booleans may not have a value (just '-varName' sets them to 'true')
			if (programArgs.size() > (argNum + 1)) {
				// Is the next argument 'true' or 'false'? => Set argument
				String boolVal = programArgs.get(++argNum);
				if (valStr.equalsIgnoreCase("true") || valStr.equalsIgnoreCase("false")) valStr = boolVal;
			}

			useVal = setVarInit(varType, varInit, valStr);
		} else {
			String val = (argNum < programArgs.size() ? programArgs.get(++argNum) : ""); // Get one argument and use it to initialize the variable
			useVal = setVarInit(varType, varInit, val); // Found variable, try to replace or add LITERAL to this VarInit
		}

		if (!useVal) argNum = argNumOri; // We did not use the arguments
	}

	/**
	 * Add or replace initialization statement in this VarInit
	 *
	 * Note: We create a Literal node (of the appropriate type) and add it to
	 * "varInit.expression"
	 */
	boolean setVarInit(Type varType, VariableInit varInit, ArrayList<String> vals) {
		boolean usedVal = true;

		try {
			Literal literal = null;

			if (varType.isList(Types.STRING)) {
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
	 * @param varType: Variable type
	 * @param varInit: Variable initialization
	 * @param valStr: Value to assign
	 */
	boolean setVarInit(Type varType, VariableInit varInit, String valStr) {
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
					else usedVal = false; // Not any valid value? => This argument is not used
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

}
