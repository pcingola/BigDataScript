package org.bds.lang.expression;

import java.util.LinkedList;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.Config;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.InterpolateVars;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.lang.value.ValueString;
import org.bds.osCmd.Exec;
import org.bds.osCmd.ExecResult;
import org.bds.run.BdsThread;
import org.bds.symbol.SymbolTable;

/**
 * An 'exec' expression (to execute a command line in a local computer, return STDOUT)
 *
 * @author pcingola
 */
public class ExpressionSys extends Expression {

	private static final long serialVersionUID = -8698024999497987021L;

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

	public static void run(BdsThread bdsThread) {
		// 'sys' expressions are always executed locally and immediately
		LinkedList<String> args = new LinkedList<>();
		String shell = Config.get().getSysShell();
		for (String arg : shell.split("\\s+"))
			args.add(arg);

		// Command from string or interpolated vars
		String cmds = bdsThread.pop().asString();
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
		bdsThread.push(new ValueString(output));
	}

	public ExpressionSys(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	@Override
	protected void parse(ParseTree tree) {
		String cmd = tree.getChild(0).getText();

		if (cmd.startsWith("sys ")) cmd = cmd.substring("sys ".length());
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

	void setCommands(String cmd) {
		commands = cmd.trim();

		// Parse interpolated variables
		interpolateVars = new InterpolateVars(this, null);
		//		interpolateVars.setUseLiteral(true);
		if (!interpolateVars.parse(cmd)) {
			interpolateVars = null; // Nothing found? don't bother to keep the object
			commands = InterpolateVars.unEscapeDollar(cmd); // Just use literal, but escape dollar signs
		}
	}

	@Override
	public String toAsm() {
		return toAsm(true);
	}

	public String toAsm(boolean useSys) {
		StringBuilder sb = new StringBuilder();

		String comment = "\\n# SYS command. line " + getLineNum() + "\\n";

		if (interpolateVars == null) {
			// No variable interpolation? => Literal
			String cmd = InterpolateVars.escapeMultiline(commands);
			sb.append("pushs '" + comment + cmd + "'\n");
		} else {
			sb.append("pushs '" + comment + "'\n");
			sb.append(interpolateVars.toAsm());
			sb.append("adds\n");
		}

		if (useSys) sb.append("sys\n");

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
