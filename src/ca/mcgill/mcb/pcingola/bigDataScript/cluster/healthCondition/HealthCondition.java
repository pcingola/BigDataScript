package ca.mcgill.mcb.pcingola.bigDataScript.cluster.healthCondition;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostHealth;

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

	Host host;
	HostHealth healthRed, healthYellow;

	public HealthCondition(Host host) {
		this.host = host;
		if (host != null) {
			healthYellow = host.getCluster().getHealthYellow();
			healthRed = host.getCluster().getHealthRed();
		}
	}

	public abstract Light test();
}
