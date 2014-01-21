package ca.mcgill.mcb.pcingola.bigDataScript.osCmd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

//class CommandInputStream extends InputStream {
//
//	StringBuilder sb = new StringBuilder();
//	int pos = 0;
//
//	public synchronized void append(String line) {
//		sb.append(line);
//		notify();
//	}
//
//	@Override
//	public int read() throws IOException {
//		do {
//			// Is there anything in the buffer?
//			if (pos < sb.length()) {
//				char c = sb.charAt(pos++);
//				System.err.print(c);
//				return c;
//			}
//
//			if (waitInput()) return -1;
//		} while (true);
//	}
//
//	synchronized boolean waitInput() {
//		try {
//			wait();
//			return false;
//		} catch (InterruptedException e) {
//			return true;
//		}
//
//	}
//}

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
public class Ssh { // extends Thread {

	public static String defaultKnownHosts = Gpr.HOME + "/.ssh/known_hosts";
	public static String defaultKnownIdentity[] = { Gpr.HOME + "/.ssh/id_dsa", Gpr.HOME + "/.ssh/id_rsa" };

	public static int MAX_ITER_DISCONNECT = 600;
	public static int WAIT_DISCONNECT = 100;
	public static int WAIT_READ = 100;
	public static int EXIT_CODE_DISCONNECT = 1;
	static int BUFFER_SIZE = 100 * 1024;

	boolean debug = true;
	//	boolean running;
	int exitValue;
	JSch jsch;
	Session session;
	Channel channel;
	Host host;
	//	CommandInputStream stdin;
	byte[] tmp = new byte[BUFFER_SIZE];

	public Ssh(Host host) {
		this.host = host;
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
		if (debug) Gpr.debug("Create conection:\n\tuser: '" + host.getUserName() + "'\n\thost : '" + host.getHostName() + "'\n\tport : " + host.getPort());
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
	 * 
	 * @throws Exception
	 */
	public int exec(String command) throws Exception {
		// Open ssh session
		channel = connect("exec", command);
		ChannelExec chexec = (ChannelExec) channel;
		chexec.setInputStream(null);
		chexec.setPty(true); // Allocate pseudo-tty (same as "ssh -t")

		// These don't seem to work
		chexec.setErrStream(System.err);
		chexec.setOutputStream(System.out);

		// Connect channel
		chexec.connect();

		// Read input
		String resultStr = readChannel(true);
		Gpr.debug("Ssh results (length: " + resultStr.length() + ")\n" + resultStr);

		return disconnect(false); // Diconnect
	}

	//	/**
	//	 * Append to stdin
	//	 * @param str
	//	 */
	//	public void input(String str) {
	//		stdin.append(str + "\n");
	//	}

	//	public boolean isRunning() {
	//		return running;
	//	}

	//	public void kill() {
	//		running = false;
	//	}

	/**
	 * Read an input stream while data is available
	 * @param in
	 * @param sb
	 * @param tmp
	 * @throws IOException
	 */
	String readAvailable(InputStream in) throws IOException {
		if (in.available() > 0) {
			int i = in.read(tmp, 0, BUFFER_SIZE);
			if (i < 0) return null;

			String recv = new String(tmp, 0, i);
			System.out.print(recv);
			return recv;
		}

		return null;
	}

	/**
	 * Read channle's input
	 * @return
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
			if (debug) Gpr.debug("Exception: " + e);
		}

		return stdout.toString();
	}

	//	/**
	//	 * Run as a thread
	//	 */
	//	@Override
	//	public void run() {
	//
	//		try {
	//			stdin = new CommandInputStream();
	//			stdin.append("ls -al\n");
	//			shellOpen();
	//			InputStream in = channel.getInputStream();
	//			running = true;
	//
	//			while (running) {
	//				// Read input
	//				String recv = readAvailable(in);
	//				Gpr.debug("READ: '" + recv + "'");
	//				if (recv != null) System.out.print(recv);
	//				Thread.sleep(WAIT_READ);
	//			}
	//
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		} finally {
	//			disconnect(true); // Diconnect
	//		}
	//	}

	/**
	 * Copy a local file to a remote file
	 * 
	 * Reference: http://www.jcraft.com/jsch/examples/ScpTo.java.html
	 * 
	 * @param localFileName : Local file name
	 * @param remoteFileName : Remote file name
	 */
	public void scpTo(String localFileName, String remoteFileName) throws Exception {
		if (debug) Gpr.debug("SCP " + localFileName + " " + remoteFileName);
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

	//	/**
	//	 * Connect via ssh and execute a shell 
	//	 * 
	//	 * Reference: http://www.jcraft.com/jsch/examples/Shell.java.html
	//	 * 
	//	 * @throws Exception
	//	 */
	//	public void shellOpen() throws Exception {
	//		// Open ssh session
	//		channel = connect("shell", null);
	//		ChannelShell chexec = (ChannelShell) channel;
	//
	//		chexec.setInputStream(stdin);
	//		chexec.setPty(true); // Allocate pseudo-tty (same as "ssh -t")
	//
	//		// These don't seem to work
	//		//		chexec.setErrStream(System.err);
	//		chexec.setOutputStream(System.out);
	//
	//		// Connect channel
	//		chexec.connect();
	//	}

	/**
	 * Wait for channel
	 */
	void waitChannel() {
		// Wait until channel is finished (otherwise redirections will not work)
		for (int i = 0; (i < MAX_ITER_DISCONNECT) && !channel.isClosed(); i++) {
			if (debug) Gpr.debug(i + "\t\tDisconnect:\tclosed: " + channel.isClosed() + "\teof: " + channel.isEOF() + "\tconnected: " + channel.isConnected());
			try {
				Thread.sleep(WAIT_DISCONNECT);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

		if (debug) Gpr.debug("\t\tSSH disconnect:\tclosed: " + channel.isClosed() + "\teof: " + channel.isEOF() + "\tconnected: " + channel.isConnected());
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
