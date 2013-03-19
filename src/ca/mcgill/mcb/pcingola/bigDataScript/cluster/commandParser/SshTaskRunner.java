package ca.mcgill.mcb.pcingola.bigDataScript.cluster.commandParser;

/**
 * Run a task in a host by sending it to an ssh connection
 * 
 * @author pcingola
 */
public class SshTaskRunner extends Thread {

	Ssh ssh;
	boolean run = false, started = false;
	String task;
	int taskNum;

	public SshTaskRunner(Ssh ssh, String task, int taskNum) {
		super();
		this.ssh = ssh;
		this.task = task;
		this.taskNum = taskNum;
	}

	/**
	 * Stop execution of this thread
	 */
	public synchronized void finish() {
		run = false; // Set run to false and wake up from 'wait'. See run() method
		notify();
	}

	public int getTaskNum() {
		return taskNum;
	}

	/**
	 * Has this runner finished?
	 * @return
	 */
	public boolean isDone() {
		return started && !run; // In order to finish, it has to be started and not running any more
	}

	@Override
	public void run() {
		//		try {
		//			run = true;
		//			ssh.open();
		//			started = true;
		//			ssh.resetBuffers(); // Clear all buffers before proceeding
		//			ssh.send(task, 0); // Send command and collect results
		//		} catch(Throwable t) {
		//			t.printStackTrace(); // Something happened? => Stop this thread
		//		} finally {
		//			ssh.close();
		//			run = false;
		//		}
	}

	public void setRun(boolean run) {
		this.run = run;
	}
}
