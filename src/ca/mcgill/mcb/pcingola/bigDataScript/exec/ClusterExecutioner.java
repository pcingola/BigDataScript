package ca.mcgill.mcb.pcingola.bigDataScript.exec;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.Cluster;

/**
 * Execute tasks in a cluster.
 * 
 * All commands are run using 'qsub' (or equivalent) commands
 * 
 * @author pcingola
 */
public class ClusterExecutioner extends LocalExecutioner {

	public ClusterExecutioner(Cluster cluster) {
		super(null);
	}

}
