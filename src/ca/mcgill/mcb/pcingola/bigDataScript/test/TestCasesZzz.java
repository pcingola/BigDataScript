package ca.mcgill.mcb.pcingola.bigDataScript.test;

import org.junit.Test;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

	@Test
	public void test11() {
		// Run pipeline and test checkpoint
		runAndCheckpoint("test/checkpoint_11.bds", "test/checkpoint_11.chp", "sumPar", "110");
	}

	//	@Test
	//	public void test_thread_structure() {
	//		verbose = true;
	//
	//		// Run pipeline and test checkpoint
	//		BigDataScript bds = runAndCheckpoint("test/z.bds", "test/z.chp", null, null);
	//
	//		// Get scope names
	//		BigDataScriptThread bdsThread = bds.getBigDataScriptThread();
	//		//		String scopeNames = bdsThread.getScope().toStringScopeNames();
	//		//
	//		//		// Count number of global scopes
	//		//		int count = 0;
	//		//		for (String line : scopeNames.split("\n")) {
	//		//			if (line.contains("Global")) count++;
	//		//		}
	//		//
	//		//		Assert.assertTrue("There should be one and only one 'Global' scope (count = " + count + ")", count == 1);
	//	}

}
