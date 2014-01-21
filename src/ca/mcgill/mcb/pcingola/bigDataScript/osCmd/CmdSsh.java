package ca.mcgill.mcb.pcingola.bigDataScript.osCmd;

/**
 * Executes an command in a remote host, via ssh
 * 
 * @author pcingola
 */
public class CmdSsh extends Cmd {

	public static boolean debug = true;
	public static final String[] EMPTY_STRING_ARRAY = new String[0];

	Ssh ssh;
	String localFileName;
	String remoteFileName;

	public CmdSsh(String cmdId, String args[]) {
		super(cmdId, args);
	}

	public CmdSsh(String cmdId, String localFileName, String remoteFileName) {
		super(cmdId, EMPTY_STRING_ARRAY);
		this.localFileName = localFileName;
		this.remoteFileName = remoteFileName;
	}

	@Override
	protected void execCmd() throws Exception {
		// Build command 
		StringBuilder cmdsb = new StringBuilder();
		for (String arg : commandArgs)
			cmdsb.append(" " + arg);
		String command = cmdsb.toString().trim();

		// Nothing in the command line? Just execute remote file
		if (command.isEmpty()) command = remoteFileName;

		// Execute ssh
		ssh = new Ssh(host);
		ssh.setShowStdout(true);
		ssh.exec(command);
	}

	@Override
	protected void execPrepare() throws Exception {
		// Should we copy these files?
		if ((localFileName != null) && (remoteFileName != null)) {
			// Copy local file to remote destination 
			ssh = new Ssh(host);
			ssh.scpTo(localFileName, remoteFileName);
		}
	}

	@Override
	protected void killCmd() {
		ssh.kill();
	}

}
