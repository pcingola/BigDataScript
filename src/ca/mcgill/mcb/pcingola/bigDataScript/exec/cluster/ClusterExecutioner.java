package ca.mcgill.mcb.pcingola.bigDataScript.exec.cluster;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.Cluster;
import ca.mcgill.mcb.pcingola.bigDataScript.exec.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.exec.local.ShellExecutioner;

/**
 * Execute tasks in a cluster
 * 
 * It is implemented by running a local queue (OsCmdQueue)
 * Cluster commands are executed using "qsub -I" (interactive mode)
 * 
 * @author pcingola
 */
public class ClusterExecutioner extends ShellExecutioner {

	public ClusterExecutioner(Cluster cluster) {
		super(cluster);
		taskQueue.setCheckResources(false); // Cluster infrastructure takes care of resource adminitration
	}

	@Override
	public Task task(String execId, String sysFileName, String commands) {
		return new ClusterTask(execId, sysFileName, commands);
	}

}
