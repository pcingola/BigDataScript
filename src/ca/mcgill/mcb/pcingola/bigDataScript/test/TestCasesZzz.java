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
	public void test08() {
		String errs = "ERROR [ file 'test/test08.bds', line 11 ] :	Only variable reference can be used with ++ or -- operators\n";
		compileErrors("test/test08.bds", errs);
	}
	//
	//	@Test
	//	public void test118_dependency_using_path() {
	//		runAndCheckExit("test/run_118.bds", 0);
	//	}

	//	@Test
	//	public void test06() {
	//		// Remove old entries
	//		String prefix = "test/graph_06";
	//		File txt = new File(prefix + ".txt");
	//		File csv = new File(prefix + ".csv");
	//		File xml = new File(prefix + ".xml");
	//		txt.delete();
	//		csv.delete();
	//		xml.delete();
	//
	//		// Create file
	//		Gpr.toFile(prefix + ".txt", "TEST");
	//
	//		// Run pipeline first
	//		System.out.println("Run first time:");
	//		String out = runAndCheckStdout(prefix + ".bds", "copying to csv\ncopying to xml");
	//		System.out.println(out);
	//
	//		// Remove CSV file
	//		csv.delete();
	//
	//		// Run pipeline again (nothing should happen, since XML is 'up to date' with respect to TXT)
	//		System.out.println("Run second time:");
	//		out = runAndCheckStdout(prefix + ".bds", "copying", true);
	//		System.out.println(out);
	//	}

	//	@Test
	//	public void test15_checkpoint_par_function_call() {
	//		runAndCheckpoint("test/checkpoint_15.bds", "test/checkpoint_15.chp", "ok", "true");
	//	}
	//
	//	@Test
	//	public void test16_checkpoint_recursive() {
	//		runAndCheckpoint("test/checkpoint_16.bds", "test/checkpoint_16.chp", "fn", "120");
	//	}

}
