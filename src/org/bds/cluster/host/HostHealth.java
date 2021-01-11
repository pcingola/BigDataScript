package org.bds.cluster.host;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.bds.cluster.ClusterSsh;
import org.bds.cluster.healthCondition.HealthCondition;
import org.bds.cluster.healthCondition.HealthConditionLoadAvg;
import org.bds.cluster.healthCondition.HealthConditionLoggedInUsers;
import org.bds.cluster.healthCondition.HealthConditionMem;
import org.bds.cluster.healthCondition.HealthCondition.Light;

/**
 * Host 'health' parameters (e.g. load average, available disk space, etc.)
 *
 * @author pcingola
 */
public class HostHealth {

	public static final int REFRESH_MULTIPLIER = 5;

	HostSsh host;
	boolean alive; // Is the host alive?
	String systemType = ""; // System type (e.g. "Linux", "Darwin")
	String error; // Latest error message
	int maxNumberOfProcs;
	int loggedInUsersCount = 0; // Number of logged users
	long latestUpdate = 0;
	double memUsage = 0; // Memory usage (as a percentage)
	double swapUsage = 0; // Percentage of used swap
	double loadAvg = 0; // load average (from 'uptime')
	long fsTotal = 0, fsAvail = 0; // File system information (in KBytes)
	String cpuInfo = ""; // CPU detail info
	String loggedInUsers = ""; // Logged in users (space separated list)
	HashMap<String, String> notes;
	ArrayList<HealthCondition> conditions; // Conditions to test

	public HostHealth(HostSsh host) {
		this.host = host;
		notes = new HashMap<String, String>();

		// Create and add conditions
		conditions = new ArrayList<HealthCondition>();
		conditions.add(new HealthConditionMem(host));
		conditions.add(new HealthConditionLoggedInUsers(host));
		conditions.add(new HealthConditionLoadAvg(host));
		//		conditions.add(new ConditionLoadAvgByCpu(host));
	}

	/**
	 * Get current host condition
	 * @return
	 */
	public Light condition() {
		Light light;
		int max = Light.Green.ordinal();

		for (HealthCondition cond : conditions) {
			light = cond.test();
			max = Math.max(max, light.ordinal());
		}

		return Light.values()[max];
	}

	public String getCpuInfo() {
		return cpuInfo;
	}

	public long getFsAvail() {
		return fsAvail;
	}

	public long getFsTotal() {
		return fsTotal;
	}

	public long getLatestUpdate() {
		return latestUpdate;
	}

	public double getLoadAvg() {
		return loadAvg;
	}

	public String getLoggedInUsers() {
		return loggedInUsers;
	}

	public int getLoggedInUsersCount() {
		return loggedInUsersCount;
	}

	public int getMaxNumberOfProcs() {
		return maxNumberOfProcs;
	}

	public double getMemUsage() {
		return memUsage;
	}

	public String getNote(String key) {
		String note = notes.get(key);
		if (notes != null) return note;
		return "";
	}

	public double getSwapUsage() {
		return swapUsage;
	}

	public String getSystemType() {
		return systemType;
	}

	public boolean isAlive() {
		return alive;
	}

	public boolean isAvailable() {
		return condition() != Light.Red;
	}

	public boolean isUpdated() {
		if (latestUpdate <= 0) return false; // Never updated
		long elapsed = ((new Date()).getTime() - latestUpdate) / 1000; // Elapsed time in seconds
		ClusterSsh cluster = (ClusterSsh) host.getSystem();
		return elapsed > (REFRESH_MULTIPLIER * cluster.getRefreshTime()); // Too much time since latest update? => not updated
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public void setCpuInfo(String cpuInfo) {
		this.cpuInfo = cpuInfo;
	}

	public void setFsAvail(long fsAvail) {
		this.fsAvail = fsAvail;
	}

	public void setFsTotal(long fsTotal) {
		this.fsTotal = fsTotal;
	}

	public void setLatestUpdate(long latestUpdate) {
		this.latestUpdate = latestUpdate;
	}

	public void setLoadAvg(double loadAvg) {
		this.loadAvg = loadAvg;
	}

	public void setLoggedInUsers(String loggedInUsers) {
		this.loggedInUsers = loggedInUsers;
	}

	public void setLoggedInUsersCount(int loggedInUsersCount) {
		this.loggedInUsersCount = loggedInUsersCount;
	}

	public void setMaxNumberOfProcs(int maxNumberOfProcs) {
		this.maxNumberOfProcs = maxNumberOfProcs;
	}

	public void setMemUsage(double memUsage) {
		this.memUsage = memUsage;
	}

	public void setNote(String key, String value) {
		notes.put(key, value);
	}

	public void setSwapUsage(float swapUsage) {
		this.swapUsage = swapUsage;
	}

	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\tAlive                   : " + alive + "\n");
		sb.append("\tMax number of processes : " + maxNumberOfProcs + "\n");
		if (systemType.length() > 0) {
			double fsusage = 0;
			if (fsTotal > 0) fsusage = 100 * fsAvail / fsTotal;
			sb.append("\tSystem type             : " + systemType + "\n");
			sb.append("\tCpus type               : " + cpuInfo + "\n");
			sb.append("\tLoad average            : " + loadAvg + "\n");
			sb.append(String.format("\tMemory usage            : %.1f%%\n", (100 * memUsage)));
			sb.append("\tLogged users            : " + loggedInUsersCount + " " + loggedInUsers + "\n");
			sb.append(String.format("\tSwap usage              : %.1f%%\n", (100 * swapUsage)));
			sb.append(String.format("\tFile system             : %.1f%%\n", fsusage));
		}

		sb.deleteCharAt(sb.length() - 1); // Remove last '\n'
		return sb.toString();
	}

}
