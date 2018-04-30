package org.bds.test;

import org.bds.Bds;
import org.bds.Config;
import org.bds.run.BdsThread;
import org.bds.util.Gpr;
import org.junit.Test;

import junit.framework.Assert;

/**
 * Check command line options
 *
 * @author pcingola
 */
public class TestCasesCommandLineOptions extends TestCasesBase {

	@Test
	public void test01_log() {
		Gpr.debug("Test");
		Config.reset();

		// Create command line
		String args[] = { "-log" };
		BdsTest bdsTest = new BdsTest("test/cmdLineOptions_01.bds", args, verbose, debug);

		// Run script
		bdsTest.run();
		bdsTest.checkRunOk();

		// Get thread
		Bds bds = bdsTest.bds;
		BdsThread bdsThread = bds.getBdsRun().getBdsThread();

		// Check that all 'log' files exists
		String base = bdsThread.getBdsThreadId() + "/task.cmdLineOptions_01.line_3.id_1";

		if (verbose) Gpr.debug("Thread ID:" + bdsThread.getBdsThreadId() + "\tBase: " + base);
		String exts[] = { "sh", "exitCode", "stderr", "stdout" };
		for (String ext : exts) {
			String fileName = base + "." + ext;
			Assert.assertTrue("Log file '" + fileName + "' not found", Gpr.exists(fileName));
		}
	}
}
