package org.bds.cluster.commandParser;

import org.bds.cluster.host.HostSsh;
import org.bds.util.Gpr;

/**
 * A command parser for 'uptime' command 
 * 
 * @author pcingola@mcgill.ca
 */
public class CommandParserUptime extends CommandParser {

	public static boolean debug = false;

	public CommandParserUptime(HostSsh host) {
		super(host, "uptime");
	}

	@Override
	public void parse(String cmdResult[]) {
		String fields[] = cmdResult[0].replace(',', ' ').split("\\s+");

		if (debug) {
			for (int i = 0; i < fields.length; i++)
				Gpr.debug("fields[" + i + "]: " + fields[i]);
		}

		for (int i = 0; i < fields.length; i++) {
			if (fields[i].equalsIgnoreCase("load") && (fields.length > i + 2)) {
				String loadAvg = fields[i + 2];
				host.getHealth().setLoadAvg(Gpr.parseDoubleSafe(loadAvg));
			}
		}
	}
}
