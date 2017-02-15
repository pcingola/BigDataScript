package org.bds.test;

import org.bds.util.Gpr;
import org.junit.Test;

import junit.framework.Assert;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

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
		String args[] = { "-quiet" };
		String stdout = runAndReturnStdout("test/run_124.bds", args);
		if (verbose) System.err.println("STDOUT: " + stdout);

		// Check that sys and task outputs are not there
		Assert.assertTrue("Print output should be in STDOUT", stdout.contains(output));
		Assert.assertTrue("Task output should NOT be in STDOUT", !stdout.contains("task"));
		Assert.assertTrue("Sys output should NOT be in STDOUT", !stdout.contains("sys"));
	}

}
