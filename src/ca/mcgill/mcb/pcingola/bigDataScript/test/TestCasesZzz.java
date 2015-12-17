package ca.mcgill.mcb.pcingola.bigDataScript.test;

import java.util.ArrayList;
import java.util.HashMap;

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
	public void test47() {
		verbose = true;
		Gpr.debug("Test");
		HashMap<String, Object> expectedValues = new HashMap<String, Object>();
		expectedValues.put("in", "[in1.txt, in2.txt, in3.txt]");
		expectedValues.put("out", "zzz.txt");
		expectedValues.put("ok", "true");

		ArrayList<String> args = new ArrayList<String>();

		args.add("-ok");

		args.add("-in");
		args.add("in1.txt");
		args.add("in2.txt");
		args.add("in3.txt");

		args.add("-out");
		args.add("zzz.txt");

		runAndCheck("test/run_47.bds", expectedValues, args);
	}

}
