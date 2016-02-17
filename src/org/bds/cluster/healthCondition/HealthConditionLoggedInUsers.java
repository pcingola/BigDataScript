package org.bds.cluster.healthCondition;

import org.bds.cluster.host.HostSsh;

/**
 * Number of users logged in
 * @author pcingola
 */
public class HealthConditionLoggedInUsers extends HealthCondition {

	public HealthConditionLoggedInUsers(HostSsh host) {
		super(host);
	}

	@Override
	public Light test() {
		if (host.getHealth().getLoggedInUsersCount() >= healthRed.getLoggedInUsersCount()) return Light.Red;
		if (host.getHealth().getLoggedInUsersCount() >= healthYellow.getLoggedInUsersCount()) return Light.Yellow;
		return Light.Green;
	}
}
