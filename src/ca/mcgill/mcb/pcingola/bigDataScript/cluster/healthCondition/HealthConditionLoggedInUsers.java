package ca.mcgill.mcb.pcingola.bigDataScript.cluster.healthCondition;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;

/**
 * Number of users logged in
 * @author pcingola
 */
public class HealthConditionLoggedInUsers extends HealthCondition {

	public HealthConditionLoggedInUsers(Host host) {
		super(host);
	}

	@Override
	public Light test() {
		if (host.getHealth().getLoggedInUsersCount() >= healthRed.getLoggedInUsersCount()) return Light.Red;
		if (host.getHealth().getLoggedInUsersCount() >= healthYellow.getLoggedInUsersCount()) return Light.Yellow;
		return Light.Green;
	}
}
