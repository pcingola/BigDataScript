package ca.mcgill.mcb.pcingola.bigDataScript.exec;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.Cluster;

/**
 * Execute tasks in a cluster
 * 
 * It is implemented by running a local queue (OsCmdQueue)
 * Cluster commands are executed using "qsub -I" (interactive mode)
 * 
 * @author pcingola
 */
public class ClusterExecutioner extends LocalQueueExecutioner {

	public ClusterExecutioner(Cluster cluster) {
		super(null);
	}

}
