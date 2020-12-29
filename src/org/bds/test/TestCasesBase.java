package org.bds.test;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.bds.Bds;
import org.bds.Config;
import org.bds.data.Data;
import org.bds.data.DataRemote;
import org.bds.data.DataS3;
import org.bds.executioner.Executioners;
import org.bds.lang.type.Type;
import org.bds.lang.value.InterpolateVars;
import org.bds.lang.value.Value;
import org.bds.run.BdsThreads;
import org.bds.util.Gpr;
import org.bds.vm.BdsVm;
import org.bds.vm.BdsVmAsm;
import org.junit.Before;

import junit.framework.Assert;

/**
 * All methods shared amongst test cases
 *
 * @author pcingola
 */
public class TestCasesBase {

	public boolean debug = false;
	public boolean verbose = false;

	// Read bucket name from $HOME/.bds/aws_test_bucket.config
	protected String awsBucketName() {
		return awsTestConfig().get("bucket").trim();
	}

	// Read region from $HOME/.bds/aws_test_bucket.config
	protected String awsRegion() {
		return awsTestConfig().get("region").trim();
	}

	// Read bucket name from $HOME/.bds/aws_test_bucket.config
	protected Map<String, String> awsTestConfig() {
		Map<String, String> keyValues = new HashMap<>();
		String awsBucketNameFile = Gpr.HOME + "/.bds/aws_test.config";
		for (String line : Gpr.readFile(awsBucketNameFile).split("\n")) {
			String[] kv = line.split("\\t");
			keyValues.put(kv[0], kv[1]);
		}
		return keyValues;
	}

	@Before
	public void before() {
		// Reset singletons
		Config.reset();
		Executioners.reset();
		BdsThreads.reset();
	}

	protected void checkInterpolate(String str, String strings[], String vars[]) {
		InterpolateVars iv = new InterpolateVars(null, null);
		iv.parse(str);
		if (verbose) {
			System.out.println("String: " + str);
			System.out.println("\tInterpolation result: |" + iv + "|");
		}

		// Special case: No variables to interpolate
		if (strings.length == 1 && vars[0].isEmpty()) {
			Assert.assertTrue(iv.isEmpty());
			return;
		}

		// Check strings
		for (int i = 0; i < strings.length; i++) {
			if (verbose) {
				System.out.print("\tIndex: " + i);
				System.out.print("\tstring.expected: " + strings[i] + "\tstring.actual: " + iv.getLiterals()[i]);
				System.out.println("\tvar.expected: " + vars[i] + "\tvar.actual: " + iv.getExpressions()[i]);
			}

			Assert.assertEquals(strings[i], iv.getLiterals()[i]);
			if (vars[i] != null && !vars[i].isEmpty()) Assert.assertEquals(vars[i], iv.getExpressions()[i].toString());
		}
	}

	/**
	 * Check a file in an S3 bucket
	 */
	protected void checkS3File(String url, String region, String bucket, String path, String paren, String txt) {
		int objectSize = txt.length();
		long now = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();

		//		Data d = Data.factory(url);
		DataS3 d = new DataS3(url, region);

		d.setVerbose(verbose);
		d.setDebug(debug);
		long lastMod = d.getLastModified().getTime();
		if (verbose) Gpr.debug("Path: " + d.getPath() + "\tlastModified: " + lastMod + "\tSize: " + d.size());

		// Check some features
		Assert.assertTrue("Is S3?", d instanceof DataS3);
		Assert.assertEquals(path, d.getAbsolutePath());
		Assert.assertEquals(objectSize, d.size());
		Assert.assertTrue((now - lastMod) < 2 * DataRemote.CACHE_TIMEOUT);
		Assert.assertTrue("Is file?", d.isFile());
		Assert.assertFalse("Is directory?", d.isDirectory());

		// Download file
		boolean ok = d.download();
		Assert.assertTrue("Download OK", ok);
		Assert.assertTrue("Is downloaded?", d.isDownloaded());

		// Is it at the correct local file?
		Assert.assertEquals("/tmp/bds/s3/" + bucket.replace('-', '_') + path, d.getLocalPath());
		Assert.assertEquals(path, d.getAbsolutePath());
		Assert.assertEquals(paren, d.getParent().toString());
		Assert.assertEquals("hello.txt", d.getName());

		// Check last modified time
		File file = new File(d.getLocalPath());
		long lastModLoc = file.lastModified();
		Assert.assertTrue("Last modified check:" //
				+ "\n\tlastMod    : " + lastMod //
				+ "\n\tlastModLoc : " + lastModLoc //
				+ "\n\tDiff       : " + (lastMod - lastModLoc)//
				, Math.abs(lastMod - lastModLoc) < 2 * DataRemote.CACHE_TIMEOUT);

		Assert.assertEquals(objectSize, file.length());
	}

