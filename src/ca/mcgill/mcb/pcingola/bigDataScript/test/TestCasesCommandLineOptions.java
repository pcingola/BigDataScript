package ca.mcgill.mcb.pcingola.bigDataScript.test;

import junit.framework.Assert;

import org.junit.Test;

import ca.mcgill.mcb.pcingola.bigDataScript.BigDataScript;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

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
		String args[] = { "-log", "test/cmdLineOptions_01.bds" };
		BigDataScript bds = bds(args);

		// Run script
		bds.run();
		BigDataScriptThread bdsThread = bds.getBigDataScriptThread();

		// Check that all 'log' files exists
		String base = bdsThread.getBdsThreadId() + "/task.line_3.id_1";

		if (verbose) Gpr.debug("Thread ID:" + bdsThread.getBdsThreadId() + "\tBase: " + base);
		String exts[] = { "sh", "exitCode", "stderr", "stdout" };
		for (String ext : exts) {
			String fileName = base + "." + ext;
			Assert.assertTrue("Log file '" + fileName + "' not found", Gpr.exists(fileName));
		}
	}
}
