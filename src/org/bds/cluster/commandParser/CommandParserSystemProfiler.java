package org.bds.cluster.commandParser;

import org.bds.cluster.host.HostSsh;
import org.bds.util.Gpr;

/**
 * A command parser for 'system_profiler' command (OS X) 
 * 
 * @author pcingola@mcgill.ca
 */
public class CommandParserSystemProfiler extends CommandParser {

	public static boolean debug = false;

	public CommandParserSystemProfiler(HostSsh host) {
		super(host, "sysctl -a");
	}

	@Override
	public void parse(String cmdResult[]) {
		for (int line = 0; line < cmdResult.length; line++) {
			String lineStr = cmdResult[line].trim();
			String fields[] = lineStr.split(":", 2);

			if (fields.length > 1) {
				String name = fields[0].trim();
				String value = fields[1].trim();
				if (name.equalsIgnoreCase("machdep.cpu.brand_string")) host.getHealth().setCpuInfo(value);
				if (name.equalsIgnoreCase("hw.ncpu")) host.getResources().setCpus(Gpr.parseIntSafe(value));
				if (name.equalsIgnoreCase("hw.logicalcpu")) host.getResources().setCpus(Gpr.parseIntSafe(value));
				if (name.equalsIgnoreCase("hw.memsize")) host.getResources().setMem(Gpr.parseLongSafe(value));

				if (debug) {
					Gpr.debug("line[" + line + "]: '" + lineStr + "'");
					for (int i = 0; i < fields.length; i++)
						Gpr.debug("\t\tfields[" + i + "]: '" + fields[i] + "'");
				}
			}
		}
	}
}
