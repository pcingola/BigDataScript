package org.bds.lang.expression;

import java.util.HashMap;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.bds.compile.CompilerMessage.MessageType;
import org.bds.compile.CompilerMessages;
import org.bds.data.Data;
import org.bds.data.DataRemote;
import org.bds.executioner.Executioner;
import org.bds.executioner.Executioners;
import org.bds.lang.BdsNode;
import org.bds.lang.statement.Block;
import org.bds.lang.statement.Statement;
import org.bds.lang.statement.StatementExpr;
import org.bds.lang.type.InterpolateVars;
import org.bds.lang.type.Type;
import org.bds.lang.type.Types;
import org.bds.lang.value.Literal;
import org.bds.lang.value.LiteralString;
import org.bds.run.BdsThread;
import org.bds.symbol.SymbolTable;
import org.bds.task.Task;
import org.bds.task.TaskDependency;
import org.bds.util.Gpr;

/**
 * A 'task' expression
 *
 * @author pcingola
 */
public class ExpressionTask extends ExpressionWithScope {

	private static final long serialVersionUID = 5026042355679287158L;

	// Variable names
	public static final String TASK_OPTION_CPUS = "cpus";
	public static final String TASK_OPTION_MEM = "mem";
	public static final String TASK_OPTION_CAN_FAIL = "canFail";
	public static final String TASK_OPTION_ALLOW_EMPTY = "allowEmpty";
	public static final String TASK_OPTION_NODE = "node";
	public static final String TASK_OPTION_PHYSICAL_PATH = "ppwd";
	public static final String TASK_OPTION_QUEUE = "queue";
	public static final String TASK_OPTION_RETRY = "retry";
	public static final String TASK_OPTION_SYSTEM = "system";
	public static final String TASK_OPTION_TASKNAME = "taskName";
	public static final String TASK_OPTION_TIMEOUT = "timeout";
	public static final String TASK_OPTION_WALL_TIMEOUT = "walltimeout";

	public static final String CMD_DOWNLOAD = "bds -download";
	public static final String CMD_UPLOAD = "bds -upload";

	// Note:	It is important that 'options' node is type-checked before the others in order to
	//			add variables to the scope before statements uses them.
	//			So the field name should be alphabetically sorted before the other (that's why
	//			I call it 'options' and not 'taskOptions').
	//			Yes, it's a horrible hack.
	protected ExpressionTaskOptions options;
	protected Statement statement;

	/**
	 * Execute a task (schedule it into executioner)
	 */
	public static void execute(BdsThread bdsThread, Task task) {
		// Select executioner and queue for execution
		String runSystem = bdsThread.getString(TASK_OPTION_SYSTEM);
		Executioner executioner = Executioners.getInstance().get(runSystem);

		// Execute task
		task.execute(bdsThread, executioner);
	}

