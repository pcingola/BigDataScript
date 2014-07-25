package ca.mcgill.mcb.pcingola.bigDataScript.run;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ca.mcgill.mcb.pcingola.bigDataScript.lang.ExpressionDepOperator;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.util.AutoHashMap;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * Store task and dependency graph
 *
 * @author pcingola
 */
public class TaskDependecies {

	boolean debug = true;
	AutoHashMap<String, List<Task>> tasksByOutput;

	public TaskDependecies() {
		tasksByOutput = new AutoHashMap<String, List<Task>>(new LinkedList<Task>());
	}

	/**
	 * Add a task
	 */
	public synchronized void add(Task task) {
		addTask(task);

		// Find and update task's immediate dependencies
		findDirectDependencies(task);
	}

	/**
	 * Add dependency
	 */
	public synchronized void addDep(Task task) {
		addTask(task);
	}

	/**
	 * Add a task
	 */
	synchronized void addTask(Task task) {
		// Add output files
		if (task.getOutputFiles() != null) {
			for (String outFile : task.getOutputFiles())
				tasksByOutput.getOrCreate(outFile).add(task);
		}

		Gpr.debug("TaskDependecies\n" + this);
	}

	/**
	 * Find all tasks that must be completed before we proceed
	 */
	void findDirectDependencies(Task task) {
		if (!task.isDone()) {
			// Add input dependencies based on input files
			if (task.getInputFiles() != null) {
				for (String inFile : task.getInputFiles()) {
					List<Task> taskDeps = tasksByOutput.get(inFile);
					if (taskDeps != null) {
						for (Task taskDep : taskDeps)
							if (!taskDep.isDone()) task.addDependency(taskDep); // Task not finished? Add it to dependency list
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
		Set<String> leaves = new HashSet<String>();
		for (String n : nodes)
			if (!tasksByOutput.containsKey(n)) leaves.add(n);

		return leaves;
	}

	/**
	 * Find all leaf nodes required for goal 'out'
	 */
	Set<String> findNodes(String out) {
		Set<String> goals = new HashSet<String>();
		goals.add(out);

		// For each goal
		for (boolean changed = true; changed;) {
			changed = false;

			// Initialize
			Set<String> newGoals = new HashSet<String>();
			newGoals.addAll(goals);

			// For each goal
			for (String goal : goals) {
				// Find all tasks required for this goal
				List<Task> tasks = tasksByOutput.get(goal);

				if (tasks != null) // Add all task's input files
					for (Task t : tasks)
						if (t.getInputFiles() != null) changed |= newGoals.addAll(t.getInputFiles());
			}

			goals = newGoals;
		}

		return goals;
	}

	/**
	 * Find tasks required to achieve goal 'out'
	 */
	public List<Task> goal(String out) {
		//---
		// Find all 'leaf nodes' (files) required for this goal
		//---
		Set<String> leaves = findLeafNodes(out);
		if (debug) {
			Gpr.debug("\n\tGoal: " + out + "\n\tLeaf nodes:");
			for (String n : leaves)
				System.err.println("\t\t'" + n + "'");
		}

		//---
		// Check if goal needs to be updated respect to leaf nodes
		//---
		if (!needsUpdate(out, leaves)) {
			if (debug) Gpr.debug("Goal '" + out + "': No update needed.");
			return null; // No update needed
		}

		// Goal needs to be updated: Ad all tasks that need updating
		Set<Task> tasks = new HashSet<Task>();
		for (String n : findNodes(out)) {
			List<Task> ntasks = tasksByOutput.get(n);
			for (Task t : ntasks)
				if (!t.isDone() && needsUpdate(t)) tasks.add(t);
		}

		return sort(tasks);
	}

	/**
	 * Do 'outs' files need to be updated respect to 'ins'?
	 */
	boolean needsUpdate(Collection<String> outs, Collection<String> ins) {
		ExpressionDepOperator dep = new ExpressionDepOperator(null, null);
		return dep.depOperator(outs, ins, debug);
	}

	/**
	 * Does 'out' file need to be updated respect to 'ins'?
	 */
	boolean needsUpdate(String out, Collection<String> ins) {
		// Collection of output files (only one element)
		LinkedList<String> outs = new LinkedList<>();
		outs.add(out);
		return needsUpdate(outs, ins);
	}

	/**
	 * Do output files in this task need to be updated?
	 */
	boolean needsUpdate(Task task) {
		Collection<String> outs = task.getOutputFiles() != null ? task.getOutputFiles() : new LinkedList<String>();
		Collection<String> ins = task.getInputFiles() != null ? task.getInputFiles() : new LinkedList<String>();
		return needsUpdate(outs, ins);
	}

	List<Task> sort(Collection<Task> tasks) {

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		ArrayList<String> outs = new ArrayList<String>();
		outs.addAll(tasksByOutput.keySet());
		Collections.sort(outs);

		for (String out : outs) {
			sb.append(out + ":\n");
			for (Task t : tasksByOutput.get(out))
				sb.append("\t" + t.getId() + "\n");
		}
		return sb.toString();
	}

}
