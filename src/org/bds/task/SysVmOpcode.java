package org.bds.task;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import org.bds.Config;
import org.bds.lang.BdsNode;
import org.bds.lang.expression.ExpressionSys;
import org.bds.lang.expression.ExpressionTask;
import org.bds.osCmd.Exec;
import org.bds.osCmd.ExecResult;
import org.bds.run.BdsThread;
import org.bds.util.Gpr;
import org.bds.util.Timer;

/**
 * Execute a 'sys' VM opcode
 *
 * @author pcingola
 */
public class SysVmOpcode {

	private static int sysId = 1;

	protected BdsThread bdsThread;
	protected BdsNode bdsNode;
	protected String commands;

	/**
	 * Get a sys ID
	 */
	protected static synchronized int nextId() {
		return sysId++;
	}

	public SysVmOpcode(BdsThread bdsThread) {
		this.bdsThread = bdsThread;
	}

	/**
	 * Try to find the current bdsNode
	 */
	protected BdsNode bdsNode() {
		BdsNode n = bdsThread.getBdsNodeCurrent();

		// Try to find a 'sys' node
		for (BdsNode bn = n; bn != null; bn = bn.getParent()) {
			if (isNode(bn)) return bn;
		}

		// Not found? Use this as default
		return n;
	}

	/**
	 * Try to find the current bdsNode
	 */
	protected BdsNode getBdsNode() {
		if (bdsNode != null) return bdsNode;
		bdsNode = bdsNode();
		return bdsNode;
	}

	protected String getFileName() {
		return (getBdsNode() != null) ? getBdsNode().getFileName() : null;
	}

	protected int getLineNum() {
		return (getBdsNode() != null) ? getBdsNode().getLineNum() : -1;
	}

	protected String getSysFileName(String execId) {
		if (execId == null) throw new RuntimeException("Exec ID is null. This should never happen!");

		String sysFileName = execId + ".sh";
		File f = new File(sysFileName);
		try {
			return f.getCanonicalPath();
		} catch (IOException e) {
			throw new RuntimeException("Cannot get cannonical path for file '" + sysFileName + "'");
		}
	}

	/**
	 * This is only used in tasks
	 */
	protected String getTaskName() {
		return null;
	}

	protected boolean isNode(BdsNode n) {
		return n instanceof ExpressionSys;
	}

	/**
	 * Create and run sys
	 */
	public String run() {
		// 'sys' expressions are always executed locally and immediately
		LinkedList<String> args = new LinkedList<>();
		String shell = Config.get().getSysShell();
		for (String arg : shell.split("\\s+"))
			args.add(arg);

		// Command from string or interpolated vars
		String cmds = bdsThread.pop().asString();
		args.add(cmds);

		// Save commands to file for debugging and traceability
		if (Config.get().isLog()) saveProgramFile(cmds);

		// Run command line
		ExecResult execResult = Exec.exec(args, bdsThread.getConfig().isQuiet());

		// Error running process?
		int exitValue = execResult.exitValue;
		if (exitValue != 0) {
			// Can this execution fail?
			boolean canFail = bdsThread.getBool(ExpressionTask.TASK_OPTION_CAN_FAIL);

			// Execution failed on a 'sys' command that cannot fail. Save checkpoint and exit
			if (!canFail) {
				bdsThread.fatalError("Exec failed." //
						+ "\n\tExit value : " + exitValue //
						+ "\n\tCommand    : " + cmds //
				);
				return "";
			}
		}

		// Collect output
		String output = "";
		if (execResult.stdOut != null) output = execResult.stdOut;
		return output;
	}

	/**
	 * Create a program file
	 */
	protected String saveProgramFile(String programTxt) {
		// Select file name
		String sysId = sysId();
		String programFileName = getSysFileName(sysId);
		if (Config.get().isDebug()) Timer.showStdErr("Task: Saving file '" + programFileName + "'");

		// Create dir
		try {
			File dir = new File(programFileName);
			dir = dir.getCanonicalFile().getParentFile();
			if (dir != null) dir.mkdirs();
		} catch (IOException e) {
			// Nothing to do
		}

		// Show 'sys' shell execution
		programTxt = "# Execution shell: " + Config.get().getSysShell() + "\n\n" + programTxt;

		// Save file and make it executable
		Gpr.toFile(programFileName, programTxt);

		return programFileName;
	}

	protected String sysId() {
		return sysId("sys");
	}

	/**
	 * Create a task ID
	 */
	protected String sysId(String tag) {
		int nextId = nextId();

		// Use module name
		String module = getFileName();
		if (module != null) module = Gpr.removeExt(Gpr.baseName(module));

		String taskName = getTaskName();
		if (taskName != null) {
			if (taskName.isEmpty()) taskName = null;
			else taskName = Gpr.sanityzeName(taskName); // Make sure that 'taskName' can be used in a filename
		}

		int ln = getLineNum();
		String execId = bdsThread.getBdsThreadId() //
				+ "/" + tag //
				+ (module == null ? "" : "." + module) //
				+ (taskName == null ? "" : "." + taskName) //
				+ (ln > 0 ? ".line_" + ln : "") //
				+ ".id_" + nextId //
		;

		return execId;
	}

}
