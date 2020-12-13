package org.bds.test.integration;

import java.io.File;
import java.util.List;

import org.bds.Bds;
import org.bds.data.DataS3;
import org.bds.run.BdsThread;
import org.bds.run.BdsThreads;
import org.bds.test.TestCasesBase;
import org.bds.util.Gpr;
import org.junit.Assert;
import org.junit.Test;

public class TestCasesCheckpoint extends TestCasesBase {

	public static boolean debug = false;

	@Test
	public void test01() {
		Gpr.debug("Test");
		runAndCheckpoint("test/checkpoint_01.bds", null, "i", "10");
	}

	@Test
	public void test02() {
		Gpr.debug("Test");
		runAndCheckpoint("test/checkpoint_02.bds", null, "l", "15");
	}

	@Test
	public void test03() {
		Gpr.debug("Test");
		runAndCheckpoint("test/checkpoint_03.bds", null, "s2", "After checkpoint 42");
	}

	@Test
	public void test04() {
		Gpr.debug("Test");
		runAndCheckpoint("test/checkpoint_04.bds", null, "s", "one\teins");
	}

	@Test
	public void test04_2() {
		Gpr.debug("Test");
		runAndCheckpoint("test/checkpoint_04.bds", null, "i", "3");
	}

	@Test
	public void test04_3() {
		Gpr.debug("Test");
		runAndCheckpoint("test/checkpoint_04.bds", null, "ss", "one\ttwo");
	}

	@Test
	public void test05() {
		Gpr.debug("Test");
		runAndCheckpoint("test/checkpoint_05.bds", null, "l0", "ONE");
	}

	/**
	 * How this test works:
	 * 		1) Try to delete a file that doesn't exits. Task fails, checkpoint is created and program finishes
	 * 		2) createFile is run: This creates the file to be deleted
	 * 		3) Checkpoint recovery, the task is re-executed. This time the file exists, so it runs OK. Variable 'b' is set to true
	 */
	@Test
	public void test06() {
		Gpr.debug("Test");
		final String fileToDelete = "test/checkpoint_06.tmp";

		Runnable createFile = new Runnable() {

			@Override
			public void run() {
				// Create the file
				Gpr.debug("Creating file: '" + fileToDelete + "'");
				Gpr.toFile(fileToDelete, "Hello");
			}
		};

		// Make sure that the file doesn't exits
		(new File(fileToDelete)).delete();

		// Run test
		runAndCheckpoint("test/checkpoint_06.bds", "checkpoint_06.bds.line_8.chp", "b", "true", createFile);
	}

	@Test
	public void test07() {
		Gpr.debug("Test");
		runAndCheckpoint("test/checkpoint_07.bds", null, "sloop", "three");
	}

	@Test
	public void test08() {
		Gpr.debug("Test");
		// Remove old entries
		String prefix = "test/checkpoint_08";
		File txt = new File(prefix + ".txt");
		final File csv = new File(prefix + ".csv");
		final File xml = new File(prefix + ".xml");
		txt.delete();
		csv.delete();
		xml.delete();

		// Create file
		Gpr.toFile(prefix + ".txt", "TEST");

		// Run this code before checkpoint recovery
		Runnable runBeforeRecovery = new Runnable() {

			@Override
			public void run() {
				// Create the file
				Gpr.debug("Deleting files: " + csv + " and " + xml);
				csv.delete();
				xml.delete();
			}
		};

		// Run pipeline and test checkpoint
		runAndCheckpoint(prefix + ".bds", prefix + ".chp", "num", "2", runBeforeRecovery);
	}

	@Test
	public void test09() {
		Gpr.debug("Test");
		// Remove old entries
		String prefix = "test/checkpoint_09";
		File txt = new File(prefix + ".txt");
		final File csv = new File(prefix + ".csv");
		final File xml = new File(prefix + ".xml");
		txt.delete();
		csv.delete();
		xml.delete();

		// Create file
		Gpr.toFile(prefix + ".txt", "TEST");

		// Run this code before checkpoint recovery
		Runnable runBeforeRecovery = new Runnable() {

			@Override
			public void run() {
				// Create the file
				Gpr.debug("Deleting file: " + csv);
				csv.delete();
			}
		};

		// Run pipeline and test checkpoint
		runAndCheckpoint(prefix + ".bds", prefix + ".chp", "num", "0", runBeforeRecovery);
	}

	@Test
	public void test10() {
		Gpr.debug("Test");
		// Run pipeline and test checkpoint
		runAndCheckpoint("test/checkpoint_10.bds", "test/checkpoint_10.chp", "sumMain", "55");
	}

	@Test
	public void test11() {
		Gpr.debug("Test");
		// Run pipeline and test checkpoint
		runAndCheckpoint("test/checkpoint_11.bds", "test/checkpoint_11.chp", "sumPar", "110");
	}

	@Test
	public void test12_serializationOfEmptyIncludes() {
		Gpr.debug("Test");
		runAndCheckpoint("test/checkpoint_12.bds", "test/checkpoint_12.chp", "ok", "true");
	}

	@Test
	public void test13_checkPoint_function_with_empty_Args() {
		Gpr.debug("Test");
		runAndCheckpoint("test/checkpoint_13.bds", "test/checkpoint_13.chp", "ok", "true");
	}

	@Test
	public void test14_serialize_method_call_args() {
		Gpr.debug("Test");
		runAndCheckpoint("test/checkpoint_14.bds", "test/checkpoint_14.chp", "ok", "true");
	}

