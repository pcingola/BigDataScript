package org.bds.cluster.commandParser;

import org.bds.cluster.host.HostSsh;
import org.bds.util.Gpr;

/**
 * A command parser for 'top -l 1' command (OS X)
 *
 * @author pcingola@mcgill.ca
 */
public class CommandParserTop extends CommandParser {

	public static boolean debug = false;

	public CommandParserTop(HostSsh host) {
		super(host, "top -l 1");
	}

	@Override
	public void parse(String cmdResult[]) {
		long memFree = -1, memUsed = -1;
		for (int line = 0; line < cmdResult.length; line++) {
			String lineStr = cmdResult[line].trim();
			String fields[] = lineStr.split(":");

			if (fields.length > 1) {
				String name = fields[0].trim();
				String value = fields[1].trim();

				// Physical memory usage
				// Sample line: "PhysMem:  375M wired,   84M active,   13M inactive,  485M used, 3611M free."
				if (name.equalsIgnoreCase("PhysMem")) {

					// Split fields by semicolon
					String memFields[] = value.split(",");
					for (int fieldNum = 0; fieldNum < memFields.length; fieldNum++) {
						// Split by space
						String memSubFields[] = memFields[fieldNum].replace('.', ' ').trim().split("\\s+");

						if (debug) Gpr.debug("memFields[" + fieldNum + "] : " + memFields[fieldNum]);

						if (memSubFields[1].equalsIgnoreCase("used")) memUsed = parseMem(memSubFields[0]);
						if (memSubFields[1].equalsIgnoreCase("free")) memFree = parseMem(memSubFields[0]);
					}
				}

				if (debug) {
					Gpr.debug("line[" + line + "]: '" + lineStr + "'");
					for (int i = 0; i < fields.length; i++)
						Gpr.debug("\t\tfields[" + i + "]: '" + fields[i] + "'");
				}
			}
		}

		// Memory usage
		if ((memFree >= 0) && (memUsed >= 0)) {
			float memPerc = ((float) memUsed) / ((float) (memFree + memUsed));
			host.getHealth().setMemUsage(memPerc);
		}
	}

	/**
	 * Parse memory strings (e.g. "3611M")
	 * @param memStr
	 * @return
	 */
	long parseMem(String memStr) {
		long mem = 0;

		char unit = memStr.charAt(memStr.length() - 1);
		String memNum = memStr.substring(0, memStr.length() - 1);

		if (unit == 'K') mem = Gpr.parseLongSafe(memNum) * 1024;
		if (unit == 'M') mem = Gpr.parseLongSafe(memNum) * 1024 * 1024;
		if (unit == 'G') mem = Gpr.parseLongSafe(memNum) * 1024 * 1024 * 1024;
		if (unit == 'T') mem = Gpr.parseLongSafe(memNum) * 1024 * 1024 * 1024 * 1024;
		if (unit == 'P') mem = Gpr.parseLongSafe(memNum) * 1024 * 1024 * 1024 * 1024 * 1024;

		return mem;
	}
}
