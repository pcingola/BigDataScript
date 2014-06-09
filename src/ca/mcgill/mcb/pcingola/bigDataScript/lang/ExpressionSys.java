package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.Exec;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.ExecResult;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.RunState;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerializer;
import ca.mcgill.mcb.pcingola.bigDataScript.util.GprString;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Tuple;

/**
 * An 'exec' expression (to execute a command line in a local computer, return STDOUT)
 * 
 * @author pcingola
 */
public class ExpressionSys extends Expression {

	public static String SHELL_COMMAND[] = { "/bin/bash", "-e", "-c" };

	protected static int sysId = 1;

	protected String commands;
	protected String execId;
	protected List<String> strings; // This is used in case of interpolated string literal
	protected List<String> variables; // This is used in case of interpolated string literal
	protected String output;

	/**
	 * Create a new sys command
	 * @param parent
	 * @param commands
	 * @param lineNum
	 * @param charPosInLine
	 * @return
	 */
	public static ExpressionSys get(BigDataScriptNode parent, String commands, int lineNum, int charPosInLine) {
		ExpressionSys sys = new ExpressionSys(parent, null);

		sys.commands = commands;
		sys.lineNum = lineNum;
		sys.charPosInLine = charPosInLine;
		sys.interpolateVars(commands);

		return sys;
	}

	/**
	 * Get a sys ID
	 * @return
	 */
	private static int nextId() {
		return sysId++;
	}

	public ExpressionSys(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public Object eval(BigDataScriptThread csThread) {
		// Run like a statement and return task ID
		run(csThread);
		return (output != null ? output : "");
	}

	/**
	 * Create an exec ID
	 * @param csThread
	 * @return
	 */
	public String execId(String name, BigDataScriptThread csThread) {
		execId = csThread.getBigDataScriptThreadId() + "/" + name + ".line_" + getLineNum() + ".id_" + nextId();
		return execId;
	}

	public String getCommands(BigDataScriptThread csThread) {
		// No variable interpolation? => Literal
		if (variables == null) return commands;

		// Variable interpolation
		return csThread.getScope().interpolate(strings, variables);
	}

	public String getSysFileName() {
		if (execId == null) throw new RuntimeException("Exec ID is null. This should never happen!");

		String sysFileName = execId + ".sh";
		File f = new File(sysFileName);
		try {
			return f.getCanonicalPath();
		} catch (IOException e) {
			throw new RuntimeException("cannot get cannonical path for file '" + sysFileName + "'");
		}
	}

	/**
	 * Interpolate variables
	 * @param value
	 */
	void interpolateVars(String value) {
		Tuple<List<String>, List<String>> interpolated = GprString.findVariables(value);
		if (!interpolated.second.isEmpty()) { // Anything found?
			strings = interpolated.first;
			variables = interpolated.second;
		}
	}

	@Override
	protected void parse(ParseTree tree) {
		commands = tree.getChild(0).getText();
		commands = commands.substring("sys".length()).trim(); // Remove leading 'exec' part and trim spaces
		interpolateVars(commands); // Find interpolated variables
	}

	/**
	 * Sys expression always returns the task id, which is a string
	 */
	@Override
	public Type returnType(Scope scope) {
		returnType = Type.STRING;
		return returnType;
	}

	@Override
	protected RunState runStep(BigDataScriptThread csThread) {
		if (csThread.isCheckpointRecover()) return RunState.CHECKPOINT_RECOVER;

		// Get an ID
		execId = execId("exec", csThread);

		// EXEC expressions are always executed locally AND immediately
		LinkedList<String> args = new LinkedList<String>();
		for (String arg : SHELL_COMMAND)
			args.add(arg);

		// Interpolated variables
		String cmds = getCommands(csThread);
		args.add(cmds);

		// Run command line
		ExecResult execResult = Exec.exec(args, false);

		// Error running process? 
		int exitValue = execResult.exitValue;
		if (exitValue != 0) {
			// Can this execution fail?
			boolean canFail = csThread.getBool(ExpressionTask.TASK_OPTION_CAN_FAIL);

			// Execution failed on a 'sys' command that cannot fail. Save checkpoint and exit
			if (!canFail) {
				csThread.fatalError(this, "Exec failed." //
						+ "\n\tExit value : " + exitValue //
						+ "\n\tCommand    : " + cmds //
				);
				return RunState.FATAL_ERROR;
			}
		}

		// Collect output
		if (execResult.stdOut != null) output = execResult.stdOut;

		return RunState.OK;
	}

	@Override
	public void serializeParse(BigDataScriptSerializer serializer) {
		super.serializeParse(serializer);
		interpolateVars(commands); // Need to re-build this
	}

	@Override
	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		// Do we have any interpolated variables? Make sure they are in scope
		if (variables != null) //
			for (String varName : variables)
				if (!varName.isEmpty() && !scope.hasSymbol(varName, false)) //
					compilerMessages.add(this, "Symbol '" + varName + "' cannot be resolved", MessageType.ERROR);
	}

}
