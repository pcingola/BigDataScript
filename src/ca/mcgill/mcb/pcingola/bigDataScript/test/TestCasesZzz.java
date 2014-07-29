package ca.mcgill.mcb.pcingola.bigDataScript.test;

import java.io.File;

import org.junit.Test;

import ca.mcgill.mcb.pcingola.bigDataScript.util.Gpr;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

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
	public void test11() {
		// Run pipeline and test checkpoint
		runAndCheckpoint("test/checkpoint_10.bds", "test/checkpoint_10.chp", "sumPar", "110");
	}

}
