package ca.mcgill.mcb.pcingola.bigDataScript.exec.ssh;

import java.util.ArrayList;

import ca.mcgill.mcb.pcingola.bigDataScript.cluster.host.Host;
import ca.mcgill.mcb.pcingola.bigDataScript.exec.Executioners.ExecutionerType;
import ca.mcgill.mcb.pcingola.bigDataScript.exec.local.ShellTask;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.CmdRunner;
import ca.mcgill.mcb.pcingola.bigDataScript.osCmd.CmdRunnerSsh;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * A shell script task to be executed via ssh
 * 
 * @author pcingola
 */
public class SshTask extends ShellTask {

	Host host;

	public SshTask(String id, String programFileName, String programTxt) {
		super(id, programFileName, programTxt);
		debug = true;
		invokeCmd = SHELL_COMMAND;
		invokeCmdOptions = SHELL_COMMAND_OPTIONS;
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
		Timer.showStdErr("Creating shell script (local): '" + programFileName + "'");
		ArrayList<String> osCmdList = new ArrayList<String>();

		if (invokeCmd != null) {
			osCmdList.add(invokeCmd);
			for (String invokeCmdOption : invokeCmdOptions)
				osCmdList.add(invokeCmdOption);
		}

		osCmdList.add(Gpr.baseName(programFileName));

		// Create command
		String osCmdStr[] = osCmdList.toArray(new String[0]);
		cmdRunner = new CmdRunnerSsh(id, programFileName, osCmdStr);

		// We want to save STDOUT & STDERR
		stdoutFile = Gpr.removeExt(programFileName) + ".stdout";
		stderrFile = Gpr.removeExt(programFileName) + ".stderr";
		cmdRunner.setRedirectStdout(stdoutFile);
		cmdRunner.setRedirectStderr(stderrFile);
		cmdRunner.setResources(resources);

		return cmdRunner;
	}

	@Override
	public ExecutionerType getExecutionerType() {
		return ExecutionerType.SSH;
	}

	public Host getHost() {
		return host;
	}

	@Override
	public boolean kill() {
		if (cmdRunner != null) cmdRunner.kill();
		return false;
	}

	@Override
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
		Gpr.debug("EXIT VALUE: " + exitValue);
		boolean ok = exitValue <= 0;

		// Finished OK
		return ok;
	}

	public void setHost(Host host) {
		this.host = host;
	}

}
