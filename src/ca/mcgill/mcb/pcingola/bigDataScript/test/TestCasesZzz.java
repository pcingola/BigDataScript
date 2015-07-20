package ca.mcgill.mcb.pcingola.bigDataScript.test;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

	@Test
	public void test129_chdir_sys() {
		String out = runAndReturnStdout("test/run_129.bds");
		Assert.assertTrue(out.contains("FILE_01\n"));
		Assert.assertTrue(out.contains("FILE_02\n"));
	}

	@Test
	public void test130_chdir_task() {
		String out = runAndReturnStdout("test/run_130.bds");
		Assert.assertTrue(out.contains("FILE_01\n"));
		Assert.assertTrue(out.contains("FILE_02\n"));
	}

	@Test
	public void test131_chdir_fileMethods() {
		String out = ""//
				+ "chdir_test_file_01.txt\tread:FILE_01\n" //
				+ "chdir_test_file_01.txt\treadLines:[FILE_01]\n" //
				+ "chdir_test_file_01.txt\texists:true\n" //
				+ "chdir_test_file_01.txt\tisDir:false\n" //
				+ "chdir_test_file_01.txt\tisEmpty:false\n" //
				+ "chdir_test_file_01.txt\tisFile:true\n" //
				+ "chdir_test_file_01.txt\tcanRead:true\n" //
				+ "chdir_test_file_01.txt\tcanWrite:true\n" //
				+ "\n" //
				+ "----------\n" //
				+ "chdir_test_file_02.txt\tread:FILE_02\n" //
				+ "chdir_test_file_02.txt\treadLines:[FILE_02]\n" //
				+ "chdir_test_file_02.txt\texists:true\n" //
				+ "chdir_test_file_02.txt\tisDir:false\n" //
				+ "chdir_test_file_02.txt\tisEmpty:false\n" //
				+ "chdir_test_file_02.txt\tisFile:true\n" //
				+ "chdir_test_file_02.txt\tcanRead:true\n" //
				+ "chdir_test_file_02.txt\tcanWrite:true\n" //
		;

		String outreal = runAndReturnStdout("test/run_131.bds");
		Assert.assertEquals(out, outreal);
	}

}
