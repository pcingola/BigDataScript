package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.executioner.Executioner;
import ca.mcgill.mcb.pcingola.bigDataScript.executioner.Executioners;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.RunState;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * A 'task' statement (to execute a command line in a node)
 * 
 * @author pcingola
 */
public class ExpressionTask extends ExpressionWithScope {

	// Variable names
	public static final String TASK_OPTION_CPUS = "cpus";
	public static final String TASK_OPTION_CAN_FAIL = "canFail";
	public static final String TASK_OPTION_NODE = "node";
	public static final String TASK_OPTION_QUEUE = "queue";
	public static final String TASK_OPTION_SYSTEM = "system";
	public static final String TASK_OPTION_TIMEOUT = "timeout";
	public static final String TASK_OPTION_PHYSICAL_PATH = "ppwd";

	TaskOptions taskOptions;
	Statement statement;
	private String execId = "";
	ArrayList<String> outputFiles;

	public ExpressionTask(BigDataScriptNode parent, ParseTree tree) {
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
	 * Execute a sys command created by this task
	 * @param csThread
	 * @param sys
	 */
	Task exec(BigDataScriptThread csThread, ExpressionSysOld sys) {
		// Get an ID
		execId = sys.execId("task", csThread);

		// Select executioner and queue for execution
		String runSystem = csThread.getString(TASK_OPTION_SYSTEM);
		Executioner executioner = Executioners.getInstance().get(runSystem);

		// Create Task
		Task task = new Task(execId, sys.getSysFileName(), sys.getCommands(csThread), getFileName(), getLineNum());

		// Configure Task parameters
		task.setVerbose(csThread.getConfig().isVerbose());
		task.setDebug(csThread.getConfig().isDebug());
		task.setCanFail(csThread.getBool(TASK_OPTION_CAN_FAIL));
		task.setNode(csThread.getString(TASK_OPTION_NODE));
		task.setQueue(csThread.getString(TASK_OPTION_QUEUE));
		task.getResources().setCpus((int) csThread.getInt(TASK_OPTION_CPUS));
		task.getResources().setTimeout(csThread.getInt(TASK_OPTION_TIMEOUT));
		task.setOutputFiles(outputFiles);

		// Queue exec
		csThread.add(task);
		executioner.add(task);

		return task;
	}

	@Override
	protected boolean isReturnTypesNotNull() {
		return true;
	}

	@Override
	protected void parse(ParseTree tree) {
		int idx = 0;
		idx++; // 'task' keyword

		// Do we have any task options?
		if (tree.getChild(idx).getText().equals("(")) {
			int lastIdx = indexOf(tree, ")");

			taskOptions = new TaskOptions(this, null);
			taskOptions.parse(tree, ++idx, lastIdx);
			idx = lastIdx + 1; // Skip last ')'
		}

		statement = (Statement) factory(tree, idx++); // Parse statement
	}

	/**
	 * Task expression always returns the task id, which is a string
	 */
	@Override
	public Type returnType(Scope scope) {
		// Calculate options' return type
		if (taskOptions != null) taskOptions.returnType(scope);

		// Task expressions return a task ID (a string)
		returnType = Type.STRING;
		return returnType;
	}

	@Override
	protected RunState runStep(BigDataScriptThread csThread) {

		// Execute options assignments
		if (taskOptions != null) {
			boolean ok = (Boolean) taskOptions.eval(csThread);
			outputFiles = taskOptions.outputFiles(); // This has to be done AFTER evaluation
			if (!ok) {
				// Return empty task ID
				return RunState.OK; // Task options clause not satisfied. Do not execute task 
			}
		}

		//---
		// Execute statements
		//---
		ExpressionSysOld sys = null;
		StringBuilder allCmds = new StringBuilder();

		if (statement instanceof ExpressionSysOld) sys = (ExpressionSysOld) statement;
		else if (statement instanceof LiteralString) {
			LiteralString lstr = (LiteralString) statement;
			allCmds.append(lstr.getValue() + "\n");
			sys = ExpressionSysOld.get(parent, lstr.getValue(), lineNum, charPosInLine);
		} else if (statement instanceof Block) {
			// Create one sys statement for all sys statements in the block
			StringBuilder syssb = new StringBuilder();

			Block block = (Block) statement;
			for (Statement st : block.getStatements()) {
				ExpressionSysOld sysst = (ExpressionSysOld) st;
				syssb.append("\n# SYS command. line " + sysst.getLineNum() + "\n\n");
				String commands = sysst.getCommands(csThread);
				syssb.append(commands);
				allCmds.append(commands + "\n");
				syssb.append("\n");
			}

			sys = ExpressionSysOld.get(parent, syssb.toString(), lineNum, charPosInLine);
		}

		// Execute
		if (csThread.getConfig().isVerbose()) Timer.showStdErr("Task, line " + getLineNum() + "\n" + allCmds);
		exec(csThread, sys);

		return RunState.OK;
	}

	@Override
	protected void sanityCheck(CompilerMessages compilerMessages) {
		//---
		// Sanity check options
		//---
		if (taskOptions != null) taskOptions.sanityCheck(compilerMessages);

		//---
		// Sanity check statements
		//---
		List<BigDataScriptNode> statements = statement.findNodes(null, true);

		// No child nodes? Add the only node we have
		if (statements.isEmpty()) statements.add(statement);

		for (BigDataScriptNode node : statements) {
			if (node instanceof Statement) {
				if (!(node instanceof ExpressionSysOld) //
						&& !(node instanceof Block) //
						&& !(node instanceof LiteralString) //
				) {
					compilerMessages.add(this, "Only sys statements are allowed in a task (line " + node.getLineNum() + ")", MessageType.ERROR);
				}
			}
		}
	}

	@Override
	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		returnType(scope);
		if (taskOptions != null) taskOptions.typeCheck(scope, compilerMessages);
	}

}
