package org.bds.osCmd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bds.BdsLog;
import org.bds.cluster.host.Host;
import org.bds.util.Gpr;

import com.jcraft.jsch.Channel;
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
public class Ssh implements BdsLog {

	public static String defaultKnownHosts = Gpr.HOME + "/.ssh/known_hosts";
	public static String defaultKnownIdentity[] = { Gpr.HOME + "/.ssh/id_rsa" };

	public static int MAX_ITER_DISCONNECT = 600;
	public static int WAIT_DISCONNECT = 100;
	public static int WAIT_READ = 100;
	public static int EXIT_CODE_DISCONNECT = 1;
	static int BUFFER_SIZE = 100 * 1024;

	boolean debug = false;
	boolean showStdout = false;
	int exitValue;
	JSch jsch;
	Session session;
	Channel channel;
	Host host;
	byte[] tmp = new byte[BUFFER_SIZE];

	public Ssh(Host host) {
		this.host = host;
	}

	/**
	 * Send a command and check for an acknowledge
	 */
	int checkAck(String scpCommand, OutputStream out, InputStream in) throws Exception {
		debug("SCP: Sending: '" + scpCommand + "'");
		if (scpCommand != null) {
			out.write(scpCommand.getBytes());
			out.flush();
		}

		debug("SCP: Waiting for acknowledge.");
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
	 * Connect to a remote host and return a channel (session and jsch are set)
	 */
	Channel connect(String channleType, String sshCommand) throws Exception {
		JSch.setConfig("StrictHostKeyChecking", "no"); // Not recommended, but useful
		jsch = new JSch();

		// Some "reasonable" defaults
		if (Gpr.exists(defaultKnownHosts)) jsch.setKnownHosts(defaultKnownHosts);
		for (String identity : defaultKnownIdentity)
			if (Gpr.exists(identity)) jsch.addIdentity(identity);

		// Create session and connect
		debug("Create conection:\n\tuser: '" + host.getUserName() + "'\n\thost : '" + host.getHostName() + "'\n\tport : " + host.getPort());
		session = jsch.getSession(host.getUserName(), host.getHostName(), host.getPort());
		session.setUserInfo(new SshUserInfo());
		session.connect();

		// Create channel
		channel = session.openChannel(channleType);
		if ((sshCommand != null) && (channel instanceof ChannelExec)) ((ChannelExec) channel).setCommand(sshCommand);

		return channel;
	}

	/**
	 * Diconnect, clear objects and set exit value
	 */
	int disconnect(boolean force) {
		// Close channel
		if (channel != null) {
			if (!force) waitChannel();

			// Channel is closed, now we can get exit status
			if (!force && channel.isClosed()) exitValue = channel.getExitStatus();
			else exitValue = EXIT_CODE_DISCONNECT; // There was an error and we were forced to close the channel

			// OK, we can disconnect now
			channel.disconnect();
			channel = null;
		}

		// Close session
		if (session != null) {
			session.disconnect();
			session = null;
		}

		if (jsch != null) jsch = null;

		return exitValue;
	}

	/**
	 * Connect via ssh and execute a command (e.g. execute "ls -al" in remote host)
	 *
	 * Reference: http://www.jcraft.com/jsch/examples/Exec.java.html
	 */
	public String exec(String command) {
		// Open ssh session
		try {
			Gpr.debug("COMMAND: " + command);
			channel = connect("exec", command);
			ChannelExec chexec = (ChannelExec) channel;
			chexec.setInputStream(null);
			chexec.setPty(true); // Allocate pseudo-tty (same as "ssh -t")

			if (debug) {
				// Show only in debug mode
				chexec.setErrStream(System.err);
				chexec.setOutputStream(System.out);
			}

			// Connect channel
			chexec.connect();

			// Read input
			String result = readChannel(true);

			disconnect(false); // Disconnect and get exit code
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public int getExitValue() {
		return exitValue;
	}

	/**
	 * Force disconnection
	 */
	public void kill() {
		disconnect(true);
	}

	/**
	 * Read an input stream while data is available
	 */
	String readAvailable(InputStream in) throws IOException {
		if (in.available() > 0) {
			int i = in.read(tmp, 0, BUFFER_SIZE);
			if (i < 0) return null;

			String recv = new String(tmp, 0, i);
			if (showStdout) System.out.print(recv);
			return recv;
		}

		return null;
	}

	/**
	 * Read channle's input
	 */
	String readChannel(boolean waitClose) {
		StringBuilder stdout = new StringBuilder();
		try {
			InputStream in = channel.getInputStream();

			while (waitClose && !channel.isClosed()) {
				String recv = readAvailable(in);
				if (recv != null) stdout.append(recv);

				Thread.sleep(WAIT_READ);
			}

			String recv = readAvailable(in);
			if (recv != null) stdout.append(recv);
		} catch (Exception e) {
			if (debug) e.printStackTrace();
		}

		return stdout.toString();
	}

	/**
	 * Copy a local file to a remote file
	 *
	 * Reference: http://www.jcraft.com/jsch/examples/ScpTo.java.html
	 *
	 * @param localFileName : Local file name
	 * @param remoteFileName : Remote file name
	 */
	public void scpTo(String localFileName, String remoteFileName) throws Exception {
		debug("SCP " + localFileName + " " + remoteFileName);
		String scpcommand = "scp -t " + remoteFileName;
		channel = connect("exec", scpcommand);
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

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setShowStdout(boolean showStdout) {
		this.showStdout = showStdout;
	}

	/**
	 * Wait for channel
	 */
	void waitChannel() {
		// Wait until channel is finished (otherwise redirections will not work)
		for (int i = 0; (i < MAX_ITER_DISCONNECT) && !channel.isClosed(); i++) {
			debug(i + "Disconnect: closed: " + channel.isClosed() + ", eof: " + channel.isEOF() + ", connected: " + channel.isConnected());
			try {
				Thread.sleep(WAIT_DISCONNECT);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

		debug("Disconnect: closed: " + channel.isClosed() + ", eof: " + channel.isEOF() + ", connected: " + channel.isConnected());
	}
}

class SshUserInfo implements UserInfo, BdsLog {

	boolean debug = false;

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
		debug("SSH Message: " + arg0);
		return false;
	}

	@Override
	public boolean promptPassword(String arg0) {
		debug("SSH Message: " + arg0);
		return true;
	}

	@Override
	public boolean promptYesNo(String arg0) {
		debug("SSH Message: " + arg0);
		return true;
	}

	@Override
	public void showMessage(String arg0) {
		System.err.println("SSH Message: " + arg0);
	}
}
