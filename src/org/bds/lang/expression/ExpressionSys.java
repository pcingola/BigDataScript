package org.bds.lang.expression;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.Config;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.InterpolateVars;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.osCmd.Exec;
import org.bds.osCmd.ExecResult;
import org.bds.run.BdsThread;
import org.bds.symbol.SymbolTable;
import org.bds.util.Gpr;

/**
 * An 'exec' expression (to execute a command line in a local computer, return STDOUT)
 *
 * @author pcingola
 */
public class ExpressionSys extends Expression {

	private static final long serialVersionUID = -8698024999497987021L;

	protected static int sysId = 1;

	protected String commands;
	InterpolateVars interpolateVars;

	/**
	 * Create a new sys command
	 */
	public static ExpressionSys get(BdsNode parent, String commands, int lineNum, int charPosInLine) {
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

	public ExpressionSys(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Create an exec ID
	 */
	public synchronized String execId(String name, String fileName, String taskName, BdsThread bdsThread) {
		int nextId = nextId();

		// Use module name
		String module = fileName;
		if (module != null) module = Gpr.removeExt(Gpr.baseName(module));

		if (taskName != null) {
			if (taskName.isEmpty()) taskName = null;
			else taskName = Gpr.sanityzeName(taskName); // Make sure that 'taskName' can be used in a filename
		}

		String execId = bdsThread.getBdsThreadId() //
				+ "/" + name //
				+ (module == null ? "" : "." + module) //
				+ (taskName == null ? "" : "." + taskName) //
				+ ".line_" + getLineNum() //
				+ ".id_" + nextId //
		;

		return execId;
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
	public Type returnType(SymbolTable symtab) {
		returnType = Types.STRING;
		return returnType;
	}

	public static void run(BdsThread bdsThread) {
		nextId(); // Make sure sysId is increased

		// 'sys' expressions are always executed locally and immediately
		LinkedList<String> args = new LinkedList<>();
		String shell = Config.get().getSysShell();
		for (String arg : shell.split("\\s+"))
			args.add(arg);

		// Command from string or interpolated vars
		String cmds = bdsThread.popString();
		args.add(cmds);

		// Run command line
		boolean quiet = !(bdsThread.getConfig().isVerbose() || bdsThread.getConfig().isDebug() || bdsThread.getConfig().isLog());
		ExecResult execResult = Exec.exec(args, quiet);

		// Error running process?
		int exitValue = execResult.exitValue;
		if (exitValue != 0) {
			// Can this execution fail?
			boolean canFail = bdsThread.getBool(ExpressionTask.TASK_OPTION_CAN_FAIL);

			// Execution failed on a 'sys' command that cannot fail. Save checkpoint and exit
			if (!canFail) {
				bdsThread.fatalError("Exec failed." //
						+ "\n\tExit map : " + exitValue //
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

	void setCommands(String cmd) {
		commands = cmd.trim();

		// Parse interpolated variables
		interpolateVars = new InterpolateVars(this, null);
		interpolateVars.setUseLiteral(true);
		if (!interpolateVars.parse(cmd)) {
			interpolateVars = null; // Nothing found? don't bother to keep the object
			commands = InterpolateVars.unEscapeDollar(cmd); // Just use literal, but escape dollar signs
		}
	}

	@Override
	public String toAsm() {
		StringBuilder sb = new StringBuilder();

		if (interpolateVars == null) {
			// No variable interpolation? => Literal
			sb.append("pushs '" + commands + "'\n");
		} else {
			sb.append(interpolateVars.toAsm());
		}

		sb.append("sys\n");

		return sb.toString();
	}

	@Override
	public String toString() {
		return "sys " + commands;
	}

	@Override
	public void typeCheck(SymbolTable symtab, CompilerMessages compilerMessages) {
		// Do we have any interpolated variables? Make sure they are in the scope
		if (interpolateVars != null) interpolateVars.typeCheckNotNull(symtab, compilerMessages);
	}
}
