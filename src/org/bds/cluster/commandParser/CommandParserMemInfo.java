package org.bds.cluster.commandParser;

import org.bds.cluster.host.HostSsh;
import org.bds.util.Gpr;

/**
 * A command parser for 'cat /rpoc/meminfo' command
 *
 * @author pcingola@mcgill.ca
 */
public class CommandParserMemInfo extends CommandParser {

	public static boolean debug = false;

	public CommandParserMemInfo(HostSsh host) {
		super(host, "cat /proc/meminfo");
	}

	@Override
	public void parse(String cmdResult[]) {
		long swap, swapFree, mem, memFree;
		swap = swapFree = mem = memFree = -1;

		for (int line = 0; line < cmdResult.length; line++) {
			String fields[] = cmdResult[line].replace(':', ' ').split("\\s+");
			if (fields[0].equalsIgnoreCase("MemTotal")) {
				mem = Gpr.parseIntSafe(fields[1]);
				host.getResources().setMem(mem);
			}
			if (fields[0].equalsIgnoreCase("MemFree")) memFree = Gpr.parseLongSafe(fields[1]);
			if (fields[0].equalsIgnoreCase("SwapTotal")) swap = Gpr.parseLongSafe(fields[1]);
			if (fields[0].equalsIgnoreCase("SwapFree")) swapFree = Gpr.parseLongSafe(fields[1]);

			if (debug) {
				Gpr.debug("line[" + line + "]: " + cmdResult[line]);
				for (int i = 0; i < fields.length; i++)
					Gpr.debug("\tfields[" + i + "]: '" + fields[i] + "'");
			}
		}

		// Calculate swap usage
		debug("swap" + swap + " swapFree:" + swapFree);
		if ((swap > 0) && (swapFree >= 0)) {
			float swapUsage = ((float) (swap - swapFree)) / ((float) swap);
			host.getHealth().setSwapUsage(swapUsage);
		}

		// Calculate memory usage
		debug("mem:" + mem + " memFree:" + memFree);
		if ((mem > 0) && (memFree >= 0)) {
			float memUsage = ((float) (mem - memFree)) / ((float) mem);
			host.getHealth().setMemUsage(memUsage);
		}

	}
}
