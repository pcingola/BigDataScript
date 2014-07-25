package ca.mcgill.mcb.pcingola.bigDataScript.run;

import java.util.LinkedList;
import java.util.List;

import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.util.AutoHashMap;

/**
 * Store task and dependency graph
 *
 * @author pcingola
 */
public class TaskDependecies {

	AutoHashMap<String, List<Task>> tasksByOutput;

	public TaskDependecies() {
		tasksByOutput = new AutoHashMap<String, List<Task>>(new LinkedList<Task>());
	}

	/**
	 * Add a task
	 */
	public void add(Task task) {
		// Add output files
		if (task.getOutputFiles() != null) {
			for (String outFile : task.getOutputFiles())
				tasksByOutput.getOrCreate(outFile).add(task);
		}

		findDirectDependencies(task);
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		return sb.toString();
	}

}
