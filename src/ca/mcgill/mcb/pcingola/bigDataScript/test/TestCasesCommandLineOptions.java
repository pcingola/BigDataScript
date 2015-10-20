package ca.mcgill.mcb.pcingola.bigDataScript.test;

import org.junit.Test;

import ca.mcgill.mcb.pcingola.bigDataScript.Bds;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BdsThread;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
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

		// Create command line
		String args[] = { "-log" };
		BdsTest bdsTest = new BdsTest("test/cmdLineOptions_01.bds", args, verbose, debug);

		// Run script
		bdsTest.run();
		bdsTest.checkRunOk();

		// Get thread
		Bds bds = bdsTest.bds;
		BdsThread bdsThread = bds.getBigDataScriptThread();

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
