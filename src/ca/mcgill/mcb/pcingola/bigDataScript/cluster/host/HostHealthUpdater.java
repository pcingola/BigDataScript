package ca.mcgill.mcb.pcingola.bigDataScript.cluster.host;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.commandParser.CommandParserCpuInfo;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.commandParser.CommandParserDf;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.commandParser.CommandParserMemInfo;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.commandParser.CommandParserSystemProfiler;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.commandParser.CommandParserTop;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.commandParser.CommandParserUname;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.commandParser.CommandParserUptime;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.commandParser.CommandParserWho;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.commandParser.Ssh;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * Update host's info every now and then (in a separate thread)
 * 
 * @author pcingola
 */
public class HostHealthUpdater extends Thread {

	public static boolean debug = false;

	Host host;
	boolean run = true;
	Ssh ssh;

	public HostHealthUpdater(Host host) {
		super();
		this.host = host;
		ssh = new Ssh(host);
	}

	/**
	 * Connect to host (via ssh) and execute several commands in order to obtain host's information
	 */
	void info() {
		// Information that is obtained only once
		new CommandParserUname(host, ssh).parse(); // System type
		String systemType = host.getHealth().getSystemType();

		if (systemType.equalsIgnoreCase("Linux")) new CommandParserCpuInfo(host, ssh).parse();
		else if (systemType.equalsIgnoreCase("Darwin")) new CommandParserSystemProfiler(host, ssh).parse();

		update(); // This information is refreshed every now and then
	}

	public boolean isRun() {
		return run;
	}

	/**
	 * Stop execution of this thread
	 */
	public void kill() {
		synchronized (this) {
			setRun(false); // Set run to false and wake up from 'wait'. See run() method
			notify();
		}
	}

	@Override
	public void run() {
		Gpr.debug("Info updater " + this + ": Run");
		try {
			ssh.open();
			info();

			while (run) {
				try {
					// I'd rather sleep this way in order to allow for notifications (i.e. 'wake up call')
					synchronized (this) {
						wait(host.getCluster().getRefreshTime() * 1000);
					}
					update();
				} catch (Exception e) {
					Gpr.debug(e);
					run = false;
				}
			}
		} catch (Throwable t) {
			Gpr.debug(t);
			t.printStackTrace(); // Something happened? => Stop this thread
		} finally {
			ssh.close();
			run = false;
		}
		Gpr.debug("Info updater " + this + ": End");
	}

	public void setRun(boolean run) {
		this.run = run;
	}

	/**
	 * Connect to host (via ssh) and execute several commands in order to update host's information 
	 * (e.g. cpu information does not usually change, so it's not obtained)
	 */
	void update() {
		String systemType = host.getHealth().getSystemType();

		host.getHealth().setAlive(ssh.isRunning());
		if (debug) Gpr.debug("Host: " + host + "\talive: " + host.getHealth().isAlive());

		if (systemType.equalsIgnoreCase("Linux")) {
			new CommandParserUptime(host, ssh).parse();
			new CommandParserMemInfo(host, ssh).parse();
			new CommandParserWho(host, ssh).parse();
			new CommandParserDf(host, ssh).parse();
		} else if (systemType.equalsIgnoreCase("Darwin")) {
			new CommandParserUptime(host, ssh).parse();
			new CommandParserTop(host, ssh).parse();
			new CommandParserWho(host, ssh).parse();
			new CommandParserDf(host, ssh).parse();
		}

		if (debug) Gpr.debug("Host info updated: " + host + "\n\t" + host.getResources() + "\n" + host.getHealth());
	}

}
