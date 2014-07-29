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
	public void test03() {
		runAndCheckpoint("test/z.bds", "test/z.chp", "out", "Task start\nTask end\n");
	}

	//	@Test
	//	public void test04() {
	//		runAndCheckpoint("test/graph_04.bds", "test/graph_04.chp", "out", "IN\nTASK 1\nTASK 2\n");
	//	}
	//	@Test
	//	public void test07() {
	//		runAndCheckpoint("test/checkpoint_07.bds", null, "sloop", "three");
	//	}
	//
	//	@Test
	//	public void test08() {
	//		// Remove old entries
	//		String prefix = "test/checkpoint_08";
	//		File txt = new File(prefix + ".txt");
	//		final File csv = new File(prefix + ".csv");
	//		final File xml = new File(prefix + ".xml");
	//		txt.delete();
	//		csv.delete();
	//		xml.delete();
	//
	//		// Create file
	//		Gpr.toFile(prefix + ".txt", "TEST");
	//
	//		// Run this code before checkpoint recovery
	//		Runnable runBeforeRecovery = new Runnable() {
	//
	//			@Override
	//			public void run() {
	//				// Create the file
	//				Gpr.debug("Deleting files: " + csv + " and " + xml);
	//				csv.delete();
	//				xml.delete();
	//			}
	//		};
	//
	//		// Run pipeline and test checkpoint
	//		runAndCheckpoint(prefix + ".bds", prefix + ".chp", "num", "2", runBeforeRecovery);
	//	}
	//
	//	@Test
	//	public void test09() {
	//		// Remove old entries
	//		String prefix = "test/checkpoint_09";
	//		File txt = new File(prefix + ".txt");
	//		final File csv = new File(prefix + ".csv");
	//		final File xml = new File(prefix + ".xml");
	//		txt.delete();
	//		csv.delete();
	//		xml.delete();
	//
	//		// Create file
	//		Gpr.toFile(prefix + ".txt", "TEST");
	//
	//		// Run this code before checkpoint recovery
	//		Runnable runBeforeRecovery = new Runnable() {
	//
	//			@Override
	//			public void run() {
	//				// Create the file
	//				Gpr.debug("Deleting file: " + csv);
	//				csv.delete();
	//			}
	//		};
	//
	//		// Run pipeline and test checkpoint
	//		runAndCheckpoint(prefix + ".bds", prefix + ".chp", "num", "0", runBeforeRecovery);
	//	}
	//
	//	@Test
	//	public void test10() {
	//		// Run pipeline and test checkpoint
	//		runAndCheckpoint("test/checkpoint_10.bds", "test/checkpoint_10.chp", "sumMain", "55");
	//	}
	//
	//	@Test
	//	public void test11() {
	//		// Run pipeline and test checkpoint
	//		runAndCheckpoint("test/checkpoint_10.bds", "test/checkpoint_10.chp", "sumPar", "110");
	//	}

}
