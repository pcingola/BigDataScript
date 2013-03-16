package ca.mcgill.mcb.pcingola.bigDataScript.exec.local;

import java.io.File;
import java.util.ArrayList;

import ca.mcgill.mcb.pcingola.bigDataScript.exec.Executioners.ExecutionerType;
import ca.mcgill.mcb.pcingola.bigDataScript.exec.Task;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.CmdRunner;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * A shell script task
 * 
 * @author pcingola
 */
public class ShellTask extends Task {

	public static final String SHELL_COMMAND = "/bin/sh";
	public static final String SHELL_COMMAND_OPTIONS[] = { "-e" };

	protected String shellCmd = SHELL_COMMAND;
	protected String shellCmdOptions[] = SHELL_COMMAND_OPTIONS;
	protected String invokeCmd = null;
	protected String invokeCmdOptions[] = {};
	protected boolean debug;
	protected CmdRunner cmdRunner;

	public ShellTask() {
		super();
	}

	public ShellTask(String id, String programFileName, String programTxt) {
		super(id, programFileName, programTxt);

		// Some defaults
		stdoutFile = Gpr.removeExt(programFileName) + ".stdout";
		stderrFile = Gpr.removeExt(programFileName) + ".stderr";
	}

	/**
	 * Create a CmdRunner
	 * @return
	 */
	@Override
	public CmdRunner cmdRunner() {
		// Save program to file
		save(programFileName);

		// Create command 
		if (verbose) Timer.showStdErr("Creating shell script '" + programFileName + "'");
		ArrayList<String> osCmdList = new ArrayList<String>();

		if (invokeCmd != null) {
			osCmdList.add(invokeCmd);
			for (String invokeCmdOption : getInvokeCmdOptions())
				osCmdList.add(invokeCmdOption);
		}

		osCmdList.add(programFileName);

		// Create command
		String osCmdStr[] = osCmdList.toArray(new String[0]);
		cmdRunner = new CmdRunner(id, osCmdStr);

		// We want to save STDOUT & STDERR
		cmdRunner.setRedirectStdout(stdoutFile);
		cmdRunner.setRedirectStderr(stderrFile);
		cmdRunner.setResources(resources);
		cmdRunner.setCmdStats(this); // Update exitValue to this object when done

		return cmdRunner;
	}

	@Override
	public ExecutionerType getExecutionerType() {
		return ExecutionerType.LOCAL;
	}

	public String getInvokeCmd() {
		return invokeCmd;
	}

	public String[] getInvokeCmdOptions() {
		return invokeCmdOptions;
	}

	public String getShellCmd() {
		return shellCmd;
	}

	public String[] getShellCmdOptions() {
		return shellCmdOptions;
	}

	public boolean kill() {
		if (cmdRunner != null) cmdRunner.kill();
		return false;
	}

	public boolean run() {
		// Create command
		cmdRunner = cmdRunner();

		// Run command
		cmdRunner.start();

		// Wait until thread (command) finishes
		try {
			cmdRunner.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Collect exit data
		exitValue = cmdRunner.getExitValue();
		boolean ok = exitValue <= 0;

		// Finished OK
		return ok;
	}

	/**
	 * Save a file with the script
	 */
	protected void save(String shellFileName) {
		// Create script
		StringBuilder prg = new StringBuilder();

		// She-bang line
		prg.append("#!" + shellCmd + " ");
		for (String shellCmdOption : shellCmdOptions)
			prg.append(shellCmdOption + " ");
		prg.append("\n\n");

		// Program
		prg.append(programTxt);
		prg.append("\n");

		// Save file
		Gpr.toFile(shellFileName, prg);

		// Make files executable
		File file = new File(shellFileName);
		file.setExecutable(true, false);
	}

	public void setInvokeCmd(String invokeCmd) {
		this.invokeCmd = invokeCmd;
	}

	public void setInvokeCmdOptions(String[] invokeCmdOptions) {
		this.invokeCmdOptions = invokeCmdOptions;
	}

	public void setShellCmd(String shellCmd) {
		this.shellCmd = shellCmd;
	}

	public void setShellCmdOptions(String[] shellCmdOptions) {
		this.shellCmdOptions = shellCmdOptions;
	}

}
