package org.bds.task;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bds.BdsLog;
import org.bds.Config;
import org.bds.data.Data;
import org.bds.data.DataTask;
import org.bds.report.Report;
import org.bds.run.BdsThread;
import org.bds.util.AutoHashMap;

/**
 * Store task and dependency graph (i.e. all task dependencies)
 *
 * @author pcingola
 */
public class TaskDependecies implements Serializable, BdsLog {

	private static final long serialVersionUID = -7139051739077288915L;
	public static final int SLEEP_TIME = 200;
	private static TaskDependecies taskDependeciesInstance = new TaskDependecies(); // Global instance (keeps track of all tasks)

	boolean debug = false;
	boolean verbose = false;
	List<Task> tasks; // Sorted list of tasks (need it for serialization purposes)
	Map<String, Task> tasksById;
	AutoHashMap<Data, List<Task>> tasksByOutput;
	Map<String, String> canonicalPath;

	public static TaskDependecies get() {
		return taskDependeciesInstance;
	}

	/**
	 * Create a new Singleton
	 */
	public static void reset() {
		taskDependeciesInstance = new TaskDependecies();
	}

	public TaskDependecies() {
		canonicalPath = new HashMap<>();
		tasksByOutput = new AutoHashMap<>(new LinkedList<Task>());
		tasksById = new HashMap<>();
		tasks = new ArrayList<>();
	}

	/**
	 * Add a task
	 */
	public synchronized void add(Task task) {
		// Sanity check: Circular dependency
		if (isCircular(task)) throw new RuntimeException("Circular dependency on task '" + task.getId() + "'");

		// Add task
		addTask(task);

		// Find and update task's immediate dependencies (only if the task is to be executed)
		if (!task.isDependency()) findDirectDependencies(task);

		// Add to glabal object
		if (!isGlobal()) TaskDependecies.get().add(task);
	}

	/**
	 * Add a task to collections
	 */
	protected synchronized void addTask(Task task) {
		// Already added? Nothing to do
		if (tasksById.containsKey(task.getId())) return;

		// Add task by ID
		tasksById.put(task.getId(), task);

		// Add task
		synchronized (tasks) {
			tasks.add(task);
		}

		// Add task by output files
		if (task.getOutputs() != null) {
			for (Data outFile : task.getOutputs())
				addTaskByOutput(outFile, task);
		}
	}

	/**
	 * Add to 'taskByOutput' map
	 */
	protected synchronized void addTaskByOutput(Data outFile, Task task) {
		tasksByOutput.getOrCreate(outFile).add(task);
	}

	/**
	 * Count how many tasks failed.
	 *
	 * Note: Only count tasks that are not supposed
	 *       to fail (canFail = false)
	 */
	public synchronized int countTaskFailed() {
		int count = 0;

		for (Task task : tasksById.values()) {
			if (task.isFailed() && !task.isCanFail()) count++;
		}

		return count;
	}

	/**
	 * Find all tasks that must be completed before 'task' is executed
	 */
	void findDirectDependencies(Task task) {
		if (!task.isDone()) {
			// Add input dependencies based on input files
			if (task.getInputs() != null) {
				for (Data in : task.getInputs()) {
					List<Task> taskDeps = getTasksByOutput(in);

					if (taskDeps != null) {
						for (Task taskDep : taskDeps)
							if (taskDep.isDetached() && !isTask(in)) {
								// If a task is detached, we cannot add their output files.
								// Why? Because by definition, we don't know when a detached task
								// is finished, so we don't know when the output is created.
								throw new RuntimeException("Detached task output files cannot be used as dependencies. Task ID '" + task.getId() + "', output file '" + in + "'");
							} else if (!taskDep.isDone() && !taskDep.isDependency()) {
								// Don't add finished tasks (they are no longer a dependency)
								// If task is a dependency, it may not be executed (because the goal is not triggered). So don't add them
								// Add it to dependency list
								task.addDependency(taskDep);
							}
					}
				}
			}
		}
	}

	/**
	 * Find 'leaf' nodes (i.e. nodes that do not have dependent tasks)
	 */
	Set<Data> findLeafNodes(Data out) {
		Set<Data> nodes = findNodes(out); // Find all nodes, including 'out'

		// Only add nodes that do not have dependent tasks (i.e. are leaves)
		Set<Data> leaves = new HashSet<>();
		for (Data n : nodes) {
			if (!hasTasksByOutput(n)) leaves.add(n);
		}

		return leaves;
	}

	/**
	 * Find all leaf nodes required for goal 'out'
	 */
	Set<Data> findNodes(Data out) {
		// A set of 'goal' nodes
		Set<Data> goals = new HashSet<>();
		goals.add(out); // We need 'out' in goals, otherwise the evaluation in empty, we'll remove it at the end

		// Loop until there is no change in this set
		int oldSize, newSize;
		do {
			Set<Data> newGoals = findNodes(goals);
			oldSize = goals.size();
			newSize = newGoals.size();
			goals = newGoals;
		} while (oldSize < newSize);

		// Original 'out' node is not a leaf dependency
		goals.remove(out);
		return goals;
	}

