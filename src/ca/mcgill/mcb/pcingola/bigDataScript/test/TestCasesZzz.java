package ca.mcgill.mcb.pcingola.bigDataScript.test;

import junit.framework.Assert;

import org.junit.Test;

import ca.mcgill.mcb.pcingola.bigDataScript.util.Timer;

/**
 * Quick test cases when creating a new feature...
 * 
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

	@Test
	public void test31() {
		Timer timer = new Timer();
		timer.start();
		runAndCheck("test/run_31.bds", "events", "[runnning, kill, done]");
		Assert.assertTrue(timer.elapsed() < 1 * 1000); // We should finish in much less than 1 secs (the program waits 60secs)
	}

	//	/**
	//	 * How this test works:
	//	 * 		1) Try to delete a file that doesn't exits. Task fails, checkpoint is created and program finishes
	//	 * 		2) createFile is run: This creates the file to be deleted
	//	 * 		3) Checkpoint recovery, the task is re-executed. This time the file exists, so it runs OK. Variable 'b' is set to true
	//	 */
	//	@Test
	//	public void test06() {
	//		final String fileToDelete = "test/checkpoint_06.tmp";
	//
	//		Runnable createFile = new Runnable() {
	//
	//			@Override
	//			public void run() {
	//				// Create the file
	//				Gpr.debug("Creating file: '" + fileToDelete + "'");
	//				Gpr.toFile(fileToDelete, "Hello");
	//			}
	//		};
	//
	//		// Make sure that the file doesn't exits
	//		(new File(fileToDelete)).delete();
	//
	//		// Run test
	//		runAndCheckpoint("test/checkpoint_06.bds", "test/checkpoint_06.bds.line_8.chp", "b", "true", createFile);
	//	}

}
