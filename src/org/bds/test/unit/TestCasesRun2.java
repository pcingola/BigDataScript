package org.bds.test.unit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bds.lang.value.Value;
import org.bds.lang.value.ValueList;
import org.bds.test.TestCasesBase;
import org.bds.util.Gpr;
import org.junit.Test;

import junit.framework.Assert;

/**
 * Test cases that require BDS code execution and check results
 *
 * Note: These test cases requires that the BDS code is correctly parsed, compiled and executes.
 *
 * @author pcingola
 *
 */
public class TestCasesRun2 extends TestCasesBase {

	@Test
	public void test100() {
		Gpr.debug("Test");

		HashMap<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("s", 1);
		expectedValues.put("s2", -1);

		runAndCheck("test/run_100.bds", expectedValues);
	}

	@Test
	public void test101() {
		Gpr.debug("Test");
		HashMap<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("a", 1);
		expectedValues.put("b", 3);
		expectedValues.put("c", 5);

		runAndCheck("test/run_101.bds", expectedValues);
	}

	@Test
	public void test102() {
		Gpr.debug("Test");
		HashMap<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("a", 1);
		expectedValues.put("b", 3);
		expectedValues.put("c", 5);
		expectedValues.put("d", 1);

		runAndCheck("test/run_102.bds", expectedValues);
	}

	@Test
	public void test103() {
		Gpr.debug("Test");
		HashMap<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("is", "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]");
		expectedValues.put("is2", "[1, 3, 5, 7, 9]");
		expectedValues.put("rs", "[1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 5.5, 6.0, 6.5, 7.0, 7.5, 8.0, 8.5, 9.0, 9.5, 10.0]");

		runAndCheck("test/run_103.bds", expectedValues);
	}

	@Test
	public void test106() {
		Gpr.debug("Test");
		HashMap<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("str1", "l[1] : '2'");
		expectedValues.put("str2", "m{'Hello'} : 'Bye'");

		runAndCheck("test/run_106.bds", expectedValues);
	}

	@Test
	public void test107() {
		Gpr.debug("Test");
		HashMap<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("paramName", "parameter_value");
		expectedValues.put("file1", "/path/to/file_1.txt");
		expectedValues.put("file2", "/path/to/file_2.txt");
		expectedValues.put("file3", "/path/to/file_3.txt");
		expectedValues.put("file4", "/path/to/file_4.txt");
		expectedValues.put("file5", "/path/to/file_5.txt");

		runAndCheck("test/run_107.bds", expectedValues);
	}

	@Test
	public void test108() {
		Gpr.debug("Test");
		HashMap<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("paramName", "parameter_value");
		expectedValues.put("file1", "/path/to/file_1.txt");
		expectedValues.put("file2", "/path/to/file_2.txt");
		expectedValues.put("file3", "/path/to/file_3.NEW.txt");
		expectedValues.put("file4", "/path/to/file_4.txt");
		expectedValues.put("file5", "/path/to/file_5.NEW.txt");

		runAndCheck("test/run_108.bds", expectedValues);
	}

	public void test109() {
		Gpr.debug("Test");
		runAndCheck("test/run_109.bds", "r1", "4027146782649399912");
	}

	public void test110() {
		Gpr.debug("Test");
		runAndCheck("test/run_110.bds", "runOk", "true");
	}

	public void test111() {
		Gpr.debug("Test");
		runAndCheck("test/run_111.bds", "runOk", "false");
	}

	public void test112() {
		Gpr.debug("Test");
		runAndCheck("test/run_112.bds", "runOk", "false");
	}

	@Test
	public void test113_parallel_function_calls() {
		Gpr.debug("Test");
		String stdout = runAndReturnStdout("test/run_113.bds");

		Set<String> linesPar = new HashSet<>();
		for (String line : stdout.split("\n")) {
			if (line.startsWith("Par:")) {
				if (linesPar.contains(line)) throw new RuntimeException("Line repeated (this should never happen): '" + line + "'");
				linesPar.add(line);
			}
		}
	}

	@Test
	public void test116_lineWrap_backslashId() {
		Gpr.debug("Test");
		String stdout = runAndReturnStdout("test/run_116.bds");
		Assert.assertEquals("hi bye\nThe answer\t\tis: 42", stdout);
	}

	@Test
	public void test118_dependency_using_path() {
		Gpr.debug("Test");
		runAndCheckExit("test/run_118.bds", 0);
	}

