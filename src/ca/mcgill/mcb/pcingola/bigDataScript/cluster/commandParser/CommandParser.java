package ca.mcgill.mcb.pcingola.bigDataScript.cluster.commandParser;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;

/**
 * A generic command parser 
 * This is a class that is used to send commands and parse responses from hosts
 * 
 * @author pcingola@mcgill.ca
 */
public abstract class CommandParser {

	public static int MAX_DETAILS_LINES = 20;
	public static int MAX_LINE_LEN = 80;

	Host host;
	SshOld ssh;
	String cmd;

	public CommandParser(Host host, SshOld ssh, String cmd) {
		this.host = host;
		this.cmd = cmd;
		this.ssh = ssh;
	}

	public void parse() {
		//		ssh.resetBuffers(); // Clear all buffers before proceeding
		//		ssh.send(cmd, Ssh.DEFAULT_CMD_WAIT); // Send command and collect results
		//		String result = ssh.getStdout();
		//
		//		// Parse results (if any)
		//		String result2 = "";
		//		if ((result != null) && (result.length() > 0)) {
		//			String res[] = result.split("\n");
		//			res[res.length - 1] = ""; // Remove last line (it has an 'alert string')
		//			parse(res);
		//
		//			// New 'results' string (remove last line)
		//			for (int i = 0; (i < MAX_DETAILS_LINES) && (i < (res.length - 1)); i++) {
		//				if (res[i].length() > MAX_LINE_LEN) result2 += res[i].substring(0, MAX_LINE_LEN) + "...\n";
		//				else result2 += res[i] + "\n";
		//			}
		//			if (res.length > MAX_DETAILS_LINES) result2 += "...\n";
		//		}
		//
		//		// Store results
		//		String key = this.getClass().getSimpleName().substring(13); // Command parser name (minus the 'CommandParser' prefix)
		//		host.getHealth().setNote(key, result2);
	}

	public abstract void parse(String cmdResult[]);
}
