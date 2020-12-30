package org.bds.osCmd;

/**
 * A command that runs a script in a cluster
 *
 * This command only submits for execution (e.g. via 'qsub')
 * So, in this case, when the command finishes execution, it
 * only means that the task is queued.
 *
 * @author pcingola
 */
public class CmdCluster extends CmdLocal {

	public CmdCluster(String id, String[] args) {
		super(id, args);
	}

	@Override
	protected void execCmd() throws Exception {
		// Wait for the process to finish and store exit value
		exitValue = process.waitFor();

		// Error sending task to cluster?
		if (exitValue < 0) throw new RuntimeException("Error queuing task in cluster.\n\tCommand: " + this);
	}

	/**
	 * This command only submits for execution (e.g. via 'qsub')
	 * So, in this case, when the command finishes execution, it
	 * only means that the task is queued.
	 */
	@Override
	protected void stateDone() {
		notifyRunning();
	}

	/**
	 * Change state after executing command
	 */
	@Override
	protected void stateRunningAfter() {
		notifyStarted();
	}

	/**
	 * Change state before executing command
	 */
	@Override
	protected void stateRunningBefore() {
		// Nothing to do
	}

	@Override
	protected void stateStarted() {
		// Nothing to do
	}

}
