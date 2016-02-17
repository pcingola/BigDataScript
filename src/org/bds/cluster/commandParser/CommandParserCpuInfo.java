package org.bds.cluster.commandParser;

import org.bds.cluster.host.HostSsh;
import org.bds.util.Gpr;

/**
 * A command parser for 'cat /rpoc/cpuinfo' command 
 * 
 * @author pcingola@mcgill.ca
 */
public class CommandParserCpuInfo extends CommandParser {

	public static boolean debug = false;

	public CommandParserCpuInfo(HostSsh host) {
		super(host, "cat /proc/cpuinfo");
	}

	@Override
	public void parse(String cmdResult[]) {
		int countCpus = 0;

		for (int line = 0; line < cmdResult.length; line++) {
			String fields[] = cmdResult[line].trim().replace(':', ' ').split("\\s+");

			if (fields[0].equalsIgnoreCase("processor")) countCpus++;

			if (fields[0].equalsIgnoreCase("model")) {
				String cpuInfo[] = cmdResult[line].split(":");
				host.getHealth().setCpuInfo(cpuInfo[1].replaceAll("\\s+", " ").trim()); // Collapse multiple spaces and trim
			}

			if (debug) {
				Gpr.debug("line[" + line + "]: " + cmdResult[line]);
				for (int i = 0; i < fields.length; i++)
					Gpr.debug("\tfields[" + i + "]: '" + fields[i] + "'");
			}
		}

		host.getResources().setCpus(countCpus);
	}
}
