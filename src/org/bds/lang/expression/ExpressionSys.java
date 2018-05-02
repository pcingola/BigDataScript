package org.bds.lang.expression;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.lang.value.InterpolateVars;
import org.bds.symbol.SymbolTable;
import org.bds.util.GprString;

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
		cmd = GprString.escapeMultiline(cmd.trim());
		commands = cmd;

		// Parse interpolated variables
		interpolateVars = new InterpolateVars(this, null);
		if (!interpolateVars.parse(cmd)) {
			interpolateVars = null; // Nothing found? don't bother to keep the object
			commands = GprString.unescapeDollar(cmd); // Just use literal, but un-escape dollar signs
		}
	}

	@Override
	public String toAsm() {
		return toAsm(true);
	}

	public String toAsm(boolean useSys) {
		StringBuilder sb = new StringBuilder();

		// When using 'sys' command we don't add comments
		// because sys commands are executed using shell inline
		// E.g.:
		//    sys ls -al
		// Is executed by running something like:
		//    bash -c 'ls -al'
		String comment = useSys ? "" : "\\n# SYS command. line " + getLineNum() + "\\n";

		if (interpolateVars == null) {
			// No variable interpolation? => Literal
			String cmd = commands;
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