	@Test
	public void test120_split_empty_string() {
		Gpr.debug("Test");
		runAndCheck("test/run_120.bds", "len", "0");
	}

	@Test
	public void test121_split_fail_regex() {
		Gpr.debug("Test");
		runAndCheck("test/run_121.bds", "len", "1");
	}

	@Test
	public void test122_nestest_break_continue() {
		Gpr.debug("Test");
		runAndCheck("test/run_122.bds", "out", "5\t7");
	}

	@Test
	public void test123_literals() {
		Gpr.debug("Test");

		String output = "print_quote        |\\t|\n" //
				+ "print_quote        |\\t|    variable:$hi\n" //
				+ "print_double       |\t|\n" //
				+ "print_double       |\t|    variable:Hello\n" //
				+ "print_double_esc   |\\t|\n" //
				+ "print_double_esc   |\\t|   variable:Hello\n" //
		;

		runAndCheckStdout("test/run_123.bds", output);
	}

	@Test
	public void test123_literals_sys() {
		Gpr.debug("Test");

		String output = "" //
				// Note: This result may change if we use a different sysShell in bds.config
				+ "sys                |\t|\n" //
				+ "sys                |\t|    variable:Hello\n" //
				+ "sys                |\\t|   variable:Hello\n" //
		;

		runAndCheckStdout("test/run_123_literals_sys.bds", output);
	}

	@Test
	public void test124_quiet_mode() {
		Gpr.debug("Test");
		String output = "print 0\n" //
				+ "print 1\n" //
				+ "print 2\n" //
				+ "print 3\n" //
				+ "print 4\n" //
				+ "print 5\n" //
				+ "print 6\n" //
				+ "print 7\n" //
				+ "print 8\n" //
				+ "print 9\n" //
		;

		// Run and capture stdout
		String args[] = { "-quiet" };
		String stdout = runAndReturnStdout("test/run_124.bds", args);
		if (verbose) System.err.println("STDOUT: " + stdout);

		// Check that sys and task outputs are not there
		Assert.assertTrue("Print output should be in STDOUT", stdout.contains(output));
		Assert.assertTrue("Task output should NOT be in STDOUT", !stdout.contains("task"));
		Assert.assertTrue("Sys output should NOT be in STDOUT", !stdout.contains("sys"));
	}

	@Test
	public void test125_automatic_help() {
		Gpr.debug("Test");
		String output = "Command line options 'run_125.bds' :\n" //
				+ "\t-mean <int>                                  : Help for argument 'mean' should be printed here\n" //
				+ "\t-min <int>                                   : Help for argument 'min' should be printed here\n" //
				+ "\t-num <int>                                   : Number of times 'hi' should be printed\n" //
				+ "\t-salutation <string>                         : Salutation to use\n" //
				+ "\t-someVeryLongCommandLineArgumentName <bool>  : This command line argument has a really long name\n" //
				+ "\t-useTab <bool>                               : Use tab before printing line\n" //
				+ "\n" //
		;

		runAndCheckHelp("test/run_125.bds", output);
	}

	@Test
	public void test125b_automatic_help_unsorted() {
		Gpr.debug("Test");
		String output = "Command line options 'run_125b.bds' :\n" //
				+ "\t-useTab <bool>                               : Use tab before printing line\n" //
				+ "\t-someVeryLongCommandLineArgumentName <bool>  : This command line argument has a really long name\n" //
				+ "\t-salutation <string>                         : Salutation to use\n" //
				+ "\t-num <int>                                   : Number of times 'hi' should be printed\n" //
				+ "\t-min <int>                                   : Help for argument 'min' should be printed here\n" //
				+ "\t-mean <int>                                  : Help for argument 'mean' should be printed here\n" //
				+ "\n" //
		;

		runAndCheckHelp("test/run_125b.bds", output);
	}

	/**
	 * Show help when there are no arguments
	 */
	@Test
	public void test125c_automatic_help() {
		Gpr.debug("Test");
		String output = "Command line options 'run_125c.bds' :\n" //
				+ "\t-mean <int>                                  : Help for argument 'mean' should be printed here\n" //
				+ "\t-min <int>                                   : Help for argument 'min' should be printed here\n" //
				+ "\t-num <int>                                   : Number of times 'hi' should be printed\n" //
				+ "\t-salutation <string>                         : Salutation to use\n" //
				+ "\t-someVeryLongCommandLineArgumentName <bool>  : This command line argument has a really long name\n" //
				+ "\t-useTab <bool>                               : Use tab before printing line\n" //
				+ "\n" //
		;

		runAndCheckHelp("test/run_125c.bds", output);
	}

