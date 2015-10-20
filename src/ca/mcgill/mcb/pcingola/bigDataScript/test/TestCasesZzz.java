package ca.mcgill.mcb.pcingola.bigDataScript.test;

import org.junit.Test;

import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;
import junit.framework.Assert;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

	@Test
	public void test_144_dollar_sign_in_task() {
		Gpr.debug("Test");

		// We want to execute an inline perl script within a task
		// E.g.:
		//     task perl -e 'use English; print "PID: \$PID\n";'
		// 
		// Here $PID is a perl variable and should not be interpreted 
		// by bds. We need a way to escape such variables.
		String bdsFile = "test/run_144.bds";

		String stdout = runAndReturnStdout(bdsFile);

		// Parse STDOUT
		String lines[] = stdout.split("\n");
		Assert.assertEquals("No lines found?", 3, lines.length);

		for (String line : lines) {
			String fields[] = line.split(":");
			Assert.assertEquals("Cannot parse line:\n" + line, 2, fields.length);

			int positiveNumber = Gpr.parseIntSafe(fields[1]);
			Assert.assertTrue("Positive number expected: '" + fields[1] + "'", positiveNumber > 0);
		}
	}

}
