package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessage.MessageType;
import ca.mcgill.mcb.pcingola.bigDataScript.compile.CompilerMessages;
import ca.mcgill.mcb.pcingola.bigDataScript.executioner.Executioner;
import ca.mcgill.mcb.pcingola.bigDataScript.executioner.Executioners;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.task.TaskDependency;
import ca.mcgill.mcb.pcingola.bigDataScript.task.TaskState;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * A 'task' expression
 *
 * @author pcingola
 */
public class ExpressionTask extends ExpressionWithScope {

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
	public static final String TASK_OPTION_TIMEOUT = "timeout";
	public static final String TASK_OPTION_WALL_TIMEOUT = "walltimeout";

	protected ExpressionTaskOptions taskOptions;
	protected Statement statement;

	/**
	 * Execute a task (schedule it into executioner)
	 */
	public static void execute(BigDataScriptThread bdsThread, Task task) {
		// Make sure the task in in initial state
		task.reset();

		// Select executioner and queue for execution
		String runSystem = bdsThread.getString(TASK_OPTION_SYSTEM);
		Executioner executioner = Executioners.getInstance().get(runSystem);

		// Queue exec
		if (bdsThread.getConfig().isDryRun()) {
			// Dry run: Don't run the task, just show what would be run
			Timer.showStdErr("Dry run task:\n" + task.toString(true, true));
			task.state(TaskState.SCHEDULED);
			task.state(TaskState.STARTED);
			task.state(TaskState.RUNNING);
			task.state(TaskState.FINISHED);
			task.setExitValue(0);
			bdsThread.add(task);
		} else {
			bdsThread.add(task);
			executioner.add(task);
		}
	}

	public ExpressionTask(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Create a task
	 */
	Task createTask(BigDataScriptThread bdsThread, TaskDependency taskDependency, ExpressionSys sys) {
		// Get an ID
		String execId = sys.execId("task", bdsThread);

		// Create Task
		Task task = new Task(execId, this, sys.getSysFileName(execId), sys.getCommands(bdsThread));

		// Configure Task parameters
		task.setVerbose(bdsThread.getConfig().isVerbose());
		task.setDebug(bdsThread.getConfig().isDebug());
		task.setCanFail(bdsThread.getBool(TASK_OPTION_CAN_FAIL));
		task.setAllowEmpty(bdsThread.getBool(TASK_OPTION_ALLOW_EMPTY));
		task.getResources().setCpus((int) bdsThread.getInt(TASK_OPTION_CPUS));
		task.getResources().setMem(bdsThread.getInt(TASK_OPTION_MEM));
		task.setNode(bdsThread.getString(TASK_OPTION_NODE));
		task.setQueue(bdsThread.getString(TASK_OPTION_QUEUE));
		task.setMaxFailCount((int) bdsThread.getInt(TASK_OPTION_RETRY) + 1); // Note: Max fail count is the number of retries plus one (we always run at least once)
		task.getResources().setTimeout(bdsThread.getInt(TASK_OPTION_TIMEOUT));
		task.getResources().setWallTimeout(bdsThread.getInt(TASK_OPTION_WALL_TIMEOUT));
		if (taskDependency != null) task.setTaskDependency(taskDependency);

		return task;
	}

	/**
	 * Dispatch task for execution
	 */
	void dispatchTask(BigDataScriptThread bdsThread, Task task) {
		execute(bdsThread, task);
	}

	/**
	 * Evaluate 'task' expression
	 */
	@Override
	public void runStep(BigDataScriptThread bdsThread) {
		// Evaluate task options (get a list of dependencies)
		TaskDependency taskDependency = null;
		if (taskOptions != null) {
			taskDependency = taskOptions.evalTaskDependency(bdsThread);
			if (bdsThread.isDebug()) log("task-options check " + (taskDependency != null ? taskDependency : "null"));
			if (taskDependency == null) {
				// Task options clause not satisfied. Do not execute task
				bdsThread.push("");
				return;
			}

			boolean needsUpdate = taskDependency.depOperator();
			if (!needsUpdate) {
				// Task options clause not satisfied. Do not execute task
				bdsThread.push("");
				return;
			}
		}

		// Evaluate 'sys' statements
		ExpressionSys sys = evalSys(bdsThread);

		// Create task
		Task task = createTask(bdsThread, taskDependency, sys);

		// Schedule task for execution
		dispatchTask(bdsThread, task);

		bdsThread.push(task.getId());
	}

	/**
	 * Evaluate 'sys' statements used to create task
	 */
	ExpressionSys evalSys(BigDataScriptThread bdsThread) {
		ExpressionSys sys = null;

		if (statement instanceof ExpressionSys) sys = (ExpressionSys) statement;
		else if (statement instanceof LiteralString) {
			LiteralString lstr = (LiteralString) statement;

			// Evaluate (e.g. interpolate variables)
			lstr.run(bdsThread);
			String str = bdsThread.pop().toString();

			sys = ExpressionSys.get(parent, str, lineNum, charPosInLine);
		} else if (statement instanceof Block) {
			// Create one sys statement for all sys statements in the block
			StringBuilder syssb = new StringBuilder();

			Block block = (Block) statement;
			for (Statement st : block.getStatements()) {
				ExpressionSys sysst = (ExpressionSys) st;
				syssb.append("\n# SYS command. line " + sysst.getLineNum() + "\n\n");
				String commands = sysst.getCommands(bdsThread);
				syssb.append(commands);
				syssb.append("\n");
			}

			sys = ExpressionSys.get(parent, syssb.toString(), lineNum, charPosInLine);
		}
		return sys;
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

			taskOptions = new ExpressionTaskOptions(this, null);
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
	protected void sanityCheck(CompilerMessages compilerMessages) {
		// Sanity check options
		if (taskOptions != null) taskOptions.sanityCheck(compilerMessages);

		// Sanity check statements
		List<BigDataScriptNode> statements = statement.findNodes(null, true);

		// No child nodes? Add the only node we have
		if (statements.isEmpty()) statements.add(statement);

		for (BigDataScriptNode node : statements) {
			if (node instanceof Statement) {
				boolean ok = node instanceof ExpressionSys //
						|| node instanceof Block //
						|| node instanceof LiteralString //
						|| node instanceof InterpolateVars //
						|| node instanceof Reference //
						;

				if (!ok) compilerMessages.add(this, "Only sys statements are allowed in a task (line " + node.getLineNum() + ")", MessageType.ERROR);
			}
		}
	}

	@Override
	public String toString() {
		return "task " + (taskOptions != null ? taskOptions : "") + " " + statement;
	}

	@Override
	protected void typeCheck(Scope scope, CompilerMessages compilerMessages) {
		returnType(scope);
		if (taskOptions != null) taskOptions.typeCheck(scope, compilerMessages);
	}

}