	/**
	 * Task dependent on output from a scheduled task
	 */
	@Test
	public void test126_task_dependency_scheduled() {
		Gpr.debug("Test");

		String expectedOutput = "IN: " + Gpr.HOME + "/zzz/in.txt\n" //
				+ "OUT: " + Gpr.HOME + "/zzz/out.txt\n" //
				+ "OUT_0: " + Gpr.HOME + "/zzz/out_0.txt\n" //
				+ "    OUT_0_0: " + Gpr.HOME + "/zzz/out_0_0.txt\n" //
				+ "    OUT_0_1: " + Gpr.HOME + "/zzz/out_0_1.txt\n" //
				+ "    OUT_0_2: " + Gpr.HOME + "/zzz/out_0_2.txt\n" //
				+ "OUT_1: " + Gpr.HOME + "/zzz/out_1.txt\n" //
				+ "    OUT_1_0: " + Gpr.HOME + "/zzz/out_1_0.txt\n" //
				+ "    OUT_1_1: " + Gpr.HOME + "/zzz/out_1_1.txt\n" //
				+ "    OUT_1_2: " + Gpr.HOME + "/zzz/out_1_2.txt\n" //
				+ "OUT_2: " + Gpr.HOME + "/zzz/out_2.txt\n" //
				+ "    OUT_2_0: " + Gpr.HOME + "/zzz/out_2_0.txt\n" //
				+ "    OUT_2_1: " + Gpr.HOME + "/zzz/out_2_1.txt\n" //
				+ "    OUT_2_2: " + Gpr.HOME + "/zzz/out_2_2.txt\n" //
		;

		String stdout = runAndReturnStdout("test/run_126.bds");
		if (verbose) {
			System.err.println("STDOUT:" //
					+ "\n----------------------------------------\n" //
					+ stdout //
					+ "\n----------------------------------------" //
			);
		}

		// Check that task output lines
		for (String out : expectedOutput.split("\n")) {
			Assert.assertTrue("Expected output line not found: '" + out + "'", stdout.contains(out));
		}
	}

	@Test
	public void test127_interpolate_variable_with_underscores() {
		Gpr.debug("Test");

		String output = "bwa parameters\n" //
				+ "bwa parameters\n" //
				+ "bwa parameters\n" //
				+ "bwa parameters\n" //
		;

		runAndCheckStdout("test/run_127.bds", output);
	}

	@Test
	public void test128_task_local_variables() {
		Gpr.debug("Test");
		runAndCheckStdout("test/run_128.bds", "TEST\n");
	}

	@Test
	public void test129_chdir_sys() {
		Gpr.debug("Test");
		String out = runAndReturnStdout("test/run_129.bds");
		Assert.assertTrue(out.contains("FILE_01\n"));
		Assert.assertTrue(out.contains("FILE_02\n"));
	}

	@Test
	public void test130_chdir_task() {
		Gpr.debug("Test");
		String out = runAndReturnStdout("test/run_130.bds");
		Assert.assertTrue(out.contains("FILE_01\n"));
		Assert.assertTrue(out.contains("FILE_02\n"));
	}

