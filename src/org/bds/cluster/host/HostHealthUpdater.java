package org.bds.cluster.host;

import org.bds.BdsLog;
import org.bds.cluster.ClusterSsh;
import org.bds.cluster.commandParser.CommandParser;
import org.bds.cluster.commandParser.CommandParserCpuInfo;
import org.bds.cluster.commandParser.CommandParserSystemProfiler;
import org.bds.cluster.commandParser.CommandParserUname;

/**
 * Update host's info every now and then (in a separate thread)
 *
 * @author pcingola
 */
public class HostHealthUpdater extends Thread implements BdsLog {

	HostSsh host;
	boolean run = true;
	String systemType;

	public HostHealthUpdater(HostSsh host) {
		super();
		this.host = host;
	}

	/**
	 * Connect to host (via ssh) and execute several commands in order to obtain host's information
	 */
	void info() {
		debug("Info\tHost: " + host);

		// Information that is obtained only once
		new CommandParserUname(host).parse(false); // System type
		systemType = host.getHealth().getSystemType();

		// Run next command
		if (systemType.equalsIgnoreCase("Linux")) new CommandParserCpuInfo(host).parse(false);
		else if (systemType.equalsIgnoreCase("Darwin")) new CommandParserSystemProfiler(host).parse(false);
	}

	/**
	 * Stop execution of this thread
	 */
	public void kill() {
		setRun(false); // Set run to false and wake up from 'wait'. See run() method
		synchronized (this) {
			notifyAll();
		}
	}

	@Override
	public void run() {
		try {
			while (run) {
				// This should be run only once
				if (systemType == null) info();
				if (run) update();

				// I'd rather sleep this way in order to allow for notifications (i.e. 'wake up call')
				synchronized (this) {
					ClusterSsh cluster = (ClusterSsh) host.getSystem();
					wait(cluster.getRefreshTime() * 1000);
				}
			}
		} catch (Exception t) {
			t.printStackTrace(); // Something happened? => Stop this thread
		} finally {
			run = false;
		}
	}

	public void setRun(boolean run) {
		this.run = run;
	}

	/**
	 * Connect to host (via ssh) and execute several commands in order to update host's information
	 * (e.g. cpu information does not usually change, so it's not obtained)
	 */
	void update() {
		// We don't have system info yet?
		if (systemType == null) return;

		debug("Update Start: host: " + host + ", alive: " + host.getHealth().isAlive());

		// Select which command parsers to run depending on system type
		CommandParser commandParser = null;
		if (systemType.equalsIgnoreCase("Linux")) commandParser = new CommandParser(host, "uptime;df;who;uname -a;cat /proc/meminfo");
		else if (systemType.equalsIgnoreCase("Darwin")) commandParser = new CommandParser(host, "uptime;df;who;uname -a;top -l 1");
		else return;

		// Run command parser (updates host health)
		commandParser.parse();

		debug("Host info updated: " + host //
				+ "\nResources: " + host.getResources() //
				+ "\nHeath:\n" + host.getHealth() //
				+ "\nCondition: " + host.getHealth().condition() //
		);

		debug("Update End: host: " + host + ", alive: " + host.getHealth().isAlive());
	}
}
