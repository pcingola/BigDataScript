package org.bds.lang.expression;

import java.util.List;
import java.util.Random;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.Config;
import org.bds.compile.BdsNodeWalker;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.lang.BdsNode;
import org.bds.lang.statement.Block;
import org.bds.lang.statement.Statement;
import org.bds.lang.statement.StatementExpr;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.lang.value.InterpolateVars;
import org.bds.lang.value.Literal;
import org.bds.lang.value.LiteralString;
import org.bds.symbol.SymbolTable;
import org.bds.util.Gpr;
import org.bds.util.GprString;

/**
 * A 'task' expression
 *
 * @author pcingola
 */
public class ExpressionTask extends ExpressionWithScope {

	private static final long serialVersionUID = 5026042355679287158L;

	public static final String CMD_DOWNLOAD = "bds -download";
	public static final String CMD_UPLOAD = "bds -upload";
	public static final String CMD_TASK_IMPROPER = "bds -task";

	// Note:	It is important that 'options' node is type-checked before the others in order to
	//			add variables to the scope before statements uses them.
	//			So the field name should be alphabetically sorted before the other (that's why
	//			I call it 'options' and not 'taskOptions').
	//			Yes, it's a horrible hack.
	protected ExpressionTaskOptions options;
	protected Statement statement;
	protected boolean asmPushDeps; // True if we must push dependencies to stack (e.g. ExpressionParallel doesn't do it)
	protected boolean improper; // A task is improper if it has non-sys statements
	protected InterpolateVars preludeInterpolateVars; // Task prelude: interpolating strings (null if there is nothing to interpolate)
	protected String preludeStr; // Task prelude (raw) string from config

	public ExpressionTask(BdsNode parent, ParseTree tree) {
		super(parent, tree);
		asmPushDeps = true;
	}

	/**
	 * Return a full path to a checkpoint file name
	 * The file might be on an Object Store (e.g. S3)
	 * @return
	 */
	protected String checkpointFile() {
		Random r = new Random();
		String checkpointFile = getProgramUnit().getFileNameCanonical() + ".task." + id + "." + Math.abs(r.nextInt()) + ".chp";
		return checkpointFile;
	}

	protected boolean hasPrelude() {
		return preludeInterpolateVars != null || (preludeStr != null && !preludeStr.isEmpty());
	}

	@Override
	public boolean isReturnTypesNotNull() {
		return true;
	}

	@Override
	protected void parse(ParseTree tree) {
		int idx = 0;
		idx++; // 'task' keyword

		// Do we have any task options?
		if (tree.getChild(idx).getText().equals("(")) {
			int lastIdx = indexOf(tree, ")");

			options = new ExpressionTaskOptions(this, null);
			options.parse(tree, ++idx, lastIdx);
			idx = lastIdx + 1; // Skip last ')'
		}

		statement = (Statement) factory(tree, idx++); // Parse statement

		parsePrelude();
	}

	/**
	 * Parse prelude string from bds.config
	 * The "prelude" is a string that is added to all 'sys' within a task
	 */
	void parsePrelude() {
		String prelude = Config.get().getTaskPrelude();
		if (prelude == null || prelude.isEmpty()) {
			preludeStr = "";
			return;
		}

		preludeInterpolateVars = new InterpolateVars(this, null);
		if (!preludeInterpolateVars.parse(prelude)) {
			preludeInterpolateVars = null; // Nothing found? don't bother to keep the object
			preludeStr = GprString.unescapeDollar(prelude); // Just use literal, but un-escape dollar signs
		}
	}

	/**
	 * Task expression always returns the task id, which is a string
	 */
	@Override
	public Type returnType(SymbolTable symtab) {
		// Calculate options' return type
		if (options != null) options.returnType(symtab);
		if (statement != null) statement.returnType(symtab);

		// Task expressions return a task ID (a string)
		returnType = Types.STRING;
		return returnType;
	}

	@Override
	public void sanityCheck(CompilerMessages compilerMessages) {
		// Sanity check options
		if (options != null) options.sanityCheck(compilerMessages);

		// Sanity check statements
		if (statement == null) {
			compilerMessages.add(this, "Task has empty statement", MessageType.ERROR);
			return;
		}

		List<BdsNode> statements = BdsNodeWalker.findNodes(statement, null, true, false);

		// No child nodes? Add the only node we have
		if (statements.isEmpty()) statements.add(statement);

		setImproper(statements);
	}

