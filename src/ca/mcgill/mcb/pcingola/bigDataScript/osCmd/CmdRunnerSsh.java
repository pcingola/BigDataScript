package ca.mcgill.mcb.pcingola.bigDataScript.osCmd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

/**
 * Executes an command in a remote host, via ssh
 * 
 * Most of this code is copied from JSch 
 * examples (http://www.jcraft.com/jsch/examples/)
 * 
 * Important: We force the allocation of a pseudo-tty (see channel.setPty(true) )
 * in order to get the commands killed if the SSH connection is lost (e.g. local 
 * command killed). By doing this, we don't have to worry about leaving 
 * commands running on a server when our script died. Otherwise we should
 * have a mechanism to log into the server and kill the processes (which may not
 * be feasible if the network is down).
 * 
 * @author pcingola
 */
public class CmdRunnerSsh extends CmdRunner {

	public static String defaultKnownHosts = Gpr.HOME + "/.ssh/known_hosts";
	public static String defaultKnownIdentity[] = { Gpr.HOME + "/.ssh/id_dsa", Gpr.HOME + "/.ssh/id_rsa" };

	public static int MAX_ITER_DISCONNECT = 600;
	public static int WAIT_DISCONNECT = 100;
	static int BUFFER_SIZE = 100 * 1024;

	public static boolean debug = false;
	JSch jsch;
	Session session;
	ChannelExec channel;
	String programFile;

	public CmdRunnerSsh(String cmdId, String programFile, String commandArgs[]) {
		super(cmdId, commandArgs);
		this.programFile = programFile;
	}

	/**
	 * Send a command and check for an acknowledge
	 * @param in
	 * @return
	 * @throws IOException
	 */
	int checkAck(String scpCommand, OutputStream out, InputStream in) throws Exception {
		if (debug) Gpr.debug("SCP: Sending: '" + scpCommand + "'");
		if (scpCommand != null) {
			out.write(scpCommand.getBytes());
			out.flush();
		}

		if (debug) Gpr.debug("SCP: Waiting for acknowledge.");
		int b = in.read();
		// Values are
		//          0 for success,
		//          1 for error,
		//          2 for fatal error,
		//          -1
		if (b <= 0) return b;

		if (b == 1 || b == 2) {
			StringBuffer sb = new StringBuffer();
			int c;
			do {
				c = in.read();
				sb.append((char) c);
			} while (c != '\n');

			if (b == 1) throw new Exception("SCP command error: " + sb.toString());
			if (b == 2) throw new Exception("SCP command fatal error: " + sb.toString());
		}

		return b;
	}

