package org.bds.cluster.healthCondition;

import org.bds.cluster.host.HostSsh;

/**
 * Cpu load average condition
 * @author pcingola
 */
public class HealthConditionLoadAvgByCpu extends HealthCondition {

	public HealthConditionLoadAvgByCpu(HostSsh host) {
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
