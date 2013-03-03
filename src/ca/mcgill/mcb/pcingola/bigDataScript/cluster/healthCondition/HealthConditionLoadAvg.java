package ca.mcgill.mcb.pcingola.bigDataScript.cluster.healthCondition;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;

/**
 * Cpu load average condition 
 * @author pcingola
 */
public class HealthConditionLoadAvg extends HealthCondition {

	public HealthConditionLoadAvg(Host host) {
		super(host);
	}

	@Override
	public Light test() {
		if (host.getHealth().getLoadAvg() >= healthRed.getLoadAvg()) return Light.Red;
		if (host.getHealth().getLoadAvg() >= healthYellow.getLoadAvg()) return Light.Yellow;
		return Light.Green;
	}
}
