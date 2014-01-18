package ca.mcgill.mcb.pcingola.bigDataScript.executioner;

import ca.mcgill.mcb.pcingola.bigDataScript.Config;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.Cluster;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * Execute tasks in a remote computer, using ssh
 * 
 * It is implemented by running a local queue (OsCmdQueue)
 * 
 * @author pcingola
 */
public class SshExecutioner extends LocalQueueExecutioner {

	Cluster cluster;

	public SshExecutioner(Config config) {
		super(config);
		createCluster();
	}

	void createCluster() {
		// Create a cluster 
		cluster = new Cluster();

		// Add nodes from config file
		String nodes = config.getString("ssh.nodes", "");
		Gpr.debug("SSH NODES: " + nodes);
		String sshNodes[] = nodes.split(",");
		for (String sshNode : sshNodes) {
			Gpr.debug("ssh node : " + sshNode);
			cluster.add(new Host(cluster, sshNode.trim()));
		}
	}

	@Override
	public synchronized void kill() {
		cluster.stopHostInfoUpdaters();
		super.kill();
	}

	@Override
	public void run() {
		Gpr.debug("SshExecutioner: Starting.");
		cluster.startHostInfoUpdaters();
		super.run();
		cluster.stopHostInfoUpdaters();
	}

}
