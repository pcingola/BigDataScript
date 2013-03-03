package ca.mcgill.mcb.pcingola.bigDataScript.cluster.healthCondition;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;

/**
 * Cpu load average condition 
 * @author pcingola
 */
public class HealthConditionLoadAvgByCpu extends HealthCondition {

	public HealthConditionLoadAvgByCpu(Host host) {
		super(host);
	}

	@Override
	public Light test() {
		int cpus = host.getResources().getCpus();
		if (cpus <= 0) return Light.Green;

		double loadAvgByCpu = host.getHealth().getLoadAvg() / cpus;
		if (loadAvgByCpu >= healthRed.getLoadAvg()) return Light.Red;
		if (loadAvgByCpu >= healthRed.getLoadAvg()) return Light.Yellow;
		return Light.Green;
	}
}