	public ExpressionTask(BdsNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Create commands that will be executed in a shell
	 */
	String createCommands(BdsThread bdsThread, TaskDependency taskDependency, ExpressionSys sys) {
		HashMap<String, String> replace = new HashMap<>();
		StringBuilder sbDown = new StringBuilder();
		StringBuilder sbUp = new StringBuilder();

		if (taskDependency != null) {
			//---
			// Are there any remote inputs?
			// We need to create the appropriate 'download' commands
			//---
			if (taskDependency.getInputs() != null) {
				for (String in : taskDependency.getInputs()) {
					Data dataIn = Data.factory(in);
					if (dataIn.isRemote()) {
						sbDown.append(CMD_DOWNLOAD //
								+ " \"" + dataIn.getAbsolutePath() + "\"" //
								+ " \"" + dataIn.getLocalPath() + "\"" //
								+ "\n");

						replace.put(dataIn.getAbsolutePath(), dataIn.getLocalPath());
					}
				}
			}

			//---
			// Are there any remote outputs?
			// We need to create the appropriate 'upload' commands
			//---
			if (taskDependency.getOutputs() != null) {
				for (String out : taskDependency.getOutputs()) {
					Data dataOut = Data.factory(out);
					if (dataOut.isRemote()) {
						sbUp.append(CMD_UPLOAD //
								+ " \"" + dataOut.getLocalPath() + "\"" //
								+ " \"" + dataOut.getAbsolutePath() + "\"" //
								+ "\n");

						replace.put(dataOut.getAbsolutePath(), dataOut.getLocalPath());

						// Note, commands executed locally will output to the local file, so
						// we must make sure that the path exists (otherwise the command
						// results in an error.
						((DataRemote) dataOut).mkdirsLocal();
					}
				}
			}
		}

		//---
		// Sys commands
		// Command from string or interpolated vars
		//---
		String sysCmds = bdsThread.pop().asString();

		// No Down/Up-load? Just return the SYS commands
		if (sbDown.length() <= 0 && sbUp.length() <= 0) return sysCmds;

		// Replace all occurrences of remote references
		sysCmds = replace(replace, sysCmds);

		// Put everything together
		StringBuilder sbSys = new StringBuilder();
		if (sbDown.length() > 0) {
			sbSys.append("# Download commands\n");
			sbSys.append(sbDown);
		}
		sbSys.append(sysCmds);
		if (sbUp.length() > 0) {
			sbSys.append("\n# Upload commands\n");
			sbSys.append(sbUp);
		}

		return sbSys.toString();
	}

	/**
	 * Create a task
	 */
	Task createTask(BdsThread bdsThread, TaskDependency taskDependency, ExpressionSys sys) {
		// Task name
		String taskName = "";
		if (bdsThread.hasVariable(TASK_OPTION_TASKNAME)) taskName = bdsThread.getString(TASK_OPTION_TASKNAME);

		// Get an ID
		String execId = sys.execId("task", getFileName(), taskName, bdsThread);

		// Get commands representing a shell program
		String sysCmds = createCommands(bdsThread, taskDependency, sys);

		// Create Task
		Task task = new Task(execId, this, sys.getSysFileName(execId), sysCmds);

		// Configure Task parameters
		task.setVerbose(bdsThread.getConfig().isVerbose());
		task.setDebug(bdsThread.getConfig().isDebug());

		// Set task options
		task.setTaskName(taskName);
		task.setCanFail(bdsThread.getBool(TASK_OPTION_CAN_FAIL));
		task.setAllowEmpty(bdsThread.getBool(TASK_OPTION_ALLOW_EMPTY));
		task.setNode(bdsThread.getString(TASK_OPTION_NODE));
		task.setQueue(bdsThread.getString(TASK_OPTION_QUEUE));
		task.setMaxFailCount((int) bdsThread.getInt(TASK_OPTION_RETRY) + 1); // Note: Max fail count is the number of retries plus one (we always run at least once)
		task.setCurrentDir(bdsThread.getCurrentDir());

		// Set task options: Resources
		task.getResources().setCpus((int) bdsThread.getInt(TASK_OPTION_CPUS));
		task.getResources().setMem(bdsThread.getInt(TASK_OPTION_MEM));
		task.getResources().setWallTimeout(bdsThread.getInt(TASK_OPTION_WALL_TIMEOUT));
		task.getResources().setTimeout(bdsThread.getInt(TASK_OPTION_TIMEOUT));
		if (taskDependency != null) task.setTaskDependency(taskDependency);

		return task;
	}

	/**
	 * Dispatch task for execution
	 */
	void dispatchTask(BdsThread bdsThread, Task task) {
		execute(bdsThread, task);
	}

	/**
	 * Evaluate 'sys' statements used to create task
	 */
	ExpressionSys evalSys(BdsThread bdsThread) {
		ExpressionSys sys = null;

		//		if (statement instanceof StatementExpr) {
		//			Expression exprSys = ((StatementExpr) statement).getExpression();
		//			sys = (ExpressionSys) exprSys;
		//		} else if (statement instanceof ExpressionSys) {
		//			sys = (ExpressionSys) statement;
		//		} else if (statement instanceof Block) {
		//			// Create one sys statement for all sys statements in the block
		//			StringBuilder syssb = new StringBuilder();
		//
		//			Block block = (Block) statement;
		//			for (Statement st : block.getStatements()) {
		//				// Get 'sys' expression
		//				if (st instanceof StatementExpr) st = ((StatementExpr) st).getExpression();
		//				ExpressionSys sysst = (ExpressionSys) st;
		//
		//				syssb.append("\n# SYS command. line " + sysst.getLineNum() + "\n\n");
		//
		//				// Get commands
		//				String commands = sysst.getCommands(bdsThread);
		//				syssb.append(commands);
		//				syssb.append("\n");
		//			}
		//
		//			if (!bdsThread.isCheckpointRecover()) {
		//				sys = ExpressionSys.get(parent, syssb.toString(), lineNum, charPosInLine);
		//			}
		//		} else {
		//			throw new RuntimeException("Unimplemented for class '" + statement.getClass().getSimpleName() + "'");
		//		}

		return sys;
	}

	@Override
	public boolean isReturnTypesNotNull() {
		return true;
	}

	@Override
	public boolean isStopDebug() {
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
	}

	/**
	 * Replace all occurrences in 'replace' map
	 */
	String replace(HashMap<String, String> replace, String sysCmds) {

		for (String key : replace.keySet()) {
			String sysCmdsPrev;
			do {
				sysCmdsPrev = sysCmds;
				sysCmds = replace(key, replace.get(key), sysCmds);
			} while (!sysCmdsPrev.equals(sysCmds)); // Continue while there are replacements
		}

		return sysCmds;
	}

	/**
	 * Replace a single instance of 'oldStr' by 'newStr'
	 */
	String replace(String oldStr, String newStr, String str) {
		int start = str.indexOf(oldStr);
		if (start < 0) return str; // Nothing found

		// Check that 'oldStr' is a separated / quoted word
		int end = start + oldStr.length();
		char prevChar = start > 0 ? str.charAt(start - 1) : '\0';
		char nextChar = end < str.length() ? str.charAt(end) : '\0';

		// Change if surrounded by spaces or quotes
		boolean change = false;
		if (prevChar == '\'' && nextChar == '\'') {
			// Surrounded by single quote
			change = true;
		} else if (prevChar == '"' && nextChar == '"') {
			// Surrounded by double quote
			change = true;
		} else if ((prevChar == ' ' || prevChar == '\t' || prevChar == '\n' || prevChar == '\0') && //
				(nextChar == ' ' || nextChar == '\t' || nextChar == '\n' || nextChar == '\0')) {
			// Surrounded by space, tab, newline or end_of_string
			change = true;
		}

		// Change oldStr by newStr?
		if (change) { //
			return str.substring(0, start) // Keep first
					+ newStr // Replace oldStr by newStr
					+ (nextChar == '\0' ? "" : str.substring(start + oldStr.length())) // Last part only if there is something after 'oldStr
			;
		}

		return str;
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

	//	/**
	//	 * Evaluate 'task' expression
	//	 */
	//	@Override
	//	public void runStep(BdsThread bdsThread) {
	//		// Evaluate task options (get a list of dependencies)
	//		TaskDependency taskDependency = null;
	//		if (options != null) {
	//			taskDependency = options.evalTaskDependency(bdsThread);
	//
	//			if (bdsThread.isCheckpointRecover()) return;
	//
	//			if (taskDependency == null) {
	//				// Task options clause not satisfied. Do not execute task => Return empty taskId
	//				if (bdsThread.isDebug()) log("Task dependency check (needsUpdate=false): null");
	//				bdsThread.push("");
	//				return;
	//			}
	//
	//			// Needs update?
	//			taskDependency.setDebug(bdsThread.isDebug());
	//			boolean needsUpdate = taskDependency.depOperator();
	//
	//			if (bdsThread.isDebug()) log("Task dependency check (needsUpdate=" + needsUpdate + "): " + taskDependency);
	//			if (!needsUpdate) {
	//				// Task options clause not satisfied. Do not execute task => Return empty taskId
	//				bdsThread.push("");
	//				return;
	//			}
	//		}
	//
	//		if (bdsThread.isCheckpointRecover()) return;
	//
	//		// Evaluate 'sys' statements
	//		ExpressionSys sys = evalSys(bdsThread);
	//
	//		// Create task
	//		Task task = createTask(bdsThread, taskDependency, sys);
	//
	//		// Schedule task for execution
	//		dispatchTask(bdsThread, task);
	//
	//		bdsThread.push(task.getId());
	//	}

	@Override
	public void sanityCheck(CompilerMessages compilerMessages) {
		// Sanity check options
		if (options != null) options.sanityCheck(compilerMessages);

		// Sanity check statements
		if (statement == null) {
			compilerMessages.add(this, "Task has empty statement", MessageType.ERROR);
			return;
		}

		List<BdsNode> statements = statement.findNodes(null, true, false);

		// No child nodes? Add the only node we have
		if (statements.isEmpty()) statements.add(statement);

		for (BdsNode node : statements) {
			if (node instanceof Statement) {
				boolean ok = node instanceof ExpressionSys //
						|| node instanceof Block //
						|| node instanceof Literal //
						|| node instanceof InterpolateVars //
						|| node instanceof Reference //
						|| node instanceof StatementExpr //
				;

				if (!ok) compilerMessages.add(this, "Only sys statements are allowed in a task (line " + node.getLineNum() + ")", MessageType.ERROR);
			}
		}
	}

	@Override
	public String toString() {
		return "task" //
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

		// Multiline
		return "{\n" //
				+ Gpr.prependEachLine("\t", statement.toString()) //
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
