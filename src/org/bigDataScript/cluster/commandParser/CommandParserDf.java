package org.bigDataScript.cluster.commandParser;

import org.bigDataScript.cluster.host.HostSsh;
import org.bigDataScript.util.Gpr;

/**
 * A command parser for 'df' command
 *
 * @author pcingola@mcgill.ca
 */
public class CommandParserDf extends CommandParser {

	public static boolean debug = false;

	public CommandParserDf(HostSsh host) {
		super(host, "df -kl"); // Show only local file systems ("-l") in KBytes ("-k")
	}

	@Override
	public void parse(String cmdResult[]) {
		long total = 0, avail = 0;

		for (int line = 1; line < cmdResult.length; line++) {
			String fields[] = cmdResult[line].split("\\s+");

			if (fields.length >= 6) {
				total += Gpr.parseLongSafe(fields[1]);
				avail += Gpr.parseLongSafe(fields[3]);
			} else if (fields.length >= 5) {
				total += Gpr.parseLongSafe(fields[0]);
				avail += Gpr.parseLongSafe(fields[2]);
			}
		}

		host.getHealth().setFsTotal(total);
		host.getHealth().setFsAvail(avail);
	}
}