	@Test
	public void test15_checkpoint_par_function_call() {
		Gpr.debug("Test");
		runAndCheckpoint("test/checkpoint_15.bds", "test/checkpoint_15.chp", "ok", "true");
	}

	@Test
	public void test16_checkpoint_recursive() {
		Gpr.debug("Test");
		runAndCheckpoint("test/checkpoint_16.bds", "test/checkpoint_16.chp", "fn", "120");
	}

	@Test
	public void test17_checkpoint_listIndex() {
		Gpr.debug("Test");
		runAndCheckpoint("test/checkpoint_17.bds", "test/checkpoint_17.chp", "res", "19");
	}

	@Test
	public void test18_checkpoint_listIndex() {
		Gpr.debug("Test");
		runAndCheckpoint("test/checkpoint_18.bds", "test/checkpoint_18.chp", "res", "34");
	}

	@Test
	public void test19_scope_global_global() {
		Gpr.debug("Test");
		// Run pipeline and test checkpoint
		Bds bds = runAndCheckpoint("test/checkpoint_19.bds", "test/checkpoint_19.chp", "ok", "true");

		// Get scope names
		BdsThread bdsThread = bds.getBdsRun().getBdsThread();
		String scopeNames = bdsThread.getScope().toStringScopeNames();

		// Count number of global scopes
		int count = 0;
		for (String line : scopeNames.split("\n")) {
			if (line.contains("Global")) count++;
		}

		Assert.assertTrue("There should be one and only one 'Global' scope (count = " + count + ")", count == 1);
	}

	@Test
	public void test20_checkpoint_after_main_thread_finished_execution() {
		Gpr.debug("Test");
		runAndCheckpoint("test/checkpoint_20.bds", "test/checkpoint_20.chp", null, null);
	}

	@Test
	public void test21_checkpoint_after_par_thread_finished_execution() {
		Gpr.debug("Test");
		runAndCheckpoint("test/checkpoint_21.bds", "test/checkpoint_21.chp", null, null);
	}

	public void test22_par_par_par() {
		Gpr.debug("Test");
		runAndCheckpoint("test/checkpoint_22.bds", "test/checkpoint_22.chp", "luae", "42");
	}

	@Test
	public void test23_thread_structure() {
		Gpr.debug("Test");

		try {
			// Run pipeline and test checkpoint
			BdsThreads.doNotRemoveThreads = true;
			Bds bds = runAndCheckpoint("test/checkpoint_23.bds", "test/checkpoint_23.chp", "luae", "42");

			// Get scope names
			BdsThread bdsThread = bds.getBdsRun().getBdsThread();

			// All threads (including root thread)
			Assert.assertEquals(4, bdsThread.getBdsThreadsAll().size());

			// First 'level'
			List<BdsThread> bdsThreadsL1 = bdsThread.getBdsThreads();
			if (verbose) Gpr.debug("Root thread '" + bdsThread.getBdsThreadId() + "', number of child threads: " + bdsThreadsL1.size());
			Assert.assertEquals(1, bdsThreadsL1.size());

			// Second 'level'
			for (BdsThread bdsthl1 : bdsThreadsL1) {
				List<BdsThread> bdsThreadsL2 = bdsthl1.getBdsThreads();
				if (verbose) Gpr.debug("Level 1 thread '" + bdsthl1.getBdsThreadId() + "', number of child threads: " + bdsThreadsL2.size());
				Assert.assertEquals(2, bdsThreadsL2.size());

				// Third 'level'
				for (BdsThread bdsthl2 : bdsThreadsL2) {
					List<BdsThread> bdsThreadsL3 = bdsthl2.getBdsThreads();
					if (verbose) Gpr.debug("Level 2 thread '" + bdsthl2.getBdsThreadId() + "', number of child threads: " + bdsThreadsL3.size());
					Assert.assertEquals(0, bdsThreadsL3.size());
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
			throw new RuntimeException(t);
		} finally {
			BdsThreads.doNotRemoveThreads = false;
		}
	}

	@Test
	public void test24_switch() {
		Gpr.debug("Test");
		runAndCheckpoint("test/checkpoint_24.bds", "test/checkpoint_24.chp", "out", 103);
	}

	@Test
	public void test25_switch() {
		Gpr.debug("Test");
		runAndCheckpoint("test/checkpoint_25.bds", "test/checkpoint_25.chp", "out", 35);
	}

	@Test
	public void test26_switch() {
		Gpr.debug("Test");
		runAndCheckpoint("test/checkpoint_26.bds", "test/checkpoint_26.chp", "out", 56);
	}

	@Test
	public void test28() {
		Gpr.debug("Test");
		runAndCheckpoint("test/checkpoint_28.bds", "test/checkpoint_28.chp", "out", 47);
	}

	// Create a remote checkpoint
	@Test
	public void test29() {
		Gpr.debug("Test");
		runAndCheck("test/checkpoint_29.bds", "ok", true);
	}

	// Save and load remote checkpoint (S3)
	@Test
	public void test30() {
		Gpr.debug("Test");
		String bucket = awsBucketName();
		String region = awsRegion();
		String checkpointFile = "https://" + bucket + ".s3." + region + "." + DataS3.AWS_S3_VIRTUAL_HOSTED_DOMAIN + "/tmp/bds/checkpoint_30.chp";
		runAndCheckpoint("test/checkpoint_30.bds", checkpointFile, "sum", 285, null);
	}

}
