package ca.mcgill.mcb.pcingola.bigDataScript.run;

import java.io.File;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import ca.mcgill.mcb.pcingola.bigDataScript.Config;
import ca.mcgill.mcb.pcingola.bigDataScript.exec.Executioner;
import ca.mcgill.mcb.pcingola.bigDataScript.exec.Executioners;
import ca.mcgill.mcb.pcingola.bigDataScript.exec.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.BigDataScriptNode;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Checkpoint;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.ProgramUnit;
import ca.mcgill.mcb.pcingola.bigDataScript.lang.Wait;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.Scope;
import ca.mcgill.mcb.pcingola.bigDataScript.scope.ScopeSymbol;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerialize;
import ca.mcgill.mcb.pcingola.bigDataScript.serialize.BigDataScriptSerializer;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * A threads used in a bigDataScript program
 * 
 * It has all information to run a program (scope, pc, run state, etc)
 * 
 * @author pcingola
 */
public class BigDataScriptThread extends Thread implements BigDataScriptSerialize {

	public static final int SLEEP_TIME = 250;

	private static int threadNumber = 1;

	String bigDataScriptThreadId;

	int bigDataScriptThreadNum;
	int checkPointRecoverNodeIdx;
	int exitValue;
	ProgramCounter pc;
	Scope scope;
	ProgramUnit programUnit;
	RunState runState;
	Object returnValue;
	HashMap<String, Task> tasks;
	Config config;
	Random random;

	/**
	 * Get an ID for a node
	 * @return
	 */
	protected synchronized static int bigDataScriptThreadId() {
		return threadNumber++;
	}

	public BigDataScriptThread() {
		bigDataScriptThreadNum = bigDataScriptThreadId();
		pc = new ProgramCounter();
		scope = Scope.getGlobalScope();
		runState = RunState.OK;
		tasks = new HashMap<String, Task>();
		random = new Random();
	}

	public BigDataScriptThread(ProgramUnit programUnit, Config config) {
		super();
		bigDataScriptThreadNum = bigDataScriptThreadId();
		pc = new ProgramCounter();
		scope = Scope.getGlobalScope();
		runState = RunState.OK;
		tasks = new HashMap<String, Task>();
		this.config = config;
		setProgram(programUnit);
		random = new Random();
	}

	/**
	 * Add a task 
	 * @param task
	 */
	public void add(Task task) {
		tasks.put(task.getId(), task);
	}

	/**
	 * Create a checkpoint
	 * @param checkpointFileName
	 */
	public String checkpoint(String checkpointFileName) {
		// Default file name
		if (checkpointFileName == null) checkpointFileName = programUnit.getFileName() + ".chp";

		BigDataScriptSerializer bdsSer = new BigDataScriptSerializer(checkpointFileName);
		bdsSer.save(this);

		return checkpointFileName;
	}

	/**
	 * Found a node (CHECKPOINT_RECOVER run state)
	 */
	public void checkpointRecoverFound() {
		checkPointRecoverNodeIdx++;
	}

	/**
	 * Is there a next node to find? (CHECKPOINT_RECOVER run state0 
	 */
	public boolean checkpointRecoverHasNextNode() {
		return pc.size() > checkPointRecoverNodeIdx;
	}

	/**
	 * Get next node to find (CHECKPOINT_RECOVER run state0 
	 */
	public int checkpointRecoverNextNode() {
		return pc.nodeId(checkPointRecoverNodeIdx);
	}

	/**
	 * Create a dir for all log files
	 */
	public void createLogDir() {
		String dirname = bigDataScriptThreadId;
		File logdir = new File(dirname);
		if (!logdir.exists()) logdir.mkdirs();

		// No logging? Delete on exit
		if ((config != null) && !config.isLog()) logdir.deleteOnExit();
	}

	public String getBigDataScriptThreadId() {
		return bigDataScriptThreadId;
	}

	/**
	 * Get variable's value as a bool
	 * @param varName
	 * @return
	 */
	public boolean getBool(String varName) {
		return (Boolean) getScope().getSymbol(varName).getValue();
	}