	/**
	 * Create a new set including all dependencies from each goal
	 */
	Set<Data> findNodes(Set<Data> goals) {
		// We need a new set to avoid 'concurrent modification'
		Set<Data> newGoals = new HashSet<>();
		newGoals.addAll(goals);

		// For each goal
		for (Data goal : goals) {
			// Find all tasks required for this goal
			List<Task> tasks = getTasksByOutput(goal);
			if (tasks == null) continue;

			// Add all task's inputs
			for (Task t : tasks) {
				// Add all inputs
				if (t.getInputs() != null) {
					for (Data in : t.getInputs())
						newGoals.add(in); // Add each node
				}

				// Add all taskIds
				List<Task> depTasks = t.getDependencies();
				if (depTasks != null) {
					for (Task depTask : depTasks) {
						// Add each task ID, mark as changed if not already added
						DataTask dtask = new DataTask(depTask.getId());
						newGoals.add(dtask);
					}
				}
			}
		}

		return newGoals;
	}

	protected Task getTask(Data dataTask) {
		return getTask(dataTask.getUrlOri());
	}

	public synchronized Task getTask(String taskId) {
		return tasksById.get(taskId);
	}

	public synchronized Collection<String> getTaskIds() {
		return tasksById.keySet();
	}

	public Collection<Task> getTasks() {
		List<Task> tasksCopy = new ArrayList<>();

		synchronized (tasks) {
			tasksCopy.addAll(tasks);
		}

		return tasksCopy;
	}

	/**
	 * Get all tasks that output this 'output' as either a data file or a task ID
	 */
	protected List<Task> getTasksByOutput(Data output) {
		// Check if 'output' is a task Id
		Task taskGoal = getTask(output);
		if (taskGoal != null) {
			// Task ID found. Has the task been executed and finished? No need to add it.
			if (taskGoal.isDone()) return null;

			// New list containing the task
			LinkedList<Task> taskList = new LinkedList<>();
			taskList.add(taskGoal);
			return taskList;
		}

		// Check 'output' as a file
		return tasksByOutput.get(output);
	}

	/**
	 * Find tasks required to achieve goal 'out'
	 */
	public synchronized Set<Task> goal(BdsThread bdsThread, String out) {
		Set<Task> tasks = new HashSet<>();
		Data outd = bdsThread.data(out);
		goalRun(bdsThread, outd, tasks);
		return tasks;
	}

	/**
	 * Does this goal need to be updated respect to the leaves
	 */
	boolean goalNeedsUpdate(Data out) {
		// Is 'out' a taskId?
		if (isTask(out)) {
			// Needs update if the task has not been scheduled
			Task t = getTask(out);
			return t != null && !t.isScheduled();
		}

		// Find all 'leaf nodes' (files) required for this goal
		Set<Data> leaves = findLeafNodes(out);
		TaskDependency tasDep = new TaskDependency(null);
		tasDep.addOutput(out);
		tasDep.addInputs(leaves);

		if (debug) {
			debug("Goal: " + out + "\n\tLeaf nodes:");
			for (Data n : leaves)
				System.err.println("\t\t'" + n + "'");
		}

		return tasDep.depOperator();
	}

	/**
	 * Find all tasks required for satisfying goal 'out' and run them
	 */
	boolean goalRun(BdsThread bdsThread, Data goal, Set<Task> addedTasks) {
		// Check if we really need to update this goal with respect to the leaf nodes
		// Important: If the middle nodes are not present (e.g. temp files have
		// been deleted), but the output nodes are ok respect to first input
		// nodes (i.e. the input nodes), then we don't need to execute this goal
		if (!goalNeedsUpdate(goal)) {
			debug("Goal '" + goal + "' does not need update, skipping");
			return false;
		}

		// Find all tasks that must be executed
		List<Task> tasks = getTasksByOutput(goal);
		if (tasks == null) return false;

		// Satisfy all goals before running
		for (Task t : tasks) {
			if (addedTasks.contains(t)) continue; // Don't analyze twice
			addedTasks.add(t);

			// Recursively analyze all input file dependencies
			if (t.getInputs() != null) {
				for (Data in : t.getInputs()) {
					goalRun(bdsThread, in, addedTasks);
				}
			}

			// Recursively analyze all task dependencies
			if (t.getDependencies() != null) {
				for (Task taskDep : t.getDependencies()) {
					if (addedTasks.contains(taskDep)) continue;
					DataTask dtask = new DataTask(taskDep.getId());
					goalRun(bdsThread, dtask, addedTasks);
				}
			}
		}

		// Run all tasks required to satisfy the goal
		for (Task t : tasks) {
			if (!t.isScheduled()) {
				debug("Goal run: Running task '" + t.getId() + "'");
				t.setDependency(false); // We are executing this task, so it it no longer a 'dep'
				TaskVmOpcode.execute(bdsThread, t);
			} else debug("Goal run: Task '" + t.getId() + "' is already scheduled, not running");

		}

		return true;
	}

