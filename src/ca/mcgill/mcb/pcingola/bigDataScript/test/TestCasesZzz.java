package ca.mcgill.mcb.pcingola.bigDataScript.test;

import org.junit.Assert;
import org.junit.Test;

import ca.mcgill.mcb.pcingola.bigDataScript.BigDataScript;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

	@Test
	public void test_scope_global_global() {
		// Run pipeline and test checkpoint
		BigDataScript bds = runAndCheckpoint("test/z.bds", "test/z.chp", "ok", "true");

		// Get scope names
		BigDataScriptThread bdsThread = bds.getBigDataScriptThread();
		String scopeNames = bdsThread.getScope().toStringScopeNames();

		// Count number of global scopes
		int count = 0;
		for (String line : scopeNames.split("\n")) {
			if (line.contains("Global")) count++;
		}

		Assert.assertTrue("There should be one and only one 'Global' scope (count = " + count + ")", count == 1);
	}

}
