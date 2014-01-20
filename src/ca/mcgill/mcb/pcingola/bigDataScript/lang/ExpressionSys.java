package ca.mcgill.mcb.pcingola.bigDataScript.lang;

import java.util.LinkedList;

import org.antlr.v4.runtime.tree.ParseTree;

import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.StreamGobbler;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.run.RunState;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * An 'exec' expression (to execute a command line in a local computer, return STDOUT)
 * 
 * @author pcingola
 */
public class ExpressionSys extends ExpressionSysOld {

	public static String SHELL_COMMAND[] = { "/bin/bash", "-e", "-c" };
	String output;

	public ExpressionSys(BigDataScriptNode parent, ParseTree tree) {
		super(parent, tree);
	}

	/**
	 * Evaluate an expression
	 */
	@Override
	public Object eval(BigDataScriptThread csThread) {
		// Run like a statement and return task ID
		run(csThread);
		return (output != null ? output : "");
	}

	@Override
	protected void parse(ParseTree tree) {
		commands = tree.getChild(0).getText();
		commands = commands.substring("exec".length()).trim(); // Remove leading 'exec' part and trim spaces
		interpolateVars(commands); // Find interpolated variables
	}

	@Override
	protected RunState runStep(BigDataScriptThread csThread) {
		if (csThread.isCheckpointRecover()) return RunState.CHECKPOINT_RECOVER;

		// Get an ID
		execId = execId("exec", csThread);

		// EXEC expressions are always executed locally AND immediately
		LinkedList<String> args = new LinkedList<String>();
		for (String arg : SHELL_COMMAND)
			args.add(arg);

		// Interpolated variables
		String cmds = getCommands(csThread);
		args.add(cmds);

		// Run commands line
		int exitValue = -1;
		StreamGobbler stdout = null, stderr = null;
		try {
			ProcessBuilder pb = new ProcessBuilder(args);
			Process process = pb.start();

			// Make sure we read STDOUT and STDERR, so that process does not block
			stdout = new StreamGobbler(process.getInputStream(), false);
			stderr = new StreamGobbler(process.getErrorStream(), true);
			stdout.setSaveLinesInMemory(true);
			stdout.start();
			stderr.start();

			// Wait for process to finish
			exitValue = process.waitFor();

			// Wait for Gobblers to finish (otherwise we may have an incomplete stdout/stderr)
			stdout.join();
			stderr.join();

			if (debug) Gpr.debug("Exit value: " + exitValue);
		} catch (Exception e) {
			throw new RuntimeException("Cannot execute commnads: '" + commands + "'");
		}

		// Error running process? 
		if (exitValue != 0) {
			// Execution failed! Save checkpoint and exit
			csThread.fatalError(this, "Exec failed." //
					+ "\n\tExit value : " + exitValue //
					+ "\n\tCommand    : " + cmds //
			);
			return RunState.FATAL_ERROR;
		}

		// Collect output
		if (stdout != null) output = stdout.getAllLines();

		return RunState.OK;
	}
}
