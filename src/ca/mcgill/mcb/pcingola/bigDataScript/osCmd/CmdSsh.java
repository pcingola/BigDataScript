package ca.mcgill.mcb.pcingola.bigDataScript.osCmd;

import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * Executes an command in a remote host, via ssh
 * 
 * @author pcingola
 */
public class CmdSsh extends Cmd {

	public static boolean debug = true;
	public static final String[] EMPTY_STRING_ARRAY = new String[0];

	Ssh ssh;

	public CmdSsh(String cmdId, String args[]) {
		super(cmdId, args);
	}

	@Override
	protected void execCmd() throws Exception {
		// Build command 
		StringBuilder cmdsb = new StringBuilder();
		if (commandArgs != null) {
			for (String arg : commandArgs)
				cmdsb.append(" " + arg);
		}

		String command = cmdsb.toString().trim();

		// Execute ssh
		ssh = new Ssh(host);
		ssh.setShowStdout(true);
		Gpr.debug("Command: " + command);
		ssh.exec(command);
	}

	@Override
	protected void execPrepare() throws Exception {
		// Nothing to do
	}

	@Override
	protected void killCmd() {
		ssh.kill();
	}

}
