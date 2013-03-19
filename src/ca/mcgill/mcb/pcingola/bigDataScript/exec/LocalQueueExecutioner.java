package ca.mcgill.mcb.pcingola.bigDataScript.exec;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.Cluster;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostLocal;

/**
 * Execute tasks in local computer.
 *  
 * Uses a queue to avoid saturating resources (e.g. number of running 
 * processes in the queue should not exceed number of CPUs)
 * 
 * It is implemented by running a local queue (OsCmdQueue)
 * 
 * @author pcingola
 */
public class LocalQueueExecutioner extends Executioner {

	protected int sleepTime = 250; // Default sleep time
	Host host; // This computer

	public LocalQueueExecutioner() {
		super();

		// Create localHost
		Cluster cluster = new Cluster();
		host = new HostLocal(cluster);
	}

	@Override
	protected boolean killTask(Task task) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean runTask(Task task) {
		// TODO Auto-generated method stub
		return false;
	}

}