	public Config getConfig() {
		return config;
	}

	public int getExitValue() {
		return exitValue;
	}

	/**
	 * Get variable's value as an int
	 * @param varName
	 * @return
	 */
	public long getInt(String varName) {
		ScopeSymbol ssym = getScope().getSymbol(varName);
		return (Long) ssym.getValue();
	}

	/**
	 * Get variable's value as a java object
	 * @param varName
	 * @return
	 */
	public Object getObject(String varName) {
		return getScope().getSymbol(varName).getValue();
	}

	public ProgramCounter getPc() {
		return pc;
	}

	public BigDataScriptNode getProgramUnit() {
		return programUnit;
	}

	public Random getRandom() {
		return random;
	}

	/**
	 * Get variable's value as a real
	 * @param varName
	 * @return
	 */
	public double getReal(String varName) {
		return (Double) getScope().getSymbol(varName).getValue();
	}

	public Object getReturnValue() {
		return returnValue;
	}

	public RunState getRunState() {
		return runState;
	}

	public Scope getScope() {
		return scope;
	}

	/**
	 * Get variable's value as a string
	 * @param varName
	 * @return
	 */
	public String getString(String varName) {
		return getScope().getSymbol(varName).getValue().toString();
	}

	/**
	 * Get a task 
	 * @param taskId
	 * @return
	 */
	public Task getTask(String taskId) {
		return tasks.get(taskId);
	}

	/**
	 * Get all tasks
	 * @return
	 */
	public Collection<Task> getTasks() {
		return tasks.values();
	}

	/**
	 * Are we in CHECKPOINT_RECOVER mode?
	 * @return
	 */
	public boolean isCheckpointRecover() {
		return runState == RunState.CHECKPOINT_RECOVER;
	}

	/**
	 * Have all tasks finished executing?
	 * @return
	 */
	public boolean isTasksDone() {
		for (String taskId : tasks.keySet()) {
			if ((taskId == null) || taskId.isEmpty()) continue;

			Task task = getTask(taskId);
			if (task == null) continue;

			if (!task.isDone()) return false;
		}

		return true;

	}

	/**
	 * Kill one task 
	 */
	public void killTask(String taskId) {
		// Just send a kill to all Executioners
		for (Executioner executioner : Executioners.getInstance().getAll())
			executioner.kill(taskId);
	}

	/**
	 * Kill all tasks in the list
	 * @param taskIds
	 */
	@SuppressWarnings("rawtypes")
	public void killTasks(List taskIds) {
		// We are done when ALL tasks are done
		for (Object tid : taskIds)
			killTask(tid.toString());
	}

	/**
	 * Create a new scope
	 */
	public void newScope() {
		scope = new Scope(scope);
	}

	/**
	 * Back to old scope
	 */
	public void oldScope() {
		scope = scope.getParent();
	}

	@Override
	public void run() {
		createLogDir(); // Create log dir 

		// Run program
		RunState runState = programUnit.run(this);
		if (config != null && config.isVerbose()) System.err.println("Program execution finished (runState: '" + runState + "' )");

		// Implicit 'wait' statement at the end of the program
		if (!isTasksDone()) System.err.println("Waiting for tasks to finish.");
		boolean ok = waitTasksAll();

		// All tasks in wait finished OK?
		if (!ok) {
			// Errors? Then set exit status appropriately
			exitValue = 1;
		} else {
			// Set exit value as the latest 'int' result
			Object ev = getReturnValue();
			if (ev instanceof Long) exitValue = (int) ((long) ((Long) ev)); // Yes, it's a very weird cast....
		}

		try {
			// Small sleep to collect latest outputs
			sleep(SLEEP_TIME);
		} catch (InterruptedException e) {
		}
	}

	@Override
	public void serializeParse(BigDataScriptSerializer serializer) {
		bigDataScriptThreadNum = (int) serializer.getNextFieldInt();

		// Update global thread number?
		if (threadNumber < bigDataScriptThreadNum) threadNumber = bigDataScriptThreadNum + 1;
	}

