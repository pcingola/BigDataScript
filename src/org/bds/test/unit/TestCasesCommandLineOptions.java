package org.bds.test.unit;

import java.io.File;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.bds.Bds;
import org.bds.Config;
import org.bds.run.BdsRun;
import org.bds.run.BdsThread;
import org.bds.test.BdsTest;
import org.bds.test.TestCasesBase;
import org.bds.util.Gpr;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Check command line options
 *
 * @author pcingola
 */
public class TestCasesCommandLineOptions extends TestCasesBase {

	@Before
	public void beforeEachTest() {
		BdsRun.reset();
		//		Config.reset();
		Config.get().load();
	}

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
		String dir = bdsThread.getBdsThreadId();
		String prefix = "task.cmdLineOptions_01.line_3.";

		// Get dir
		Set<String> files = Arrays.stream((new File(dir)).list()) //
				.filter(f -> f.startsWith(prefix)) //
				.collect(Collectors.toSet()) //
		;

		String exts[] = { "sh", "exitCode", "stderr", "stdout" };
		for (String ext : exts) {
			boolean ok = files.stream().anyMatch(f -> f.endsWith("." + ext));
			Assert.assertTrue("Log file starting with '" + prefix + "' and ending with '" + ext + "' not found in dir '" + dir + "'", ok);
		}
	}
}
