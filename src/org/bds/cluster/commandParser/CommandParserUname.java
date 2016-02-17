package org.bds.cluster.commandParser;

import org.bds.cluster.host.HostSsh;
import org.bds.util.Gpr;

/**
 * A command parser for 'uname' command 
 * 
 * @author pcingola
 */
public class CommandParserUname extends CommandParser {

	public static boolean debug = false;

	public CommandParserUname(HostSsh host) {
		super(host, "uname");
	}

	@Override
	public void parse(String cmdResult[]) {
		String fields[] = cmdResult[0].split("\\s+");

		if (debug) {
			for (int i = 0; i < fields.length; i++)
				Gpr.debug("fields[" + i + "]: " + fields[i]);
		}

		if (fields != null) host.getHealth().setSystemType(fields[0]);
	}
}
