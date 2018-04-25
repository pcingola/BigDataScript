package org.bds.test;

import org.bds.util.Gpr;
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
		Gpr.debug("Test");
		verbose = true;
		// Run pipeline and test checkpoint
		runAndCheckpoint("test/checkpoint_11.bds", "test/checkpoint_11.chp", "sumPar", "110");
	}

	//	@Test
	//	public void test15_checkpoint_par_function_call() {
	//		Gpr.debug("Test");
	//		runAndCheckpoint("test/checkpoint_15.bds", "test/checkpoint_15.chp", "ok", "true");
	//	}
	//
	//	@Test
	//	public void test23_thread_structure() {
	//		Gpr.debug("Test");
	//		// Run pipeline and test checkpoint
	//		Bds bds = runAndCheckpoint("test/checkpoint_23.bds", "test/checkpoint_23.chp", "luae", "42");
	//
	//		// Get scope names
	//		BdsThread bdsThread = bds.getBdsRun().getBdsThread();
	//
	//		// All threads (including root thread)
	//		Assert.assertEquals(4, bdsThread.getBdsThreadsAll().size());
	//
	//		// First 'level'
	//		List<BdsThread> bdsThreadsL1 = bdsThread.getBdsThreads();
	//		if (verbose) Gpr.debug("Root thread '" + bdsThread.getBdsThreadId() + "', number of child threads: " + bdsThreadsL1.size());
	//		Assert.assertEquals(1, bdsThreadsL1.size());
	//
	//		// Second 'level'
	//		for (BdsThread bdsthl1 : bdsThreadsL1) {
	//			List<BdsThread> bdsThreadsL2 = bdsthl1.getBdsThreads();
	//			if (verbose) Gpr.debug("Level 1 thread '" + bdsthl1.getBdsThreadId() + "', number of child threads: " + bdsThreadsL2.size());
	//			Assert.assertEquals(2, bdsThreadsL2.size());
	//
	//			// Third 'level'
	//			for (BdsThread bdsthl2 : bdsThreadsL2) {
	//				List<BdsThread> bdsThreadsL3 = bdsthl2.getBdsThreads();
	//				if (verbose) Gpr.debug("Level 2 thread '" + bdsthl2.getBdsThreadId() + "', number of child threads: " + bdsThreadsL3.size());
	//				Assert.assertEquals(0, bdsThreadsL3.size());
	//			}
	//		}
	//	}

}
