package ca.mcgill.mcb.pcingola.bigDataScript.cluster.commandParser;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * A command parser for 'cat /rpoc/cpuinfo' command 
 * 
 * @author pcingola@mcgill.ca
 */
public class CommandParserCpuInfo extends CommandParser {

	public static boolean debug = false;

	public CommandParserCpuInfo(Host host) {
		super(host, "cat /proc/cpuinfo");
	}

	@Override
	public void parse(String cmdResult[]) {
		int countCpus = 0;

		for (int line = 0; line < cmdResult.length; line++) {
			Gpr.debug(line + "\t" + cmdResult[line]);
			String fields[] = cmdResult[line].trim().replace(':', ' ').split("\\s+");

			if (fields[0].equalsIgnoreCase("processor")) {
				countCpus++;
				Gpr.debug("CPUS: " + countCpus + "\t" + host.getHostName() + "\t" + cmdResult[line]);
			}

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
