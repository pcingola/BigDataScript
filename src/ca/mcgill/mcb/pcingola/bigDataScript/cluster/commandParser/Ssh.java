package ca.mcgill.mcb.pcingola.bigDataScript.cluster.commandParser;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;

/**
 * A representation of an ssh connection (executes 'ssh' command
 * 
 * @author pcingola@mcgill.ca
 */
public class Ssh {

	public static final long DEFAULT_CMD_WAIT = 10000; // How much to wait when we send a command to the ssh connection. Default:10 seconds

	Host host;
	String error; // Latest error message
	//	CmdRunner sshCmd = null;
	String jobId = "";
	String head = "";

	public Ssh(Host host) {
		this.host = host;
	}

	/**
	 * Close connection to host (i.e. terminate ssh)
	 */
	public synchronized void close() {
		//		if (sshCmd != null) {
		//			sshCmd.kill();
		//			head = sshCmd.getHead();
		//		}
		//		sshCmd = null;
	}

	//	public String getHead() {
	//		if (sshCmd != null) return sshCmd.getHead();
	//		return head;
	//	}
	//
	//	public Host getHost() {
	//		return host;
	//	}
	//
	//	public String getStderr() {
	//		if (sshCmd != null) return sshCmd.getStderr();
	//		return "";
	//	}
	//
	//	public String getStdout() {
	//		if (sshCmd != null) return sshCmd.getStdout();
	//		return "";
	//	}
	//
	//	public boolean isRunning() {
	//		return sshCmd.isExecuting();
	//	}
	//
	//	public boolean isStarted() {
	//		if (sshCmd == null) return false;
	//		return sshCmd.isStarted();
	//	}
	//
	//	/**
	//	 * Open connection to host (ssh)
	//	 */
	//	public synchronized void open() {
	//		sshCmd = new CmdRunner("", sshCommand());
	//		sshCmd.setQuiet(true, true); // Do not copy command's output to stdout or sterr
	//		sshCmd.setSaveStd(true);
	//		sshCmd.start();
	//
	//		try {
	//			// Wait for command to start 
	//			while (!sshCmd.isStarted())
	//				Thread.sleep(host.getCluster().getDefaultWaitTime());
	//
	//			// Wait for stdin to became available
	//			while (sshCmd.getStdin() == null)
	//				Thread.sleep(host.getCluster().getDefaultWaitTime());
	//		} catch (Exception e) {
	//			error = e.toString();
	//			close();
	//		}
	//	}
	//
	//	/**
	//	 * Reset Stdout & Stderr buffers
	//	 */
	//	public void resetBuffers() {
	//		if (sshCmd != null) sshCmd.resetBuffers();
	//	}
	//
	//	/**
	//	 * Send a command and wait for the host to finish executing it.
	//	 * 
	//	 * Note: In order to know when the command finished executing we add another command (i.e. and "echo" of a random string). When we see this 
	//	 * random string (parsing ssh's stdout) we assume that the command finished executing. This is neither perfect nor elegant. I'll try to change 
	//	 * it sometime in the future.
	//	 * 
	//	 * @param cmd
	//	 * @param timeout
	//	 */
	//	public synchronized void send(String cmd, long timeout) {
	//		if (sshCmd == null) open();
	//
	//		// Create a command that also prints an 'alert' string (to indicate that the command finished execution)
	//		jobId = ("" + Math.random()).substring(2); // Create a random number and remove the '0.' part at the beginning (...mmm...this is not very elegant)
	//		String alertStr = "Cluster101 JOBID:" + jobId + "";
	//		String cmdNl = cmd + "\necho " + alertStr + "\n";
	//
	//		// Set alert string 
	//		sshCmd.setStdoutAlert(alertStr);
	//
	//		try {
	//			// Write command to ssh
	//			sshCmd.getStdin().write(cmdNl.getBytes());
	//			sshCmd.getStdin().flush();
	//
	//			// Wait for command to finish (or timeout)
	//			sshCmd.setStdoutAlertNotify(this); // Notify me when an 'alert string' is printed to stdout (i.e. the command finished)
	//			wait(timeout);
	//
	//		} catch (Exception e) {
	//			Gpr.debug("Exception while writing:\n---\n" + cmdNl + "---\n" + e);
	//			e.printStackTrace();
	//			close();
	//		}
	//	}
	//
	//	/**
	//	 * Create an 'ssh' command (String[])
	//	 * @param remoteCommand
	//	 * @return
	//	 */
	//	String[] sshCommand() {
	//		Cluster cluster = host.getCluster();
	//
	//		String cmd[] = { //
	//		cluster.getSshComand() //
	//				, "-o", "ConnectTimeout=" + cluster.getConnectTimeout() //
	//				, "-o", "StrictHostKeyChecking=no" //
	//				, "-o", "ServerAliveInterval=" + cluster.getSshKeepAlive() //
	//				, "-p", "" + host.getPort() //
	//				, host.getUserName() + "@" + host.getHostName() //
	//		};
	//
	//		return cmd;
	//	}
}
