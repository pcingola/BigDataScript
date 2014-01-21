package ca.mcgill.mcb.pcingola.bigDataScript.cluster.host;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.commandParser.CommandParserCpuInfo;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.commandParser.CommandParserSystemProfiler;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.commandParser.CommandParserUname;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.commandParser.CommandParserUptime;
import ca.mcgill.mcb.pcingola.bigDataScript.cluster.commandParser.CommandParserWho;
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

	public HostHealthUpdater(Host host) {
		super();
		this.host = host;
	}

	/**
	 * Connect to host (via ssh) and execute several commands in order to obtain host's information
	 */
	void info() {
		// Information that is obtained only once
		new CommandParserUname(host).parse(); // System type
		String systemType = host.getHealth().getSystemType();

		if (systemType.equalsIgnoreCase("Linux")) new CommandParserCpuInfo(host).parse();
		else if (systemType.equalsIgnoreCase("Darwin")) new CommandParserSystemProfiler(host).parse();

		update(); // This information is refreshed every now and then
	}

	/**
	 * Stop execution of this thread
	 */
	public synchronized void kill() {
		setRun(false); // Set run to false and wake up from 'wait'. See run() method
		notify();
	}

	@Override
	public void run() {
		try {
			info();

			while (run) {
				try {
					// I'd rather sleep this way in order to allow for notifications (i.e. 'wake up call')
					synchronized (this) {
						wait(host.getCluster().getRefreshTime() * 1000);
					}

					if (run) update();

				} catch (Exception e) {
					Gpr.debug(e);
					run = false;
				}
			}
		} catch (Throwable t) {
			Gpr.debug(t);
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
		// host.getHealth().setAlive(ssh.isRunning());

		if (debug) Gpr.debug("Host: " + host + "\talive: " + host.getHealth().isAlive());

		new CommandParserUptime(host).parse();
		new CommandParserWho(host).parse();

		//		new CommandParserDf(host).parse();
		//		String systemType = host.getHealth().getSystemType();
		//		if (systemType.equalsIgnoreCase("Linux")) {
		//			new CommandParserMemInfo(host).parse();
		//		} else if (systemType.equalsIgnoreCase("Darwin")) {
		//			new CommandParserTop(host).parse();
		//		}

		if (debug) Gpr.debug("Host info updated: " + host + "\n\t" + host.getResources() + "\n" + host.getHealth());
		Gpr.debug("Host info updated: " + host //
				+ "\nResources: " + host.getResources() //
				+ "\nHeath:\n" + host.getHealth() //
				+ "\nCondition: " + host.getHealth().condition() //
		);
	}

}
