package ca.mcgill.mcb.pcingola.bigDataScript.cluster.commandParser;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * A command parser for 'df' command 
 * 
 * @author pcingola@mcgill.ca
 */
public class CommandParserDf extends CommandParser {

	public static boolean debug = false;

	public CommandParserDf(Host host, SshOld ssh) {
		super(host, ssh, "df -kl"); // Show only local file systems ("-l") in KBytes ("-k")
	}

	@Override
	public void parse(String cmdResult[]) {
		long total = 0, avail = 0, used = 0;

		for (int line = 1; line < cmdResult.length; line++) {
			String fields[] = cmdResult[line].split("\\s+");

			if (fields.length >= 6) {
				total += Gpr.parseLongSafe(fields[1]);
				used += Gpr.parseLongSafe(fields[2]);
				avail += Gpr.parseLongSafe(fields[3]);
			} else if (fields.length >= 5) {
				total += Gpr.parseLongSafe(fields[0]);
				used += Gpr.parseLongSafe(fields[1]);
				avail += Gpr.parseLongSafe(fields[2]);
			}
		}

		host.getHealth().setFsTotal(total);
		host.getHealth().setFsAvail(avail);
	}
}