	/** 
	 * Diconect, clear objects and set exit value
	 */
	void disconnect(boolean force) {
		if (channel != null) {

			// Wait until channel is finished (otherwise redirections will not work)
			for (int i = 0; !force && (i < MAX_ITER_DISCONNECT) && !channel.isClosed(); i++) {
				if (debug) Gpr.debug(i + "\t\tDisconnect:\tclosed: " + channel.isClosed() + "\teof: " + channel.isEOF() + "\tconnected: " + channel.isConnected());
				try {
					Thread.sleep(WAIT_DISCONNECT);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
			if (debug) Gpr.debug("\t\tDisconnect:\tclosed: " + channel.isClosed() + "\teof: " + channel.isEOF() + "\tconnected: " + channel.isConnected());

			// Channel is closed, now we can get exit status
			if (!force && channel.isClosed()) exitValue = channel.getExitStatus();

			// OK, we can disconnect now
			channel.disconnect();
			channel = null;
		}

		if (session != null) {
			// Close session
			session.disconnect();
			session = null;
		}

		if (jsch != null) jsch = null;
	}

	@Override
	public int exec() {
		try {
			//			createTimeoutCommand(); // Handle timeout requirement here (if possible)

			executing = true;

			// Copy file to remote host
			started = true;
			String remotefileName = Gpr.baseName(programFile);
			scpTo(programFile, remotefileName);

			// Execute remote file
			if (executing) sshExec(remotefileName);

			executing = false;
		} catch (Exception e) {
			error = e.getMessage();
			exitValue = -1;
			if (debug) e.printStackTrace();
			disconnect(false);
		} finally {
			// Disconnect and set exit value
			disconnect(true);

			// We are done. Either process finished or an exception was raised.
			started = true;
			executing = false;
			if (task != null) {
				// Add command stats (now we only have exitValue)
				task.setExitValue(exitValue);
				task.setDone(true);
			}
		}

		return exitValue;
	}

	@Override
	public void kill() {
		executing = false;
		disconnect(true);

		// Update task stats
		if (task != null) {
			if (debug) Gpr.debug("Killed: Setting stats " + task);
			task.setExitValue(-1);
			task.setDone(true);
		}
	}

	/**
	 * Copy a local file to a remote file
	 * 
	 * Reference: http://www.jcraft.com/jsch/examples/ScpTo.java.html
	 * 
	 * @param localFileName : Local file name
	 * @param remoteFileName : Remote file name
	 */
	void scpTo(String localFileName, String remoteFileName) throws Exception {
		if (debug) Gpr.debug("SCP " + localFileName + " " + remoteFileName);
		String scpcommand = "scp -t " + remoteFileName;
		channel = sshConnect(scpcommand);
		File lfile = new File(localFileName);

		// Get I/O streams for remote scp
		OutputStream out = channel.getOutputStream();
		InputStream in = channel.getInputStream();

		// Connect
		channel.connect();
		if (checkAck(null, out, in) != 0) throw new Exception("Error in SCP (connect command was not acknoledged)");

		// Send "C0644 fileSize fileName", where filename should not include '/'
		long filesize = lfile.length();
		scpcommand = "C0644 " + filesize + " " + Gpr.baseName(localFileName) + "\n";
		if (checkAck(scpcommand, out, in) != 0) throw new Exception("Error in SCP ('C' command was not acknoledged)");

		// Send a contents of localFileName
		FileInputStream fis = new FileInputStream(localFileName);
		byte[] buf = new byte[BUFFER_SIZE];
		while (true) {
			int len = fis.read(buf, 0, buf.length);
			if (len <= 0) break;
			out.write(buf, 0, len); //out.flush();
		}
		fis.close();
		fis = null;
		// send '\0'
		buf[0] = 0;
		out.write(buf, 0, 1);
		out.flush();

		if (checkAck(null, out, in) != 0) throw new Exception("Error in SCP ('C' command was not acknoledged)");
		out.close();

		disconnect(false);
	}

	/**
	 * Connect to a remote host and return a channel (session and jsch are set)
	 */
	ChannelExec sshConnect(String sshCommand) throws Exception {
		JSch.setConfig("StrictHostKeyChecking", "no"); // Not recommended, but useful
		jsch = new JSch();

		// Some "reasonable" defaults
		if (Gpr.exists(defaultKnownHosts)) jsch.setKnownHosts(defaultKnownHosts);
		for (String identity : defaultKnownIdentity)
			if (Gpr.exists(identity)) jsch.addIdentity(identity);

		//---
		// Start process & wait until completion
		//---
		started = true;

		// Create session and connect
		if (debug) Gpr.debug("Create conection");
		session = jsch.getSession(host.getUserName(), host.getHostName(), host.getPort());
		session.setUserInfo(new SshUserInfo());
		session.connect();

		// Create channel
		if (debug) Gpr.debug("Create channel");
		channel = (ChannelExec) session.openChannel("exec");
		if (sshCommand != null) {
			if (debug) Gpr.debug("Channel command: " + sshCommand);
			channel.setCommand(sshCommand);
		}

		return channel;
	}

	/**
	 * Connect via ssh and execute a script (execute "/bin/sh -e program" in remote host)
	 * 
	 * Reference: http://www.jcraft.com/jsch/examples/Exec.java.html
	 * 
	 * @throws Exception
	 */
	void sshExec(String programFile) throws Exception {
		// Build command string
		StringBuilder cmdStr = new StringBuilder();
		for (String arg : commandArgs)
			cmdStr.append((cmdStr.length() > 0 ? " " : "") + arg);

		Gpr.debug("CMDSTR: " + cmdStr);
		channel = sshConnect(cmdStr.toString());
		channel.setInputStream(null);
		channel.setPty(true); // Allocate pseudo-tty (same as "ssh -t"?)

		//---
		// Redirect?
		//---
		InputStream in = null;

		throw new RuntimeException("Unimplemented!!!");
		//		if (redirectStderr != null) {
		//			TeeOutputStream teeStderr = new TeeOutputStream(System.err, new FileOutputStream(redirectStderr));
		//			channel.setErrStream(teeStderr);
		//		} else channel.setErrStream(System.err);
		//
		//		if (redirectStdout != null) {
		//			TeeOutputStream teeStdout = new TeeOutputStream(System.out, new FileOutputStream(redirectStdout));
		//			channel.setOutputStream(teeStdout);
		//		} else {
		//			channel.setOutputStream(System.out);
		//
		//			// Read input
		//			if (debug) Gpr.debug("Read channel input");
		//			in = channel.getInputStream();
		//		}
		//
		//		// Connect channel
		//		channel.connect();
		//		if (debug) Gpr.debug("Ssh channel contected");
		//
		//		if (redirectStdout == null) {
		//			String resultStr = Gpr.read(in);
		//			if (debug) Gpr.debug("Ssh results (length: " + resultStr.length() + ")\n" + resultStr);
		//		}
		//
		//		// Diconnect
		//		disconnect(false);
	}
}

class SshUserInfo implements UserInfo {

	@Override
	public String getPassphrase() {
		return null;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public boolean promptPassphrase(String arg0) {
		Gpr.debug("SSH Message: " + arg0);
		return false;
	}

	@Override
	public boolean promptPassword(String arg0) {
		Gpr.debug("SSH Message: " + arg0);
		return true;
	}

	@Override
	public boolean promptYesNo(String arg0) {
		Gpr.debug("SSH Message: " + arg0);
		return true;
	}

	@Override
	public void showMessage(String arg0) {
		System.err.println("SSH Message: " + arg0);
	}
}
