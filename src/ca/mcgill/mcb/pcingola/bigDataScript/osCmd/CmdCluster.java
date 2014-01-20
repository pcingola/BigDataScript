package ca.mcgill.mcb.pcingola.bigDataScript.osCmd;

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

	/** 
	 * This command only submits for execution (e.g. via 'qsub')
	 * So, in this case, when the command finishes execution, it 
	 * only means that the task is queued.
	 */
	@Override
	protected void execDone() {
		stateDone();
		if (executioner != null) executioner.taskRunning(task);
	}

	@Override
	protected void stateDone() {
		started = true;
		executing = false;
	}

	@Override
	protected void stateRunning() {
		started = true;
		if (executioner != null) executioner.taskStarted(task);
	}

	@Override
	protected void stateStarted() {
		started = true;
	}

}
