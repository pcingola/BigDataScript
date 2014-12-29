package ca.mcgill.mcb.pcingola.bigDataScript.test;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import ca.mcgill.mcb.pcingola.bigDataScript.BigDataScript;
import ca.mcgill.mcb.pcingola.bigDataScript.run.BigDataScriptThread;
import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

public class TestCasesCheckpoint extends TestCasesBase {

	public static boolean debug = false;

	@Test
	public void test01() {
		runAndCheckpoint("test/checkpoint_01.bds", null, "i", "10");
	}

	@Test
	public void test02() {
		runAndCheckpoint("test/checkpoint_02.bds", null, "l", "15");
	}

	@Test
	public void test03() {
		runAndCheckpoint("test/checkpoint_03.bds", null, "s2", "After checkpoint 42");
	}

	@Test
	public void test04() {
		runAndCheckpoint("test/checkpoint_04.bds", null, "s", "one\teins");
	}

	@Test
	public void test04_2() {
		runAndCheckpoint("test/checkpoint_04.bds", null, "i", "3");
	}

	@Test
	public void test04_3() {
		runAndCheckpoint("test/checkpoint_04.bds", null, "ss", "one\ttwo");
	}

	@Test
	public void test05() {
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
		runAndCheckpoint("test/checkpoint_06.bds", "test/checkpoint_06.bds.line_8.chp", "b", "true", createFile);
	}

	@Test
	public void test07() {
		runAndCheckpoint("test/checkpoint_07.bds", null, "sloop", "three");
	}

	@Test
	public void test08() {
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
		// Run pipeline and test checkpoint
		runAndCheckpoint("test/checkpoint_10.bds", "test/checkpoint_10.chp", "sumMain", "55");
	}

	@Test
	public void test11() {
		// Run pipeline and test checkpoint
		runAndCheckpoint("test/checkpoint_11.bds", "test/checkpoint_11.chp", "sumPar", "110");
	}

	@Test
	public void test12_serializationOfEmptyIncludes() {
		runAndCheckpoint("test/checkpoint_12.bds", "test/checkpoint_12.chp", "ok", "true");
	}

	@Test
	public void test13_checkPoint_function_with_empty_Args() {
		runAndCheckpoint("test/checkpoint_13.bds", "test/checkpoint_13.chp", "ok", "true");
	}

	@Test
	public void test14_serialize_method_call_args() {
		runAndCheckpoint("test/checkpoint_14.bds", "test/checkpoint_14.chp", "ok", "true");
	}

	@Test
	public void test15_checkpoint_par_function_call() {
		runAndCheckpoint("test/checkpoint_15.bds", "test/checkpoint_15.chp", "ok", "true");
	}

	@Test
	public void test16_checkpoint_recursive() {
		runAndCheckpoint("test/checkpoint_16.bds", "test/checkpoint_16.chp", "fn", "120");
	}

	@Test
	public void test17_checkpoint_listIndex() {
		runAndCheckpoint("test/checkpoint_17.bds", "test/checkpoint_17.chp", "res", "19");
	}

	@Test
	public void test18_checkpoint_listIndex() {
		runAndCheckpoint("test/checkpoint_18.bds", "test/checkpoint_18.chp", "res", "34");
	}

	@Test
	public void test19_scope_global_global() {
		// Run pipeline and test checkpoint
		BigDataScript bds = runAndCheckpoint("test/checkpoint_19.bds", "test/checkpoint_19.chp", "ok", "true");

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

	@Test
	public void test20_checkpoint_after_main_thread_finished_execution() {
		runAndCheckpoint("test/checkpoint_20.bds", "test/checkpoint_20.chp", null, null);
	}

	@Test
	public void test21_checkpoint_after_par_thread_finished_execution() {
		runAndCheckpoint("test/checkpoint_21.bds", "test/checkpoint_21.chp", null, null);
	}

	public void test22_par_par_par() {
		runAndCheckpoint("test/checkpoint_22.bds", "test/checkpoint_22.chp", "luae", "42");
	}

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
