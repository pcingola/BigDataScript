package org.bds.osCmd;

/**
 * Output form a process execution
 * 
 * @author pcingola
 */
public class ExecResult {

	public String stdOut;
	public String stdErr;
	public int exitValue;

	public ExecResult(String stdOut, String stdErr, int exitValue) {
		this.stdOut = stdOut;
		this.stdErr = stdErr;
		this.exitValue = exitValue;
	}
}