	/**
	 * Check that a file compiles with expected errors
	 */
	protected void compileErrors(String fileName, String expectedErrors) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.compile();
		bdsTest.checkCompileError(expectedErrors);
	}

	/**
	 * Check that a file compiles without any errors
	 */
	protected void compileOk(String fileName) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.compile();
		bdsTest.checkCompileOk();
	}

	// Create a file in S3
	protected void createS3File(String s3file, String region, String text) {
		String localFile = "createS3.tmp";
		Gpr.toFile(localFile, text);
		DataS3 ds3 = new DataS3(s3file, region);
		Data dlocal = Data.factory(localFile);
		ds3.upload(dlocal);
		dlocal.delete();
	}

	protected BdsTest runAndCheck(int expectedExitCode, String fileName, Map<String, Object> expectedValues) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.run();
		bdsTest.checkExitCode(expectedExitCode);
		bdsTest.checkVariables(expectedValues);
		return bdsTest;
	}

	protected BdsTest runAndCheck(int expectedExitCode, String fileName, String varname, Object expectedValue) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.run();
		bdsTest.checkExitCode(expectedExitCode);
		bdsTest.checkVariable(varname, expectedValue);
		return bdsTest;
	}

	protected BdsTest runAndCheck(String fileName, Map<String, Object> expectedValues) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.run();
		bdsTest.checkRunOk();
		bdsTest.checkVariables(expectedValues);
		return bdsTest;
	}

	/**
	 * Check that a file compiles without any errors, runs and all variables have their expected values
	 */
	protected BdsTest runAndCheck(String fileName, Map<String, Object> expectedValues, List<String> argsAfterList) {
		String argsAfter[] = argsAfterList.toArray(new String[0]);
		BdsTest bdsTest = new BdsTest(fileName, null, argsAfter, verbose, debug);
		bdsTest.run();
		bdsTest.checkRunOk();
		bdsTest.checkVariables(expectedValues);
		return bdsTest;
	}

	/**
	 * Check that a file compiles without any errors, runs and a variable have its expected map
	 */
	protected BdsTest runAndCheck(String fileName, String varname, Object expectedValue) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.run();
		bdsTest.checkRunOk();
		bdsTest.checkVariable(varname, expectedValue);
		return bdsTest;
	}

	/**
	 * Check that a file compiles without any errors, runs and a variable have its expected map
	 */
	protected BdsTest runAndCheck(String fileName, String[] args, String varname, Object expectedValue) {
		BdsTest bdsTest = new BdsTest(fileName, args, verbose, debug);
		bdsTest.run();
		bdsTest.checkRunOk();
		bdsTest.checkVariable(varname, expectedValue);
		return bdsTest;
	}

	protected BdsTest runAndCheckException(String fileName, String exceptionType) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.run();
		bdsTest.checkRunExitCodeFail();
		bdsTest.checkException(exceptionType);
		return bdsTest;
	}

	/**
	 * Check exit code
	 */
	protected BdsTest runAndCheckExit(String fileName, int expectedExitValue) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.run();
		bdsTest.checkExitCode(expectedExitValue);
		return bdsTest;
	}

	/**
	 * Check that StdOut has a string
	 */
	protected BdsTest runAndCheckHelp(String fileName, String expectedStdout) {
		String argsAfter[] = { "-h" };
		BdsTest bdsTest = new BdsTest(fileName, null, argsAfter, verbose, debug);
		bdsTest.run();
		bdsTest.checkStdout(expectedStdout);
		return bdsTest;
	}

	/**
	 * Check that a file recovers from a checkpoint and runs without errors
	 */
	protected Bds runAndCheckpoint(String fileName, String checkpointFileName, String varname, Object expectedValue) {
		return runAndCheckpoint(fileName, checkpointFileName, varname, expectedValue, null);
	}

	/**
	 * Check that a file recovers from a checkpoint and runs without errors
	 */
	protected Bds runAndCheckpoint(String fileName, String checkpointFileName, String varName, Object expectedValue, Runnable runBeforeRecover) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		return bdsTest.runAndCheckpoint(checkpointFileName, varName, expectedValue, runBeforeRecover);
	}

	/**
	 * Check that StdErr has a string
	 */
	protected BdsTest runAndCheckStderr(String fileName, String expectedStderr) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.run();
		bdsTest.checkStderr(expectedStderr);
		return bdsTest;
	}

	/**
	 * Check that StdOut has a string (or that the string is NOT present if 'negate' is true)
	 */
	protected String runAndCheckStdout(String fileName, List<String> expectedStdout, String args[], boolean negate) {
		BdsTest bdsTest = new BdsTest(fileName, args, verbose, debug);
		bdsTest.run();
		bdsTest.checkRunOk();
		for (String exp : expectedStdout)
			bdsTest.checkStdout(exp, negate);
		return bdsTest.captureStdout.toString();
	}

	/**
	 * Check that StdOut has a string
	 */
	protected String runAndCheckStdout(String fileName, String expectedStdout) {
		return runAndCheckStdout(fileName, expectedStdout, null, false);
	}

	protected String runAndCheckStdout(String fileName, String expectedStdout, boolean negate) {
		return runAndCheckStdout(fileName, expectedStdout, null, negate);
	}

	/**
	 * Check that StdOut has a string (or that the string is NOT present if 'negate' is true)
	 */
	protected String runAndCheckStdout(String fileName, String expectedStdout, String args[], boolean negate) {
		BdsTest bdsTest = new BdsTest(fileName, args, verbose, debug);
		bdsTest.run();
		bdsTest.checkRunOk();
		bdsTest.checkStdout(expectedStdout, negate);
		return bdsTest.captureStdout.toString();
	}

	/**
	 * Check that a file compiles without any errors, runs and obtains a variable
	 */
	protected Object runAndGet(String fileName, String varname) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.run();
		bdsTest.checkRunOk();
		return bdsTest.getValue(varname);
	}

	/**
	 * Run and get latest report file
	 */
	protected String runAndGetReport(String fileName, boolean yaml) {
		String[] argsHtml = { "-reportHtml" };
		String[] argsYaml = { "-reportYaml" };
		String[] args = yaml ? argsYaml : argsHtml;

		BdsTest bdsTest = new BdsTest(fileName, args, verbose, debug);

		bdsTest.run();
		bdsTest.checkRunOk();
		String reportFileName = bdsTest.bds.getBdsRun().getBdsThread().getReportFile();

		// Sanity check
		Assert.assertNotNull("No report generated", reportFileName);
		Assert.assertTrue("Report file not found", Gpr.exists(reportFileName));

		String report = Gpr.readFile(reportFileName);
		if (debug) System.err.println("Report:\n" + report);

		return report;
	}

	/**
	 * Run a bds program and capture stdout (while still showing it)
	 */
	protected String runAndReturnStdout(String fileName) {
		return runAndReturnStdout(fileName, null);
	}

	/**
	 * Run a bds program and capture stdout (while still showing it)
	 */
	protected String runAndReturnStdout(String fileName, String args[]) {
		BdsTest bdsTest = new BdsTest(fileName, args, verbose, debug);
		bdsTest.run();
		bdsTest.checkRunOk();
		return bdsTest.captureStdout.toString();
	}

	/**
	 * Make sure the program runs OK
	 */
	protected void runOk(String fileName) {
		runAndCheckExit(fileName, 0);
	}

	/**
	 * Run test cases: Check that at least one test case FAILS
	 */
	protected void runTestCasesFail(String fileName) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.setTestCases(true);
		bdsTest.run();
		bdsTest.checkRunExitCodeFail();
	}

	protected Bds runTestCasesFailCoverage(String fileName, double coverageMin) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.setCoverage(true);
		bdsTest.setCoverageMin(coverageMin);
		bdsTest.setTestCases(true);
		bdsTest.run();
		bdsTest.checkRunExitCodeFail();
		return bdsTest.bds;
	}

	/**
	 * Run test cases: Check that all test cases PASS
	 */
	protected void runTestCasesPass(String fileName) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.setTestCases(true);
		bdsTest.run();
		bdsTest.checkRunOk();
	}

	protected Bds runTestCasesPassCoverage(String fileName, double coverageMin) {
		BdsTest bdsTest = new BdsTest(fileName, verbose, debug);
		bdsTest.setCoverage(true);
		bdsTest.setCoverageMin(coverageMin);
		bdsTest.setTestCases(true);
		bdsTest.run();
		bdsTest.checkRunOk();
		return bdsTest.bds;
	}

	/**
	 * Check that a file compiles without any errors, runs the VM
	 * and checks that a variable has the expected value
	 */
	protected void runVmAndCheck(String fileName, String varname, Object expectedValue) {
		runVmAndCheck(fileName, varname, expectedValue, null);
	}

	/**
	 * Check that a file compiles without any errors, runs the VM
	 * and checks that a variable has the expected value.
	 * Add all 'types' during compilation
	 */
	protected void runVmAndCheck(String fileName, String varname, Object expectedValue, List<Type> types) {
		BdsVmAsm vmasm = new BdsVmAsm(fileName);
		vmasm.setDebug(debug);
		vmasm.setVerbose(verbose);
		if (types != null) types.forEach(t -> vmasm.addType(t));
		BdsVm vm = vmasm.compile();
		vm.run();

		// Check value
		Value val = vm.getValue(varname);
		Assert.assertTrue("Variable '" + varname + "' not found ", val != null);
		Assert.assertEquals( //
				"Variable '" + varname + "' has different value than expeced:\n" //
						+ "\tExpected value : " + expectedValue //
						+ "\tReal value     : " + val //
				, expectedValue.toString() //
				, val.toString() //
		);
	}

	public void sleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
