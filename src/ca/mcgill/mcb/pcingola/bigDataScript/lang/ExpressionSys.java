package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.Config;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.Exec;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.ExecResult;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerializer;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * An 'exec' expression (to execute a command line in a local computer, return STDOUT)
 *
 * @author pcingola
 */
public class ExpressionSys extends Expression {

	public static String DEFAULT_SYS_SHELL = "/bin/bash -e -c"; // Note: This executes a script, so it requires the "-c" right before script name

	protected static int sysId = 1;

	protected String commands;
	InterpolateVars interpolateVars;

	/**
	 * Create a new sys command
	 */
	public static ExpressionSys get(BigDataScriptNode parent, String commands, int lineNum, int charPosInLine) {
		ExpressionSys sys = new ExpressionSys(parent, null);

		sys.lineNum = lineNum;
		sys.charPosInLine = charPosInLine;
		sys.setCommands(commands);

		return sys;
	}

	/**
	 * Get a sys ID
	 */
	private static synchronized int nextId() {
		return sysId++;
	}

	public ExpressionSys(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Create an exec ID
	 */
	public synchronized String execId(String name, String fileName, BigDataScriptThread bdsThread) {
		int nextId = nextId();

		String module = fileName;
		if (module != null) module = Gpr.removeExt(Gpr.baseName(module));

		String execId = bdsThread.getBdsThreadId() //
				+ "/" + name //
				+ (module == null ? "" : "." + module) //
				+ ".line_" + getLineNum() //
				+ ".id_" + nextId //
		;

		return execId;
	}

	public String getCommands(BigDataScriptThread bdsThread) {
		if (interpolateVars == null) return commands; // No variable interpolation? => Literal

		// Variable interpolation
		bdsThread.run(interpolateVars);
		if (bdsThread.isCheckpointRecover()) return null;

		return bdsThread.pop().toString();
	}

	public String getSysFileName(String execId) {
		if (execId == null) throw new RuntimeException("Exec ID is null. This should never happen!");

		String sysFileName = execId + ".sh";
		File f = new File(sysFileName);
		try {
			return f.getCanonicalPath();
		} catch (IOException e) {
			throw new RuntimeException("cannot get cannonical path for file '" + sysFileName + "'");
		}
	}

	@Override
	protected void parse(ParseTree tree) {
		String cmd = tree.getChild(0).getText();

		if (cmd.startsWith("sys")) cmd = cmd.substring("sys".length());
		setCommands(cmd);
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
	public void runStep(BigDataScriptThread bdsThread) {
		if (bdsThread.isCheckpointRecover()) return;

		execId("exec", getFileName(), bdsThread);

		// EXEC expressions are always executed locally AND immediately
		LinkedList<String> args = new LinkedList<String>();
		String shell = Config.get().getString(Config.SYS_SHELL, DEFAULT_SYS_SHELL);
		for (String arg : shell.split("\\s+"))
			args.add(arg);

		// Interpolated variables
		String cmds = getCommands(bdsThread);
		if (bdsThread.isCheckpointRecover()) return;

		args.add(cmds);

		// Run command line
		ExecResult execResult = Exec.exec(args, bdsThread.getConfig().isQuiet());

		// Error running process?
		int exitValue = execResult.exitValue;
		if (exitValue != 0) {
			// Can this execution fail?
			boolean canFail = bdsThread.getBool(ExpressionTask.TASK_OPTION_CAN_FAIL);

			// Execution failed on a 'sys' command that cannot fail. Save checkpoint and exit
			if (!canFail) {
				bdsThread.fatalError(this, "Exec failed." //
						+ "\n\tExit value : " + exitValue //
						+ "\n\tCommand    : " + cmds //
				);
				return;
			}
		}

		// Collect output
		String output = "";
		if (execResult.stdOut != null) output = execResult.stdOut;
		bdsThread.push(output);
	}

	@Override
	public void serializeParse(BigDataScriptSerializer serializer) {
		super.serializeParse(serializer);
	}

	void setCommands(String cmd) {
		commands = cmd.trim();

		// Parse interpolated variables
		interpolateVars = new InterpolateVars(this, null);
		interpolateVars.setUseLiteral(true);
		if (!interpolateVars.parse(cmd)) {
			interpolateVars = null; // Nothing found? don't bother to keep the object
			commands = cmd; // Just use literal
			// commands = InterpolateVars.unEscape(cmd); // Just un-escape characters
		}
	}

	@Override
	public String toString() {
		return "sys " + commands;
	}

	@Override
	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		// Do we have any interpolated variables? Make sure they are in the scope
		if (interpolateVars != null) interpolateVars.typeCheckNotNull(scope, compilerMessages);
	}
}
