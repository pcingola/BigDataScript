package org.bds.cluster.commandParser;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

import org.bds.cluster.host.HostSsh;

/**
 * A command parser for 'who' command 
 * 
 * @author pcingola@mcgill.ca
 */
public class CommandParserWho extends CommandParser {

	public static boolean debug = false;

	public CommandParserWho(HostSsh host) {
		super(host, "who");
	}

	@Override
	public void parse(String cmdResult[]) {
		HashSet<String> users = new HashSet<String>();

		for (int line = 0; line < cmdResult.length; line++) {
			String fields[] = cmdResult[line].split("\\s+");
			if (fields[0].length() > 0) users.add(fields[0]);
		}

		// Number of users logged in
		host.getHealth().setLoggedInUsersCount(users.size());

		// Create a sorted list of logged in users
		String allUsers = "";
		LinkedList<String> lusers = new LinkedList<String>();
		lusers.addAll(users);
		Collections.sort(lusers);
		for (String u : lusers)
			allUsers += u + " ";
		host.getHealth().setLoggedInUsers(allUsers);
	}
}
