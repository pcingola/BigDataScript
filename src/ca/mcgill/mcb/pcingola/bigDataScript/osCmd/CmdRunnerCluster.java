package ca.mcgill.mcb.pcingola.bigDataScript.osCmd;

/**
 * A command that runs a script in a cluster
 * 
 * @author pcingola
 */
public class CmdRunnerCluster extends CmdRunner {

	public CmdRunnerCluster(String id, String[] args) {
		super(id, args);
	}

	/**
	 * Finished 'exec' of a command, update states
	 */
	@Override
	protected void execDone() {
		started = true; // We started.
		if (task != null) task.setExitValue(exitValue); // Update task
	}

	@Override
	protected void execError(Exception e) {
		super.execError(e);
		super.execDone(); // In this case, we are done, task failed
	}

}
