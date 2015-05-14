package ca.mcgill.mcb.pcingola.bigDataScript.cluster.healthCondition;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.ClusterSsh;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostHealth;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostSsh;

/**
 * A 'condition' that is tested in a host
 * E.g.: Memory, CPU usage, etc
 *
 * @author pcingola
 */
public abstract class HealthCondition {

	public enum Light {
		Green, Yellow, Red
	};

	HostSsh host;
	HostHealth healthRed, healthYellow;

	public HealthCondition(HostSsh host) {
		this.host = host;
		if (host != null) {
			ClusterSsh cluster = (ClusterSsh) host.getCluster();
			healthYellow = cluster.getHealthYellow();
			healthRed = cluster.getHealthRed();
		}
	}

	public abstract Light test();
}
