package ca.mcgill.mcb.pcingola.bigDataScript.cluster.commandParser;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * A command parser for 'system_profiler' command (OS X) 
 * 
 * @author pcingola@mcgill.ca
 */
public class CommandParserSystemProfiler extends CommandParser {

	public static boolean debug = false;

	public CommandParserSystemProfiler(Host host, SshOld ssh) {
		super(host, ssh, "system_profiler");
	}

	@Override
	public void parse(String cmdResult[]) {
		for (int line = 0; line < cmdResult.length; line++) {
			String lineStr = cmdResult[line].trim();
			String fields[] = lineStr.split(":");

			if (fields.length > 1) {
				String name = fields[0].trim();
				String value = fields[1].trim();
				if (name.equalsIgnoreCase("Processor Name")) host.getHealth().setCpuInfo(value);
				if (name.equalsIgnoreCase("Processor Speed")) host.getHealth().setCpuInfo(host.getHealth().getCpuInfo() + " " + value);
				if (name.equalsIgnoreCase("Number Of Processors")) host.getResources().setCpus(Gpr.parseIntSafe(value));
				if (name.equalsIgnoreCase("Total Number Of Cores")) host.getResources().setCpus(Gpr.parseIntSafe(value));
				if (name.equalsIgnoreCase("Memory")) {
					String memFields[] = value.split("\\s+");
					if (memFields.length >= 2) {
						long mem = Gpr.parseIntSafe(memFields[0]);

						if (memFields[1].equalsIgnoreCase("GB")) mem *= 1024 * 1024;
						else if (memFields[1].equalsIgnoreCase("MB")) mem *= 1024;

						host.getResources().setMem((int) (mem / 1024));
					}
				}

				if (debug) {
					Gpr.debug("line[" + line + "]: '" + lineStr + "'");
					for (int i = 0; i < fields.length; i++)
						Gpr.debug("\t\tfields[" + i + "]: '" + fields[i] + "'");
				}
			}
		}
	}
}
