package ca.mcgill.mcb.pcingola.bigDataScript.cluster.healthCondition;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.HostSsh;

/**
 * Memory usage condition
 * @author pcingola
 */
public class HealthConditionMem extends HealthCondition {

	public HealthConditionMem(HostSsh host) {
		super(host);
	}

	@Override
	public Light test() {
		if (host.getHealth().getMemUsage() >= healthRed.getMemUsage()) return Light.Red;
		if (host.getHealth().getMemUsage() >= healthYellow.getMemUsage()) return Light.Yellow;
		return Light.Green;
	}
}
