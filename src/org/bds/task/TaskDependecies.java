package org.bds.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bds.Config;
import org.bds.data.Data;
import org.bds.lang.ExpressionTask;
import org.bds.report.Report;
import org.bds.run.BdsThread;
import org.bds.util.AutoHashMap;
import org.bds.util.Timer;

/**
 * Store task and dependency graph
 *
 * @author pcingola
 */
public class TaskDependecies {

	public static final int SLEEP_TIME = 250;

	private static TaskDependecies taskDependecies = new TaskDependecies(); // Global instance (keeps track of all tasks)

	boolean debug = false;
	boolean verbose = false;
	List<Task> tasks; // Sorted list of tasks (need it for serialization purposes)
	Map<String, Task> tasksById;
	AutoHashMap<String, List<Task>> tasksByOutput;
	HashMap<String, String> canonicalPath;

	public static TaskDependecies get() {
		return taskDependecies;
	}

	/**
	 * Create a new Singleton
	 */
	public static void reset() {
		taskDependecies = new TaskDependecies();
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
			for (String outFile : task.getOutputs())
				addTaskByOutput(outFile, task);
		}
	}

	/**
	 * Add to 'taskByOutput' map
	 */
	protected synchronized void addTaskByOutput(String outFile, Task task) {
		// Use canonical paths
		String outPath = getCanonicalPath(outFile);

		// Add to map
		tasksByOutput.getOrCreate(outPath).add(task);
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
	 * Find all tasks that must be completed before we proceed
	 */
	void findDirectDependencies(Task task) {
		if (!task.isDone()) {
			// Add input dependencies based on input files
			if (task.getInputs() != null) {
				for (String inFile : task.getInputs()) {
					List<Task> taskDeps = getTasksByOutput(inFile);
					if (taskDeps != null) {
						for (Task taskDep : taskDeps)
							if (!taskDep.isDone() // Don't add finished tasks
									&& !taskDep.isDependency() // If task is a dependency, it may not be executed (because the goal is not triggered). So don't add them
							) task.addDependency(taskDep); // Add it to dependency list
					}
				}
			}
		}
	}

	/**
	 * Find 'leaf' nodes (i.e. nodes that do not have dependent tasks)
	 */
	Set<String> findLeafNodes(String out) {
		Set<String> nodes = findNodes(out); // Find all nodes

		// Only add nodes that do not have dependent tasks (i.e. are leaves)
		Set<String> leaves = new HashSet<>();
		for (String n : nodes) {
			if (!hasTasksByOutput(n)) leaves.add(n);
		}

		return leaves;
	}

	/**
	 * Find all leaf nodes required for goal 'out'
	 */
	Set<String> findNodes(String out) {
		// A set of 'goal' nodes
		Set<String> goals = new HashSet<>();
		goals.add(out);

		// For each goal
		for (boolean changed = true; changed;) {
			changed = false;

			// We need a new set to avoid 'concurrent modification' exception
			Set<String> newGoals = new HashSet<>();
			newGoals.addAll(goals);

			// For each goal
			for (String goal : goals) {
				// Find all tasks required for this goal
				List<Task> tasks = getTasksByOutput(goal);

				if (tasks != null) // Add all task's input files
					for (Task t : tasks) {
					// Add all input files
					if (t.getInputs() != null) {
					for (String in : t.getInputs())
					changed |= newGoals.add(in); // Add each node
					}

					// Add all task Ids
					List<Task> depTasks = t.getDependencies();
					if (depTasks != null) {
					for (Task dt : depTasks)
					changed |= newGoals.add(dt.getId()); // Add each task ID
					}
					}
			}

			goals = newGoals;
		}

		return goals;
	}

	/**
	 * Find canonical path (cache return values)
	 */
	String getCanonicalPath(String fileName) {
		String filePath = canonicalPath.get(fileName);

		// Not found? => Populate map
		if (filePath == null) {
			Data f = Data.factory(fileName);
			filePath = f.getPath();

			// Add to map
			canonicalPath.put(fileName, filePath);
		}

		return filePath;
	}

	public synchronized Task getTask(String taskId) {
		return tasksById.get(taskId);
	}

	public Task getTaskNoSync(String taskId) {
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
	 * Get all tasks that output this outFile
	 */
	protected List<Task> getTasksByOutput(String output) {
		//---
		// Check if 'output' is a task Id
		//---
		Task taskGoal = getTask(output);
		if (taskGoal != null) {
			// Task ID found. Has the task been executed and finished? No need to add it.
			if (taskGoal.isDone()) return null;

			// New list containing the task
			LinkedList<Task> taskList = new LinkedList<>();
			taskList.add(taskGoal);
			return taskList;
		}

		//---
		// Check 'output' as a file
		//---
		// Use canonical paths
		String outPath = getCanonicalPath(output);

		// Get list
		return tasksByOutput.get(outPath);
	}

	/**
	 * Find tasks required to achieve goal 'out'
	 */
	public synchronized Set<Task> goal(BdsThread bdsThread, String out) {
		Set<Task> tasks = new HashSet<>();
		goalRun(bdsThread, out, tasks);
		return tasks;
	}

	/**
	 * Does this goal need to be updated respect to the leaves
	 */
	boolean goalNeedsUpdate(String out) {
		// Find all 'leaf nodes' (files) required for this goal
		Set<String> leaves = findLeafNodes(out);
		TaskDependency tasDep = new TaskDependency(null);
		tasDep.addOutput(out);
		tasDep.addInput(leaves);

		if (debug) {
			Timer.showStdErr("Goal: " + out + "\n\tLeaf nodes:");
			for (String n : leaves)
				System.err.println("\t\t'" + n + "'");
		}

		return tasDep.depOperator();
	}

	/**
	 * Find all leaf nodes required for goal 'out'
	 */
	boolean goalRun(BdsThread bdsThread, String goal, Set<Task> addedTasks) {

		// Check if we really need to update this goal (with respect to the leaf nodes)
		if (!goalNeedsUpdate(goal)) return false;

		List<Task> tasks = getTasksByOutput(goal);
		if (tasks == null) return false;

		// Satisfy all goals before running
		for (Task t : tasks) {
			if (addedTasks.contains(t)) continue; // Don't add twice
			addedTasks.add(t);

			// Add file dependencies
			if (t.getInputs() != null) {
				for (String in : t.getInputs())
					goalRun(bdsThread, in, addedTasks);
			}

			// Add task dependencies
			if (t.getDependencies() != null) {
				for (Task tt : t.getDependencies())
					if (!addedTasks.contains(tt)) // Not added yet?
						goalRun(bdsThread, tt.getId(), addedTasks);
			}

		}

		// Run all tasks
		for (Task t : tasks) {
			t.setDependency(false); // We are executing this task, so it it no long a 'dep'
			ExpressionTask.execute(bdsThread, t);
		}

		return true;
	}

	public synchronized boolean hasTask(String taskId) {
		return tasksById.containsKey(taskId);
	}

	/**
	 * Do we have any entries on this outFile?
	 */
	boolean hasTasksByOutput(String outFile) {
		String outPath = getCanonicalPath(outFile);
		return tasksByOutput.containsKey(outPath);
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
		boolean circ = isCircular(task, new HashSet<Task>());
		return circ;
	}

	/**
	 * Is there a circular dependency for this task?
	 */
	@SuppressWarnings("unchecked")
	boolean isCircular(Task task, HashSet<Task> tasks) {
		if (!tasks.add(task)) return true;

		// Get all input files, find corresponding tasks and recurse
		if (task.getInputs() != null) {
			for (String in : task.getInputs()) {
				List<Task> depTasks = getTasksByOutput(in);

				if (depTasks != null) {
					for (Task t : depTasks) {
						HashSet<Task> newTasks = (HashSet<Task>) tasks.clone();
						if (isCircular(t, newTasks)) return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Is this the global instance?
	 */
	boolean isGlobal() {
		return this == taskDependecies;
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

		ArrayList<String> outs = new ArrayList<>();
		outs.addAll(tasksByOutput.keySet());
		Collections.sort(outs);

		for (String out : outs) {
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
			if (debug) Timer.showStdErr("Wait: Task '" + task.getId() + "' is dependency and has not been scheduled for execution. Not wating.");
			return true;
		}

		if (verbose) Timer.showStdErr("Wait: Waiting for task to finish: " + task.getId() + ", state: " + task.getTaskState());

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
			System.err.println("Task failed:\n" + task.toString(true));
			task.deleteOutputFilesOnExit();
		}

		if (verbose) Timer.showStdErr("Wait: Task '" + task.getId() + "' finished.");

		return ok;
	}

	/**
	 * Wait for all tasks to finish
	 * @return true if all tasks finished OK or it were allowed to fail (i.e. canFail = true)
	 */
	public synchronized boolean waitTasksAll() {
		// Wait for all tasks to finish
		boolean ok = true;

		if (verbose && !isAllTasksDone()) Timer.showStdErr("Waiting for all tasks to finish.");

		// Get all taskIds in a new collection (to avoid concurrent modification
		LinkedList<String> tids = new LinkedList<>();
		tids.addAll(getTaskIds());

		// Wait for each task
		for (String tid : tids)
			ok &= waitTask(tid);

		return ok;
	}

}
