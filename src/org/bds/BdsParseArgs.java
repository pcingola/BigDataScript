package org.bds;

import java.util.ArrayList;
import java.util.List;

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
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueBool;
import org.bds.lang.value.ValueInt;
import org.bds.lang.value.ValueList;
import org.bds.lang.value.ValueReal;
import org.bds.lang.value.ValueString;
import org.bds.scope.GlobalScope;
import org.bds.util.Gpr;

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
public class BdsParseArgs implements BdsLog {

	boolean debug;
	boolean showHelp;
	int argNum = 0;
	ProgramUnit programUnit;
	List<String> programArgs; // Command line arguments for BigDataScript program

	public BdsParseArgs(ProgramUnit programUnit, List<String> args) {
		this.programUnit = programUnit;
		programArgs = args;
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
				debug("Activating 'show help' mode");
				showHelp = true;
			} else if (arg.startsWith("-")) {
				// Get variable name and map
				String varName = arg.substring(1);
				initializeArgs(varName);
			}
		}

		// Initialize program name
		String programPath = programUnit.getFileName();
		String progName = Gpr.baseName(programPath);
		GlobalScope gs = GlobalScope.get();

		// Create and populate argument list
		ValueList vargs = (ValueList) TypeList.get(Types.STRING).newValue();
		for (String arg : programArgs)
			vargs.add(new ValueString(arg));

		gs.add(GlobalScope.GLOBAL_VAR_ARGS_LIST, vargs); // Make all unprocessed arguments available for the program (in 'args' list)
		gs.add(GlobalScope.GLOBAL_VAR_PROGRAM_NAME, progName);
		gs.add(GlobalScope.GLOBAL_VAR_PROGRAM_PATH, programPath);
		gs.add(GlobalScope.GLOBAL_VAR_PROGRAM_PID, ProcessHandle.current().pid());
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
			// We can only parse some basic types or lists of strings
			if (varType.isBool() //
					|| varType.isInt() //
					|| varType.isReal() //
					|| varType.isString() //
					|| varType.isList(Types.STRING) //
			) {

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
		Value val = GlobalScope.get().getValue(varName);
		if (val != null) {
			Value value = parseArgs(val.getType());
			val.setValue(value);
		}

	}

	@Override
	public boolean isDebug() {
		return debug;
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

	public void parse() {
		initializeArgs();
	}

	/**
	 * Parse an argument and return an object with the
	 */
	Value parseArgs(Type varType) {
		if (varType.isList()) {
			// Create a list of arguments and use them to initialize the variable (list)
			List<String> vals = new ArrayList<>();
			for (argNum++; argNum < programArgs.size(); argNum++) {
				String val = programArgs.get(argNum);
				if (isOpt(val)) { // Stop if another argument is found
					argNum--; // This map is not used
					break;
				} else vals.add(val);
			}

			return TypeList.get(Types.STRING).newValue(vals);
		} else if (varType.isBool()) {
			// Booleans may not have a map (just '-varName' sets them to 'true')
			if (programArgs.size() > (argNum + 1)) {
				// Is the next argument 'true' or 'false'? => Set argument
				String valStr = programArgs.get(++argNum).toLowerCase();
				if (valStr.equals("true") || valStr.equals("t") || valStr.equals("1")) return ValueBool.TRUE;
				else if (valStr.equals("false") || valStr.equals("f") || valStr.equals("0")) return ValueBool.FALSE;

				// Not any valid map? => This argument is not used
				argNum--;
			}
			return Value.factory(true); // Default map
		}

		// Default parsing
		String valStr = (argNum < programArgs.size() ? programArgs.get(++argNum) : ""); // Get one argument and use it to initialize the variable
		Value val = varType.newDefaultValue();
		val.parse(valStr);
		return val;
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
			ValueList vals = new ValueList(Types.STRING);
			for (argNum++; argNum < programArgs.size(); argNum++) {
				String s = programArgs.get(argNum);
				if (isOpt(s)) { // Stop if another argument is found
					argNum--; // This map is not used
					break;
				} else vals.add(new ValueString(s));
			}

			useVal = setVarInit(varType, varInit, vals); // Found variable, try to replace or add LITERAL to this VarInit
		} else if (varType.isBool()) {
			String valStr = "true";

			// Booleans may not have a map (just '-varName' sets them to 'true')
			if (programArgs.size() > (argNum + 1)) {
				// Is the next argument 'true' or 'false'? => Set argument
				String boolVal = programArgs.get(++argNum);
				if (valStr.equalsIgnoreCase("true") || valStr.equalsIgnoreCase("false")) valStr = boolVal;
			}

			useVal = setVarInit(varType, varInit, valStr);
		} else {
			String val = (programArgs.size() > (argNum + 1) ? programArgs.get(++argNum) : ""); // Get one argument and use it to initialize the variable
			useVal = setVarInit(varType, varInit, val); // Found variable, try to replace or add LITERAL to this VarInit
		}

		if (!useVal) argNum = argNumOri; // We did not use the arguments
	}

	/**
	 * Add or replace initialization statement in this VarInit
	 *
	 * Note: We create a Literal node (of the appropriate type) and add it to
	 * "fieldDecl.expression"
	 *
	 * @param varType: Variable type
	 * @param fieldDecl: Variable initialization
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

				// Set literal map
				boolean valBool = true; // Default map is 'true'
				if (valStr != null) {
					// Parse boolean
					valStr = valStr.toLowerCase();
					if (valStr.equals("true") || valStr.equals("t") || valStr.equals("1")) valBool = true;
					else if (valStr.equals("false") || valStr.equals("f") || valStr.equals("0")) valBool = false;
					else usedVal = false; // Not any valid value? => This argument is not used
				}

				lit.setValue(new ValueBool(valBool));
			} else if (varType.isInt()) {
				// Create literal
				LiteralInt lit = new LiteralInt(varInit, null);
				literal = lit;

				// Set literal map
				long valInt = Long.parseLong(valStr);
				lit.setValue(new ValueInt(valInt));
			} else if (varType.isReal()) {
				// Create literal
				LiteralReal lit = new LiteralReal(varInit, null);
				literal = lit;

				// Set literal map
				double valReal = Double.parseDouble(valStr);
				lit.setValue(new ValueReal(valReal));
			} else if (varType.isString()) {
				// Create literal
				LiteralString lit = new LiteralString(varInit, null);
				literal = lit;

				// Set literal value
				if (valStr == null) valStr = ""; // We should never have 'null' values
				lit.setValue(new ValueString(valStr));
			} else throw new RuntimeException("Cannot convert command line argument to variable type '" + varType + "'");

			// Set fieldDecl to literal
			varInit.setExpression(literal);
		} catch (Exception e) {
			// Error parsing 'val'?
			throw new RuntimeException("Cannot convert argument '" + valStr + "' to type " + varType);
		}

		return usedVal;
	}

	/**
	 * Add or replace initialization statement in this VarInit
	 *
	 * Note: We create a Literal node (of the appropriate type) and add it to
	 * "fieldDecl.expression"
	 */
	boolean setVarInit(Type varType, VariableInit varInit, ValueList vals) {
		boolean usedVal = true;

		try {
			Literal literal = null;

			if (varType.isList(Types.STRING)) {
				// Create literal
				LiteralListString lit = new LiteralListString(varInit, null);
				literal = lit;
				lit.setValue(vals); // Set literal map
			} else throw new RuntimeException("Cannot convert command line argument to variable type '" + varType + "'");

			// Set fieldDecl to literal
			varInit.setExpression(literal);
		} catch (Exception e) {
			// Error parsing 'val'?
			throw new RuntimeException("Cannot convert argument '" + vals + "' to type " + varType);
		}

		return usedVal;
	}

}
