package ca.mcgill.mcb.pcingola.bigDataScript.executioner;

import ca.mcgill.mcb.pcingola.bigDataScript.Config;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.Cmd;
import ca.mcgill.mcb.pcingola.bigDataScript.task.Task;

/**
 * Execute tasks in local computer.
 *  
 * Uses a queue to avoid saturating resources (e.g. number of running 
 * processes in the queue should not exceed number of CPUs)
 * 
 * @author pcingola
 */
public class ExecutionerLocal extends Executioner {

	public ExecutionerLocal(Config config) {
		super(config);
	}

	@Override
	protected Cmd createCmd(Task task) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String osKillCommand(Task task) {
		// TODO Auto-generated method stub
		return null;
	}

	//	/**
	//	 * Can we run the next task?
	//	 * @return true is there at least one host with enough resources to run the next task
	//	 */
	//	protected synchronized boolean canRunNextTask() {
	//		if (tasksToRun.isEmpty()) return true;
	//
	//		// Are running tasks are consuming all resources?
	//		HostResources res = host.getResources().clone();
	//		for (Task t : tasksRunning.values())
	//			res.consume(t.getResources());
	//
	//		return res.isValid();
	//	}
	//
	//	/**
	//	 * Run task
	//	 */
	//	@Override
	//	protected boolean runExecutionerLoop() {
	//		// Nothing to run?
	//		if (!hasTaskToRun()) return false;
	//
	//		// Are there any more task to run?
	//		while (running && hasTaskToRun()) {
	//			// Can we run the next task?
	//			if (canRunNextTask()) {
	//				// Get next task and run it
	//				runTaskNext(host);
	//			} else {
	//				sleepShort();
	//				if (verbose) Timer.showStdErr("Queue tasks:\tPending : " + tasksToRun.size() + "\tRunning: " + tasksRunning.size() + "\tDone: " + tasksDone.size());
	//			}
	//		}
	//
	//		return true;
	//	}
}