	@Test
	public void test131_chdir_fileMethods() {
		Gpr.debug("Test");
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

	/**
	 * Make sure taskId contains 'taskName' parameter
	 */
	@Test
	public void test132_taskName() {
		Gpr.debug("Test");
		String out = runAndReturnStdout("test/run_132.bds");
		Assert.assertTrue(out.contains("run_132.mytask"));
	}

	/**
	 * Make sure taskId contains 'taskName' parameter
	 * In this test 'taskName' is not safe to be used with as file name, so it has to be sanitized
	 */
	@Test
	public void test133_taskName_unsafe() {
		Gpr.debug("Test");
		String out = runAndReturnStdout("test/run_133.bds");
		Assert.assertTrue(out.contains("run_133.mytask_unsafe_with_spaces"));
	}

	/**
	 * Show help when there are no arguments
	 */
	@Test
	public void test134_automatic_help_sections() {
		Gpr.debug("Test");
		String output = "This program does blah\n" //
				+ "Actually, a lot of blah blah\n" //
				+ "    and even more blah\n" //
				+ "    or blah\n" //
				+ "\t-quiet <bool>     : Be very quiet\n" //
				+ "\t-verbose <bool>   : Be verbose\n" //
				+ "Options related to database\n" //
				+ "\t-dbName <string>  : Database name\n" //
				+ "\t-dbPort <int>     : Database port\n" //
				+ "\n" //
		;

		runAndCheckHelp("test/run_134.bds", output);
	}

	@Test
	public void test135() {
		Gpr.debug("Test");
		HashMap<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("l", "[1, 2, 3, 99]");
		expectedValues.put("lc1", "[1, 2, 3, 99]");
		expectedValues.put("lc2", "[1, 2, 3, 99]");
		expectedValues.put("lc3", "[1, 2, 3, 99]");

		runAndCheck("test/run_135.bds", expectedValues);
	}

	@Test
	public void test135_clone() {
		Gpr.debug("Test");
		HashMap<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("l", "[1, 2, 3, 99]");
		expectedValues.put("lc1", "[1, 2, 3]");
		expectedValues.put("lc2", "[1, 2, 3]");
		expectedValues.put("lc3", "[1, 2, 3]");

		runAndCheck("test/run_135_clone.bds", expectedValues);
	}

	@Test
	public void test136() {
		Gpr.debug("Test");
		HashMap<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("l", "[1, 99, 2, 3]");
		expectedValues.put("l2", "[3, 2, 99, 1]");
		expectedValues.put("l3", "[3, 2, 99, 1, 99]");
		expectedValues.put("l3count", "2");
		expectedValues.put("l3idx", "2");
		expectedValues.put("l4", "[3, 2, 1, 99]");
		expectedValues.put("l5", "[3, 2, 1, 99]");

		runAndCheck("test/run_136.bds", expectedValues);
	}

	@Test
	public void test137() {
		Gpr.debug("Test");
		HashMap<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("l", "[1, 2, 3]");
		expectedValues.put("has2", "true");
		expectedValues.put("has7", "false");

		runAndCheck("test/run_137.bds", expectedValues);
	}

	/**
	 * Cos(x)
	 */
	@Test
	public void test138() {
		Gpr.debug("Test");
		runAndCheck("test/run_138.bds", "hasErr", "false");
	}

	/**
	 * Sin(x)
	 */
	@Test
	public void test139() {
		Gpr.debug("Test");
		runAndCheck("test/run_139.bds", "hasErr", "false");
	}

	@Test
	public void test140_list_nonvariable() {
		Gpr.debug("Test");
		runAndCheck("test/run_140.bds", "i", "2");
	}

	@Test
	public void test141_map_nonvariable() {
		Gpr.debug("Test");
		runAndCheck("test/run_141.bds", "i", "42");
	}

	@Test
	public void test142_dirPath() {
		Gpr.debug("Test");
		ValueList dir2 = (ValueList) runAndGet("test/run_142.bds", "dir2");

		Assert.assertEquals(10, dir2.size());

		for (Value v : dir2) {
			String f = v.toString();
			debug(f);
			Assert.assertTrue("Path must be canonical", f.startsWith("/"));
			Assert.assertTrue("Path must be canonical", f.endsWith(".bds"));
		}
	}

	@Test
	public void test143_pathAbsolute() {
		Gpr.debug("Test");
		runAndCheck("test/run_143.bds", "fileBase", "tmp_run_143_link.txt");
	}

	@Test
	public void test144_dollar_sign_in_task() {
		Gpr.debug("Test");

		// We want to execute an inline perl script within a task
		// E.g.:
		//     task perl -e 'use English; print "PID: \$PID\n";'
		//
		// Here $PID is a perl variable and should not be interpreted
		// by bds. We need a way to escape such variables.
		String bdsFile = "test/run_144.bds";

		String stdout = runAndReturnStdout(bdsFile);

		// Parse STDOUT
		String lines[] = stdout.split("\n");
		Assert.assertEquals("No lines found?", 3, lines.length);

		for (String line : lines) {
			String fields[] = line.split(":");
			Assert.assertEquals("Cannot parse line:\n" + line, 2, fields.length);

			int positiveNumber = Gpr.parseIntSafe(fields[1]);
			Assert.assertTrue("Positive number expected: '" + fields[1] + "'", positiveNumber > 0);
		}
	}

	@Test
	public void test145_switch() {
		Gpr.debug("Test");
		runAndCheck("test/run_145.bds", "out", 3);
	}

	@Test
	public void test146_switch_fallthrough() {
		Gpr.debug("Test");
		runAndCheck("test/run_146.bds", "out", 35);
	}

	@Test
	public void test147_switch_default() {
		Gpr.debug("Test");
		runAndCheck("test/run_147.bds", "out", 100);
	}

	@Test
	public void test148_switch_default_fallthrough() {
		Gpr.debug("Test");
		runAndCheck("test/run_148.bds", "out", 700);
	}

	@Test
	public void test149_div() {
		Gpr.debug("Test");
		HashMap<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("val0", 50);
		expectedValues.put("val1", 50.0);
		expectedValues.put("val2", 50.0);
		expectedValues.put("val3", 50.0);

		runAndCheck("test/run_149.bds", expectedValues);
	}

	@Test
	public void test150_mult() {
		Gpr.debug("Test");
		HashMap<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("val0", 200);
		expectedValues.put("val1", 200.0);
		expectedValues.put("val2", 200.0);
		expectedValues.put("val3", 200.0);

		runAndCheck("test/run_150.bds", expectedValues);
	}

	@Test
	public void test151_plus() {
		Gpr.debug("Test");
		HashMap<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("val0", 102);
		expectedValues.put("val1", 102.0);
		expectedValues.put("val2", 102.0);
		expectedValues.put("val3", 102.0);

		runAndCheck("test/run_151.bds", expectedValues);
	}

	@Test
	public void test152_minus() {
		Gpr.debug("Test");
		HashMap<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("val0", 98);
		expectedValues.put("val1", 98.0);
		expectedValues.put("val2", 98.0);
		expectedValues.put("val3", 98.0);

		runAndCheck("test/run_152.bds", expectedValues);
	}

	@Test
	public void test153_caseInt() {
		Gpr.debug("Test");
		runAndCheck("test/run_153.bds", "r", "The answer");
	}

	@Test
	public void test154_caseReal() {
		Gpr.debug("Test");
		runAndCheck("test/run_154.bds", "res", "OK");
	}

	@Test
	public void test155_list_sort() {
		Gpr.debug("Test");
		runAndCheck("test/run_155.bds", "sl", "[a, a, a+a, a_a, aa, aaa, aaaa, aaaaaa, x, y, z]");
	}

	@Test
	public void test156_list_int_sort() {
		Gpr.debug("Test");
		runAndCheck("test/run_156.bds", "sl", "[-99, -1, 1, 2, 3, 9, 23, 99, 101]");
	}

	@Test
	public void test157_multiline_sys() {
		Gpr.debug("Test");
		runAndCheck("test/run_157.bds", "o", "hello world\n");
	}

	@Test
	public void test158_log() {
		runAndCheckStderr("test/run_158.bds", "hi there");
	}

	@Test
	public void test159_task_prelude() {
		Gpr.debug("Test");
		String args[] = { "-c", "test/test159_prelude_task.config" };
		runAndCheckStdout("test/run_159.bds", "=== TASK PRELUDE local ===", args, false);
	}

	@Test
	public void test161() {
		Gpr.debug("Test");
		runAndCheck("test/run_161.bds", "out", "a.x = 42");
	}

	@Test
	public void test162() {
		Gpr.debug("Test");
		runAndCheck("test/run_162.bds", "out", "B: A: Hi");
	}

	@Test
	public void test164() {
		Gpr.debug("Test");
		runAndCheck("test/run_164.bds", "out", "hi");
	}

	@Test
	public void test165() {
		Gpr.debug("Test");
		Map<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("n1", "500");
		expectedValues.put("n2", "500");
		runAndCheck("test/run_165.bds", expectedValues);
	}

	@Test
	public void test166_switch_case_return() {
		Gpr.debug("Test");
		runAndCheck("test/run_166.bds", "res", "1");
	}

	@Test
	public void test167_binary_expression_assign_bool() {
		Gpr.debug("Test");
		Map<String, Object> expectedValues = new HashMap<>();
		expectedValues.put("band1", "true");
		expectedValues.put("band2", "false");
		expectedValues.put("bor1", "true");
		expectedValues.put("bor2", "false");
		runAndCheck("test/run_167.bds", expectedValues);
	}
}
