package org.bds.cluster.commandParser;

import java.util.ArrayList;

import org.bds.BdsLog;
import org.bds.cluster.host.HostSsh;
import org.bds.osCmd.Ssh;

/**
 * A generic command parser
 * This is a class that is used to send commands and parse responses from hosts
 *
 * @author pcingola@mcgill.ca
 */
public class CommandParser implements BdsLog {

	public static int MAX_DETAILS_LINES = 20;
	public static int MAX_LINE_LEN = 80;
	public static final String[] EMPTY_STRING_ARRAY = new String[0];

	HostSsh host;
	Ssh ssh;
	String cmd;

	public CommandParser(HostSsh host, String cmd) {
		this.host = host;
		this.cmd = createCmd(cmd);
		debug("Commnd to parse: " + this.cmd);
	}

	/**
	 * Create a list of commands and 'identifier' lines (echo '#cmdname')
	 */
	String createCmd(String cmd) {
		StringBuilder sb = new StringBuilder();
		String cmds[] = cmd.split(";");
		for (String c : cmds) {
			c = c.trim();
			sb.append("echo '#" + c + "';" + c + ";");
		}
		return sb.toString();
	}

	public void parse() {
		parse(true);
	}

	/**
	 * Parse command's output
	 */
	public void parse(boolean updateAlive) {
		try {
			//---
			// Connect and get answer from server
			//---
			Ssh ssh = new Ssh(host);
			String result = ssh.exec(cmd);
			debug("\n---------- RESULT:Start ----------\n" + result + "---------- RESULT:End ----------");

			// Parse results (if any)
			if ((result != null) && (result.length() > 0)) {
				//---
				// Parse the results. Split each command and parse separately
				//---
				String command = null;

				result.replace('\r', ' ');
				String res[] = result.split("\n");
				ArrayList<String> lines = new ArrayList<String>();

				for (String line : res) {
					// New 'command'?
					if (line.startsWith("#")) {
						// Parse old section
						if (command != null) parse(command, lines);

						// Create new command
						command = line.substring(1).replaceAll(" ", "_").trim();
						lines = new ArrayList<String>();
					} else lines.add(line);

				}

				// Parse last command
				if (command != null) parse(command, lines);

				// We were able to connect and got some results, so probably the host is alive.
				if (updateAlive) host.getHealth().setAlive(true);
			} else {
				debug("Error trying to connect: Empty result string");
				// Could not connect
				host.getHealth().setAlive(false);
			}

			// Store results
			String key = this.getClass().getSimpleName().substring("CommandParser".length()); // Command parser name (minus the 'CommandParser' prefix)
			host.getHealth().setNote(key, result);
		} catch (Exception e) {
			error("Error trying to connect:\n" + e);
			// Could not connect
			host.getHealth().setAlive(false);
		}
	}

	public void parse(String cmdResult[]) {
		throw new RuntimeException("This should never be invoked!");
	}

	/**
	 * Invoke a command parser
	 * @param command
	 * @param lines
	 */
	public void parse(String command, ArrayList<String> lines) {
		String cmdResult[] = lines.toArray(EMPTY_STRING_ARRAY);

		CommandParser cp = null;

		if (command.startsWith("df")) cp = new CommandParserDf(host);
		else if (command.startsWith("sysctl")) cp = new CommandParserSystemProfiler(host);
		else if (command.startsWith("top")) cp = new CommandParserTop(host);
		else if (command.startsWith("uname")) cp = new CommandParserUname(host);
		else if (command.startsWith("uptime")) cp = new CommandParserUptime(host);
		else if (command.startsWith("who")) cp = new CommandParserWho(host);
		else if (command.startsWith("cat_/proc/meminfo")) cp = new CommandParserMemInfo(host);
		else if (command.startsWith("cat_/proc/cpuinfo")) cp = new CommandParserCpuInfo(host);
		else throw new RuntimeException("Unknown parser for command '" + command + "'");

		// Parse
		debug("Parsing: " + command);
		cp.parse(cmdResult);
	}
}
