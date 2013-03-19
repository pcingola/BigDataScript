package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.exec.Executioner;
import ca.mcgill.mcb.pcingola.bigDataScript.exec.Executioners;
import ca.mcgill.mcb.pcingola.bigDataScript.exec.Executioners.ExecutionerType;
import ca.mcgill.mcb.pcingola.bigDataScript.exec.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.RunState;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerializer;
import ca.mcgill.mcb.pcingola.bigDataScript.util.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.util.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.util.GprString;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Tuple;

/**
 * A 'sys' statement (to execute a command line in a local computer)
 * 
 * Create a shell file, then invoke a shell to execute it.
 *
 * @author pcingola
 */
public class ExpressionSys extends Expression {

	private static int sysId = 1;

	String commands;
	private String execId;
	private List<String> strings; // This is used in case of interpolated string literal
	private List<String> variables; // This is used in case of interpolated string literal

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
		return execId;
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
		commands = commands.substring("sys".length()).trim(); // Remove leading 'sys' part and trim spaces
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
		execId = execId("sys", csThread);

		// Create a 

		// SYS expressions are always executed locally
		// Create a task
		Task task = new Task(execId, getSysFileName(), getCommands(csThread));
		task.setVerbose(csThread.getConfig().isVerbose());
		task.setDebug(csThread.getConfig().isDebug());
		csThread.add(task); // Add task to thread

		// Execute
		Executioner executioner = Executioners.getInstance().get(ExecutionerType.LOCAL_SYS); // Get executioner
		executioner.add(task); // Execute task and wait for command to finish

		boolean runOk = true;

		// Error running the program? 
		if (!runOk) {
			// Execution failed! Save checkpoint and exit
			csThread.checkpoint(null);
			csThread.setExitValue(task.getExitValue()); // Set return value and exit
			return RunState.EXIT;
		}

		return RunState.OK;
	}

	@Override
	public void serializeParse(BigDataScriptSerializer serializer) {
		super.serializeParse(serializer);
		interpolateVars(commands); // Need to re-build this
	}

	@Override
	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		// Do we have any interpolated variables? Make sure they are in th scope
		if (variables != null) //
			for (String varName : variables)
				if (!varName.isEmpty() && !scope.hasSymbol(varName, false)) //
					compilerMessages.add(this, "Symbol '" + varName + "' cannot be resolved", MessageType.ERROR);
	}

}
