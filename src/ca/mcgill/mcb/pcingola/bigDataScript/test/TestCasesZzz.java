package ca.mcgill.mcb.pcingola.bigDataScript.test;

import java.util.List;

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
	public void test23_thread_structure() {
		// Run pipeline and test checkpoint
		BigDataScript bds = runAndCheckpoint("test/checkpoint_23.bds", "test/checkpoint_23.chp", "luae", "42");

		// Get scope names
		BigDataScriptThread bdsThread = bds.getBigDataScriptThread();

		// All threads (including root thread)
		Assert.assertEquals(4, bdsThread.getBdsThreadsAll().size());

		// First 'level'
		List<BigDataScriptThread> bdsThreadsL1 = bdsThread.getBdsThreads();
		if (verbose) Gpr.debug("Root thread '" + bdsThread.getBdsThreadId() + "', number of child threads: " + bdsThreadsL1.size());
		Assert.assertEquals(1, bdsThreadsL1.size());

		// Second 'level'
		for (BigDataScriptThread bdsthl1 : bdsThreadsL1) {
			List<BigDataScriptThread> bdsThreadsL2 = bdsthl1.getBdsThreads();
			if (verbose) Gpr.debug("Level 1 thread '" + bdsthl1.getBdsThreadId() + "', number of child threads: " + bdsThreadsL2.size());
			Assert.assertEquals(2, bdsThreadsL2.size());

			// Third 'level'
			for (BigDataScriptThread bdsthl2 : bdsThreadsL2) {
				List<BigDataScriptThread> bdsThreadsL3 = bdsthl2.getBdsThreads();
				if (verbose) Gpr.debug("Level 2 thread '" + bdsthl2.getBdsThreadId() + "', number of child threads: " + bdsThreadsL3.size());
				Assert.assertEquals(0, bdsThreadsL3.size());
			}
		}
	}
}
