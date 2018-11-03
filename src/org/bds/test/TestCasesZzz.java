package org.bds.test;

import java.util.HashMap;
import java.util.Map;

import org.bds.Config;
import org.bds.util.Gpr;
import org.junit.Before;
import org.junit.Test;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

	@Before
	public void beforeEachTest() {
		Config.reset();
		Config.get().load();
	}

	//	/**
	//	 * How this test works:
	//	 * 		1) Try to delete a file that doesn't exits. Task fails, checkpoint is created and program finishes
	//	 * 		2) createFile is run: This creates the file to be deleted
	//	 * 		3) Checkpoint recovery, the task is re-executed. This time the file exists, so it runs OK. Variable 'b' is set to true
	//	 */
	//	@Test
	//	public void test06() {
	//		Gpr.debug("Test");
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
	//		runAndCheckpoint("test/checkpoint_06.bds", "checkpoint_06.bds.line_8.chp", "b", "true", createFile);
	//	}
	//
	@Test
	public void test225_super() {
		Gpr.debug("Test");
		Map<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("af", "1");
		expectedValues.put("ag", "2");
		expectedValues.put("ax", "41");
		expectedValues.put("bf", "11");
		expectedValues.put("bf", "12");
		expectedValues.put("bx", "42");

		runAndCheck("test/run_225.bds", expectedValues);
	}
	//
	//	@Test
	//	public void test113_parallel_function_calls() {
	//		Gpr.debug("Test");
	//		String stdout = runAndReturnStdout("test/run_113.bds");
	//
	//		Set<String> linesPar = new HashSet<>();
	//		for (String line : stdout.split("\n")) {
	//			if (line.startsWith("Par:")) {
	//				if (linesPar.contains(line)) throw new RuntimeException("Line repeated (this should never happen): '" + line + "'");
	//				linesPar.add(line);
	//			}
	//		}
	//	}

}