	@Override
	public String serializeSave(BigDataScriptSerializer serializer) {
		StringBuilder out = new StringBuilder();

		out.append(getClass().getSimpleName() + "\t" + bigDataScriptThreadNum + "\n");

		// Save program counter
		out.append(pc.serializeSave(serializer));

		// Save scopes
		out.append(scope.serializeSave(serializer));

		// Save program nodes
		out.append(programUnit.serializeSave(serializer));

		// Save all task nodes
		for (Task task : tasks.values())
			out.append(task.serializeSave(serializer));

		return out.toString();
	}

	public void setExitValue(long exitValue) {
		this.exitValue = (int) exitValue;
	}

	public void setPc(ProgramCounter pc) {
		this.pc = pc;
	}

	/**
	 * Ser program unit and update bigDataScriptThreadId"
	 * @param programUnit
	 */
	public void setProgram(ProgramUnit programUnit) {
		this.programUnit = programUnit;

		// Create ID
		String name = Gpr.baseName(programUnit.getFileName());
		bigDataScriptThreadId = String.format("%s.%2$tY%2$tm%2$td_%2$tH%2$tM%2$tS_%2$tL", name, Calendar.getInstance());
	}

	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}

	public void setRunState(RunState runState) {
		this.runState = runState;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	/**
	 * Should we run this node?
	 * @param node
	 * @return
	 */
	public boolean shouldRun(BigDataScriptNode node) {
		// Not int checkpoint recovery mode? => Run 
		if (!isCheckpointRecover()) return true;

		// Which node are we looking for?
		int nodeNum = checkpointRecoverNextNode();
		if (node.getId() == nodeNum) {
			// Node found!
			checkpointRecoverFound();

			// More nodes to recurse? => continue
			if (checkpointRecoverHasNextNode()) return true;

			// Last node found!
			runState = RunState.OK; // Switch to 'normal' run state
			if (node instanceof Wait) {
				runState = RunState.WAIT_RECOVER; // We want to recover all tasks that failed in wat instructions
			}

			if (node instanceof Checkpoint) return false; // We want to recover AFTER the checkpoint
			return true;
		}

		return false;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("BigDataScriptThread: " + bigDataScriptThreadNum + "\n");
		sb.append("\tPC        : " + pc + "\n");
		sb.append("\tRun state : " + runState + "\n");
		sb.append("\tScope     : " + scope + "\n");
		sb.append("\tProgram   :\n" + programUnit.toStringTree("\t\t", "program") + "\n");
		return sb.toString();
	}

	/**
	 * Wait for one task to finish
	 * @return true if task finished OK or it was allowed to fail (i.e. canFail = true)  
	 */
	public boolean waitTask(String taskId) {
		if ((taskId == null) || taskId.isEmpty()) return true;

		Task task = getTask(taskId);
		if (task == null) return false; // No task? We are done!

		// Wait
		while (!task.isDone()) {
			// Sleep a little bit
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// Either finished OK or it was allowed to fail
		boolean ok = task.isDoneOk() || task.isCanFail();

		// If task failed, show task information and failure reason.
		if (!ok) {
			if (config.isVerbose()) System.err.println("Task failed:\n" + task);
			else System.err.println("Task failed: " + task);
		}

		return ok;
	}

	/**
	 * Wait for all tasks in the list to finish
	 * @return true if all tasks finished OK or it were allowed to fail (i.e. canFail = true)  
	 */
	@SuppressWarnings("rawtypes")
	public boolean waitTasks(List taskIds) {
		boolean ok = true;

		// We are done when ALL tasks are done
		for (Object tid : taskIds)
			ok &= waitTask(tid.toString());

		return ok;
	}

	/**
	 * Wait for all tasks to finish
	 * @return true if all tasks finished OK or it were allowed to fail (i.e. canFail = true)  
	 */
	public boolean waitTasksAll() {
		// Wait for all tasks to finish
		boolean ok = true;

		for (String tid : tasks.keySet())
			ok &= waitTask(tid);

		return ok;
	}

}
