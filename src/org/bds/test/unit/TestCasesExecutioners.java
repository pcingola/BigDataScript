package org.bds.test.unit;

import java.util.Set;

import org.bds.Config;
import org.bds.executioner.CheckTasksRunningCmd;
import org.bds.executioner.Executioner;
import org.bds.executioner.Executioners;
import org.bds.executioner.Executioners.ExecutionerType;
import org.bds.test.TestCasesBase;
import org.bds.util.Gpr;
import org.junit.Test;

import junit.framework.Assert;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesExecutioners extends TestCasesBase {

	@Test
	public void test01_parsePidQstatRegex() {
		Gpr.debug("Test");

		// Create 'CheckTasksRunning'
		Config config = new Config("test/test_parsePidQstatRegex_qstat.config"); // We set here the 'PID_REGEX_CHECK_TASK_RUNNING' parameter
		config.setDebug(debug);
		config.setVerbose(verbose);
		config.load();

		Executioner ex = Executioners.getInstance(config).get(ExecutionerType.LOCAL);
		CheckTasksRunningCmd ctr = new CheckTasksRunningCmd(config, ex);

		// Parse 'qstat' lines
		String fileName = "test/test_parsePidQstatRegex_qstat.txt";
		if (verbose) System.out.println("Reading file '" + fileName + "'");
		String file = Gpr.readFile(fileName);
		String lines[] = file.split("\n");
		Set<String> pids = ctr.parseCommandOutput(lines);

		// Check that all IDs are there
		for (int i = 1; i < 10; i++) {
			String pid = "jobId_10" + i;
			if (verbose) System.out.println("PID: '" + pid + "'\t" + pids.contains(pid));
			Assert.assertTrue("PID not found: '" + pid + "'", pids.contains(pid));
		}

		// Finished
		if (verbose) System.out.println("Killing executioners");
		ex.kill();
		if (verbose) System.out.println("Done");
	}

	@Test
	public void test02_parsePidQstatColumn() {
		Gpr.debug("Test");

		// Create 'CheckTasksRunning'
		Config config = new Config();
		config.setDebug(debug);
		config.setVerbose(verbose);
		config.load();

		Executioner ex = Executioners.getInstance(config).get(ExecutionerType.LOCAL);
		CheckTasksRunningCmd ctr = new CheckTasksRunningCmd(config, ex);

		// Parse 'qstat' lines
		String fileName = "test/test_parsePidQstatColumn_qstat.txt";
		if (verbose) System.out.println("Reading file '" + fileName + "'");
		String file = Gpr.readFile(fileName);
		String lines[] = file.split("\n");
		Set<String> pids = ctr.parseCommandOutput(lines);

		// Check that all IDs are there
		for (int i = 1; i < 10; i++) {
			String pid = "" + i;
			if (verbose) System.out.println("PID: '" + pid + "'\t" + pids.contains(pid));
			Assert.assertTrue("PID not found: '" + pid + "'", pids.contains(pid));
		}

		// Finished
		if (verbose) System.out.println("Killing executioners");
		ex.kill();
		if (verbose) System.out.println("Done");
	}

}
