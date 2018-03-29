package org.bds.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bds.data.Data;
import org.bds.lang.expression.Expression;
import org.bds.lang.value.Value;
import org.bds.lang.value.ValueList;
import org.bds.run.BdsThreads;
import org.bds.util.Timer;

/**
 * Output and Input files (and tasks) that are required for a task to succesfully execute
 *
 * @author pcingola
 */
public class TaskDependency {

	boolean debug;
	protected Expression expresison; // Expression that created this 'TaskDependency' (for logging & debugging purposes)
	protected List<String> inputs; // Input files generated by this task
	protected List<String> outputs; // Output files generated by this task
	protected String checkOutputs; // Errors that pop-up when checking output files
	protected List<Task> tasks; // Task that need to finish before this one is executed

	public TaskDependency() {
		this(null);
	}

	public TaskDependency(Expression expresison) {
		this.expresison = expresison;
		outputs = new ArrayList<>();
		inputs = new ArrayList<>();
		tasks = new ArrayList<>();
	}

	public void add(Task task) {
		tasks.add(task);
	}

	/**
	 * Add all dependencies from 'taskDependency' to this this one
	 */
	public void add(TaskDependency taskDependency) {
		addInput(taskDependency.getInputs());
		addOutput(taskDependency.getOutputs());
		tasks.addAll(taskDependency.getTasks());
	}

	/**
	 * Add a list of inputs
	 */
	public void addInput(Collection<String> inputs) {
		for (String in : inputs)
			addInput(in);
	}

	/**
	 * Add input. It can be either a taskId or a file
	 */
	public void addInput(String input) {
		// Is 'input' a task ID?
		Task task = TaskDependecies.get().getTask(input);

		if (task != null) {
			// It is a taskId, add task as dependency
			tasks.add(task);
		} else {
			// Not a taksID, must be an input 'data' (a file)
			inputs.add(BdsThreads.data(input).getAbsolutePath());
		}
	}

	public void addInput(ValueList inputs) {
		for (Value in : inputs)
			addInput(in.asString());
	}

	/**
	 * Add a list of outputs
	 */
	public void addOutput(Collection<String> outputs) {
		for (String out : outputs)
			addOutput(out);
	}

	/**
	 * Add output
	 */
	public void addOutput(String output) {
		outputs.add(BdsThreads.data(output).getAbsolutePath());
	}

	public void addOutput(ValueList outputs) {
		for (Value out : outputs)
			addOutput(out.asString());
	}

	/**
	 * Check if output files are OK
	 * @return true if OK, false there is an error (output file does not exist or has zero length)
	 */
	public String checkOutputFiles(Task task) {
		if (checkOutputs != null) return checkOutputs;
		if (!task.isStateFinished() || outputs == null) return ""; // Nothing to check

		checkOutputs = "";
		for (String fileName : outputs) {
			Data file = Data.factory(fileName);
			if (!file.exists()) checkOutputs += "Error: Output file '" + fileName + "' does not exist.";
			else if ((!task.isAllowEmpty()) && (file.size() <= 0)) checkOutputs += "Error: Output file '" + fileName + "' has zero length.";
		}

		if (task.verbose && !checkOutputs.isEmpty()) Timer.showStdErr(checkOutputs);
		return checkOutputs;
	}

	/**
	 * Mark output files to be deleted on exit
	 */
	public void deleteOutputFilesOnExit() {
		for (String fileName : outputs) {
			Data file = Data.factory(fileName);
			if (file.exists()) file.deleteOnExit();
		}
	}

	/**
	 * Calculate the result of '<-' operator give two collections files (left hand side and right hand-side)
	 */
	public boolean depOperator() {
		// Empty dependency is always true
		if (outputs.isEmpty() && inputs.isEmpty()) return true;

		//---
		// Left hand side
		// Calculate minimum modification time
		//---

		long minModifiedLeft = Long.MAX_VALUE;
		for (String output : outputs) {
			Data dataOut = Data.factory(output);

			// Any 'left' file does not exists? => We need to build this dependency
			if (!dataOut.exists()) {
				if (debug && (expresison != null)) expresison.log("Left hand side: file '" + output + "' doesn't exist");
				return true;
			}

			if (dataOut.isFile() && dataOut.size() <= 0) {
				if (debug && (expresison != null)) expresison.log("Left hand side: file '" + output + "' is empty");
				return true; // File is empty? => We need to build this dependency.
			} else if (dataOut.isDirectory()) {
				// Notice: If it is a directory, we must rebuild if it is empty
				List<String> dirList = dataOut.list();
				if (dirList.isEmpty()) {
					if (debug && (expresison != null)) expresison.log("Left hand side: file '" + output + "' is an empty dir");
					return true;
				}
			}

			// Analyze modification time
			long modTime = dataOut.getLastModified().getTime();
			minModifiedLeft = Math.min(minModifiedLeft, modTime);
			if (debug) expresison.log("Left hand side: file '" + output + "' modified on " + modTime + ". Min modification time: " + minModifiedLeft);
		}

		//---
		// Right hand side
		// Calculate maximum modification time
		//---

		long maxModifiedRight = Long.MIN_VALUE;
		for (String inout : inputs) {
			Data dataIn = Data.factory(inout);

			// Is this file scheduled to be modified by a pending task? => Time will change => We'll need to update
			List<Task> taskOutList = TaskDependecies.get().getTasksByOutput(inout);
			if (taskOutList != null && !taskOutList.isEmpty()) {
				for (Task t : taskOutList) {
					// If the task modifying 'file' is not finished => We'll need to update
					if (!t.isDone()) {
						if (debug) expresison.log("Right hand side: file '" + inout + "' will be modified by task '" + t.getId() + "' (task state: '" + t.getTaskState() + "')");
						return true;
					}
				}
			}

			if (dataIn.exists()) {
				// Update max time
				long modTime = dataIn.getLastModified().getTime();
				maxModifiedRight = Math.max(maxModifiedRight, modTime);
				if (debug) expresison.log("Right hand side: file '" + inout + "' modified on " + modTime + ". Max modification time: " + maxModifiedRight);
			} else {
				// Make sure that we schedule the task if the input file doesn't exits
				// The reason to do this, is that probably the input file was defined
				// by some other task that is pending execution.
				if (debug && (expresison != null)) expresison.log("Right hand side: file '" + inout + "' doesn't exist");
				return true;
			}
		}

		// Have all 'left' files been modified before 'right' files?
		// I.e. Have all goals been created after the input files?
		boolean ret = (minModifiedLeft < maxModifiedRight);
		if (debug) expresison.log("Modification times, minModifiedLeft (" + minModifiedLeft + ") < maxModifiedRight (" + maxModifiedRight + "): " + ret);
		return ret;
	}

	public List<String> getInputs() {
		return inputs;
	}

	public List<String> getOutputs() {
		return outputs;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public boolean hasTasks() {
		return !tasks.isEmpty();
	}

	boolean isTask(String tid) {
		return TaskDependecies.get().hasTask(tid);
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("( ");

		if ((outputs != null && !outputs.isEmpty()) || (inputs != null && !inputs.isEmpty())) {

			if (outputs != null && !outputs.isEmpty()) {
				boolean comma = false;
				for (String f : outputs) {
					sb.append((comma ? ", " : "") + "'" + f + "'");
					comma = true;
				}
			}

			sb.append(" <- ");

			if (inputs != null && !inputs.isEmpty()) {
				boolean comma = false;
				for (String f : inputs) {
					sb.append((comma ? ", " : "") + "'" + f + "'");
					comma = true;
				}
			}
		}
		sb.append(" )");

		return sb.toString();
	}

}
