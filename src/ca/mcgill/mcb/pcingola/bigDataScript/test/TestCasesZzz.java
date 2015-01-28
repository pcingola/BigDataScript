package ca.mcgill.mcb.pcingola.bigDataScript.test;

import junit.framework.Assert;

import org.junit.Test;

import ca.mcgill.mcb.pcingola.bigDataScript.BigDataScript;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

	@Test
	public void test01() {
		Gpr.debug("Test");
		runAndCheck("test/run_01.bds", "i", 2L);
	}

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

	@Test
	public void test124_quiet_mode() {
		Gpr.debug("Test");
		String output = "print 0\n" //
				+ "print 1\n" //
				+ "print 2\n" //
				+ "print 3\n" //
				+ "print 4\n" //
				+ "print 5\n" //
				+ "print 6\n" //
				+ "print 7\n" //
				+ "print 8\n" //
				+ "print 9\n" //
		;

		// Run and capture stdout
		String args[] = { "-quiet", "test/run_124.bds" };
		String stdout = runAndReturnStdout(args);
		if (verbose) System.err.println("STDOUT: " + stdout);

		// Check that sys and task outputs are not there
		Assert.assertTrue("Print output should be in STDOUT", stdout.contains(output));
		Assert.assertTrue("Task output should NOT be in STDOUT", !stdout.contains("task"));
		Assert.assertTrue("Sys output should NOT be in STDOUT", !stdout.contains("sys"));
	}

}
