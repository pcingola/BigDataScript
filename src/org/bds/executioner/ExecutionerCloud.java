package org.bds.executioner;

import org.bds.Config;
import org.bds.cluster.Cluster;
import org.bds.cluster.host.HostInifinte;

/**
 * Execute tasks in a cloud
 *
 * How tasks are run:
 * 	- A checkpointVm is created and saved to a bucket
 * 	- A startup script is created to run bds from the checkpoint
 * 	- An instance is created, the startup script is sent as parameter (terminate on shutdown should be enforced if possible)
 * 	- The instance executes the checkpoint
 * 	- The instance sends STDOUT / STDERR / EXIT messages via a queue (maybe a heartbeat message)
 *
 * Executioner
 * 	- The Executioner reads the queue and shows messages / updates task status
 * 	- Instances are monitored for timeout
 * 	- Instances are monitored for failure
 * 	- Instace are terminated
 *
 *
 * @author pcingola
 */
public abstract class ExecutionerCloud extends Executioner {

	protected ExecutionerCloud(Config config) {
		super(config);

		// Create a cluster having only one host with 'infinite' capacity
		cluster = new Cluster();
		new HostInifinte(cluster);
	}

}