	/**
	 * An "improper" task is a task that anything other than 'sys' statement/s
	 * Improper tasks are executed by creating a checkpoint and restoring from the checkpoint
	*/
	protected void setImproper(List<BdsNode> statements) {
		improper = false;
		for (BdsNode node : statements) {
			if (node instanceof Statement) {
				boolean ok = node instanceof ExpressionSys //
						|| node instanceof Block //
						|| node instanceof Literal //
						|| node instanceof InterpolateVars //
						|| node instanceof Reference //
						|| node instanceof StatementExpr //
				;

				if (!ok) improper = true;
			}
		}
	}

	@Override
	public String toAsm() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toAsmNode()); // Task will use the node to get parameters
		sb.append("scopepush\n");

		// Define labels
		String labelEnd = baseLabelName() + "end";
		String labelFalse = baseLabelName() + "false";

		// Options
		sb.append(toAsmOptions(labelFalse));

		// Command (e.g. task and statements)
		sb.append(toAsmCmd(labelEnd));

		// Task expression not evaluated because one or more bool expressions was false
		sb.append(labelFalse + ":\n");
		sb.append("pushs ''\n"); // Task not executed, push an empty task id

		// End of task expression
		sb.append(labelEnd + ":\n");
		sb.append("scopepop\n");
		return sb.toString();
	}

	/**
	 * Commands (i.e. task)
	 */
	protected String toAsmCmd(String labelEnd) {
		if (improper) return toAsmCmdOpCode(labelEnd, "taskimp");
		return toAsmCmdOpCode(labelEnd, "task");
	}

	/**
	 * Commands
	 */
	protected String toAsmCmdOpCode(String labelEnd, String opCode) {
		StringBuilder sb = new StringBuilder();

		// Should we add a 'prelude' to the script?
		boolean addPrelude = hasPrelude();

		if (addPrelude) sb.append(toAsmPrelude());
		sb.append(toAsmStatements()); // Statements (e.g.: sys commands)
		if (addPrelude) sb.append("adds\n");

		sb.append(opCode + "\n");

		sb.append("jmp " + labelEnd + "\n"); // Go to the end
		return sb.toString();
	}

	/**
	 * Options
	 */
	protected String toAsmOptions(String labelFalse) {
		StringBuilder sb = new StringBuilder();

		if (options != null) {
			// Jump to 'labelFalse' if any of the bool expressions is false
			sb.append(options.toAsm(labelFalse, asmPushDeps));
		} else if (asmPushDeps) {
			// No options or dependencies.
			// Add empty list as dependency
			sb.append("new string[]\n");
			sb.append("new string[]\n");
		}
		return sb.toString();
	}

	protected String toAsmPrelude() {
		if (preludeInterpolateVars != null) return preludeInterpolateVars.toAsm();
		if (preludeStr != null && !preludeStr.isEmpty()) return "pushs " + preludeStr + "\n";
		return "";
	}

	protected String toAsmStatements() {
		return improper ? toAsmStatementsImproper() : toAsmStatementsProper();

	}

	/**
	 * Create a checkpoint and create a task to execute from that checkpoint
	 * 'sys' opcodes are transformed into 'shell'
	 */
	protected String toAsmStatementsImproper() {
		StringBuilder sb = new StringBuilder();

		// Store the input / output dependencies to a hidden variable name
		// These were pushed into the stack in the previous step
		String varInputs = baseVarName() + "inputs";
		String varOutputs = baseVarName() + "outputs";
		sb.append("varpop " + varInputs + "\n");
		sb.append("varpop " + varOutputs + "\n");

		// Create a checkpoint
		String labelTaskBodyEnd = baseLabelName() + "body_end";
		String checkpointFileVar = baseVarName() + "checkpoint_file";

		// Reset the 'checkpoint_recovered' flag: Why? Because we want to make
		// sure that the flag is only true if the checkpoint we are creating
		// is recovered (e.g. it might be true because we recovered from a
		// previous checkpoint and we never checked it).
		// Note: Checking the flag 'checkpoint_recovered' also resets it.
		sb.append("checkpoint_recovered\n");
		sb.append("pop\n");

		// Create checkpoint (only VM, no threads, tasks, etc.) and push file name to stack
		sb.append("pushs ''\n"); // Empty checkpoint file name (it will be generated)
		sb.append("checkpointvm\n");

		// If this code is being executed right after a checkpoint recover, we
		// should continue into the task statements. Otherwise, we skip to the
		// end, because we are executing the 'main' bds process (not the improper
		// task)
		sb.append("checkpoint_recovered\n");
		sb.append("jmpf " + labelTaskBodyEnd + "\n");

		// Task body (i.e. the statements in the task) are executed by the
		// process that recovers from the checkpoint
		sb.append(toAsmStatementsImproperTaskBody(varOutputs, varInputs));

		// This code schedules the task execution. The task is recovering
		// from a the checkpoint we've just created.
		sb.append(labelTaskBodyEnd + ":\n");
		if (!Config.get().isLog()) {
			// Make sure we delete the checkpoint file at the end of the run
			sb.append("var " + checkpointFileVar + "\n");
			sb.append("rmonexit\n");
		} else {
			// If command line '-log' was provided, then we should not delete the checkpoint file
			sb.append("varpop " + checkpointFileVar + "\n");
		}

		// Add all task parameters: checkpointFile, outputs, inputs, script_command
		sb.append("load " + checkpointFileVar + "\n");
		sb.append("load " + varOutputs + "\n");
		sb.append("load " + varInputs + "\n");
		// Command to execute: "bds -restore $checkpointFileVar"
		sb.append("pushs \"" + CMD_TASK_IMPROPER + " \"\n");
		sb.append("load " + checkpointFileVar + "\n");
		sb.append("adds\n");

		return sb.toString();
	}

	/**
	 * This method creates the code from the statements of an improper task
	 *
	 * E.g. An improper task is executed in a cluster:
	 *   - A checkpoint is created
	 *   - A job is submitted to the cluster to run bds recovering from that checkpoint
	 *   - When the node executes bds, it recovers the checkpoint and executes the statement within the task
	 */
	protected String toAsmStatementsImproperTaskBody(String varOutputs, String varInputs) {
		StringBuilder sb = new StringBuilder();

		Block block = (Block) statement;
		for (Statement st : block.getStatements()) {
			sb.append(st.toAsm());
		}

		// There is an implicit 'exit' in the block.
		// Remember that these statement are executed in another process or another host, so
		// there is no point to continue beyond the task statements (that part is executed
		// on the main bds process)
		sb.append("pushi 0\n");
		sb.append("halt\n");

		return sb.toString();
	}

	/**
	 * Evaluate 'sys' statements used to create task
	 */
	protected String toAsmStatementsProper() {
		// Only one 'sys' expression
		if (statement instanceof StatementExpr) {
			Expression exprSys = ((StatementExpr) statement).getExpression();
			ExpressionSys sys = (ExpressionSys) exprSys;
			return sys.toAsm(false);
		}

		// One 'sys' expression within a statement
		if (statement instanceof ExpressionSys) {
			ExpressionSys sys = (ExpressionSys) statement;
			return sys.toAsm(false);
		}

		// Multiple 'sys' expressions in a block
		if (statement instanceof Block) {
			// Create one sys statement for all sys statements in the block
			StringBuilder sb = new StringBuilder();

			Block block = (Block) statement;
			sb.append("new string\n");
			for (Statement st : block.getStatements()) {
				// Get 'sys' expression
				if (st instanceof StatementExpr) st = ((StatementExpr) st).getExpression();
				ExpressionSys sys = (ExpressionSys) st;
				sb.append(sys.toAsm(false));
				sb.append("adds\n");
			}

			return sb.toString();
		}

		throw new RuntimeException("Unimplemented for class '" + statement.getClass().getSimpleName() + "'");
	}

	@Override
	public String toString() {
		return (improper ? "taskimp" : "task") //
				+ (options != null ? options : "") //
				+ " " //
				+ toStringStatement() //
		;
	}

	/**
	 * Format statements
	 */
	protected String toStringStatement() {
		if (statement instanceof LiteralString) {
			// Compact single line
			return ((LiteralString) statement).getValue().asString().trim();
		}

		if (statement instanceof ExpressionSys || statement instanceof StatementExpr) {
			// Compact single line form
			return statement.toString();
		}

		// Multi-line
		return "{\n" //
				+ (statement != null ? Gpr.prependEachLine("\t", statement.toString()) : "NULL") //
				+ "}" //
		;
	}

	@Override
	public void typeCheck(SymbolTable symtab, CompilerMessages compilerMessages) {
		returnType(symtab);
		if (options != null) options.typeCheck(symtab, compilerMessages);
		if (statement != null) statement.typeCheck(symtab, compilerMessages);
	}

}
