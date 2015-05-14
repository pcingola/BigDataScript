package ca.mcgill.mcb.pcingola.bigDataScript.cluster.healthCondition;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostSsh;

/**
 * Cpu load average condition
 * @author pcingola
 */
public class HealthConditionLoadAvg extends HealthCondition {

	public HealthConditionLoadAvg(HostSsh host) {
		super(host);
	}

	@Override
	public Light test() {
		if (host.getHealth().getLoadAvg() >= healthRed.getLoadAvg()) return Light.Red;
		if (host.getHealth().getLoadAvg() >= healthYellow.getLoadAvg()) return Light.Yellow;
		return Light.Green;
	}
}