	public synchronized boolean hasTask(String taskId) {
		return tasksById.containsKey(taskId);
	}

	/**
	 * Do we have any entries on this outFile?
	 */
	boolean hasTasksByOutput(Data outFile) {
		return tasksByOutput.containsKey(outFile);
	}

	/**
	 * Have all tasks finished executing?
	 */
	public synchronized boolean isAllTasksDone() {
		for (String taskId : getTaskIds()) {
			if ((taskId == null) || taskId.isEmpty()) continue;

			Task task = getTask(taskId);
			if (task == null) continue;

			if (!task.isDone()) return false;
		}

		return true;
	}

	/**
	 * Is there a circular dependency for this task?
	 */
	boolean isCircular(Task task) {
		return isCircular(task, new HashSet<Task>());
	}

	/**
	 * Is there a circular dependency for this task?
	 */
	@SuppressWarnings("unchecked")
	boolean isCircular(Task task, HashSet<Task> tasks) {
		if (!tasks.add(task)) return true;

		// Get all input files, find corresponding tasks produce them as outputs and recurse
		if (task.getInputs() != null) {
			for (Data in : task.getInputs()) {
				// Add all tasks that have 'in' as an output
				List<Task> depTasks = getTasksByOutput(in);
				if (depTasks == null) continue;
				for (Task t : depTasks) {
					// Note: We clone the set because another branch might have the same dependency (in that case it is not circular)
					HashSet<Task> newTasks = (HashSet<Task>) tasks.clone();
					if (isCircular(t, newTasks)) return true;
				}
			}
		}

		// Get all dependent tasks, find circular dependencies
		if (task.getDependencies() != null) {
			for (Task t : task.getDependencies()) {
				HashSet<Task> newTasks = (HashSet<Task>) tasks.clone();
				if (isCircular(t, newTasks)) return true;
			}
		}

		return false;
	}

	@Override
	public boolean isDebug() {
		return debug;
	}

	/**
	 * Is this the global instance?
	 */
	boolean isGlobal() {
		return this == taskDependeciesInstance;
	}

	public synchronized boolean isTask(Data taskId) {
		return tasksById.containsKey(taskId.getUrlOri());
	}

	@Override
	public boolean isVerbose() {
		return verbose;
	}

	/**
	 * Resolve un-serialization
	 */
	private Object readResolve() throws ObjectStreamException {
		taskDependeciesInstance = this;
		return this;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public synchronized int size() {
		return tasksById.size();
	}

	void sleep() {
		try {
			Thread.sleep(SLEEP_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * A string of at most 'num' task names of tasks that failed
	 */
	public synchronized String taskFailedNames(int num, String sep) {
		StringBuilder sb = new StringBuilder();
		for (Task task : tasksById.values()) {
			if (task.isFailed() && !task.isCanFail()) {
				if (sb.length() > 0) sb.append(sep);
				sb.append(task.getName());
			}
		}

		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		ArrayList<Data> outs = new ArrayList<>();
		outs.addAll(tasksByOutput.keySet());
		Collections.sort(outs);

		for (Data out : outs) {
			sb.append(out + ":\n");
			for (Task t : tasksByOutput.get(out))
				sb.append("\t" + t.getId() + "\n");
		}
		return sb.toString();
	}

	/**
	 * Wait for one task to finish
	 * @return true if task finished OK or it was allowed to fail (i.e. canFail = true)
	 */
	public synchronized boolean waitTask(String taskId) {
		if ((taskId == null) || taskId.isEmpty()) return true;

		Task task = getTask(taskId);
		if (task == null) return false; // No task? We are done!

		// Is task a dependency?
		if (task.isDependency() && !task.isScheduled()) {
			debug("Wait: Task '" + task.getId() + "' is dependency and has not been scheduled for execution. Not wating.");
			return true;
		}

		debug("Wait: Waiting for task to finish: " + task.getId() + ", state: " + task.getTaskState());

		// Wait for task to finish
		while (!task.isDone()) {
			sleep();
			if (Config.get().isLog()) {
				Report.reportTime();
			}
		}

		// Either finished OK or it was allowed to fail
		boolean ok = task.isDoneOk() || task.isCanFail();

		// If task failed, show task information and failure reason.
		if (!ok) {
			// Show error and mark all files to be deleted on exit
			error("Task failed:\n" + task.toString(true));
			task.deleteOutputFilesOnExit();
		}

		debug("Wait: Task '" + task.getId() + "' finished.");

		return ok;
	}

	/**
	 * Wait for all tasks to finish
	 * @return true if all tasks finished OK or it were allowed to fail (i.e. canFail = true)
	 */
	public synchronized boolean waitTasksAll() {
		// Wait for all tasks to finish
		boolean ok = true;

		if (!isAllTasksDone()) debug("Waiting for all tasks to finish.");

		// Get all taskIds in a new collection (to avoid concurrent modification
		LinkedList<String> tids = new LinkedList<>();
		tids.addAll(getTaskIds());

		// Wait for each task
		for (String tid : tids) {
			ok &= waitTask(tid);
		}

		return ok;
	}

}
