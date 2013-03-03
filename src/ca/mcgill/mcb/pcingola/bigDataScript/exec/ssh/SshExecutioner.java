package ca.mcgill.mcb.pcingola.bigDataScript.exec.ssh;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.Cluster;
import ca.mcgill.mcb.pcingola.bigDataScript.exec.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.exec.local.ShellExecutioner;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.CmdRunner;

/**
 * Execute tasks in a remote computer, using ssh
 * 
 * It is implemented by running a local queue (OsCmdQueue)
 * 
 * @author pcingola
 */
public class SshExecutioner extends ShellExecutioner {

	public SshExecutioner(Cluster cluster) {
		super(cluster);
	}

	@Override
	public synchronized void kill() {
		cluster.stopHostInfoUpdaters();
		super.kill();
	}

	@Override
	public synchronized String queue(Task task) {
		CmdRunner oscmd = task.cmdRunner();
		taskQueue.add(oscmd);
		return oscmd.getCmdId();
	}

	@Override
	public void run() {
		cluster.startHostInfoUpdaters();
		super.run();
		cluster.stopHostInfoUpdaters();
	}

	@Override
	public Task task(String execId, String sysFileName, String commands) {
		return new SshTask(execId, sysFileName, commands);
	}

}
