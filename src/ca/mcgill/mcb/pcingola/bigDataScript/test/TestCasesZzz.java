package ca.mcgill.mcb.pcingola.bigDataScript.test;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

	//	@Test
	//	public void test02() {
	//		runAndCheckpoint("test/z_splitLines.bds", null, "l", "15");
	//	}

	@Test
	public void test114_parallel_function_task_calls() {
		String stdout = runAndReturnStdout("test/run_114.bds");

		Set<String> linesPar = new HashSet<String>();
		for (String line : stdout.split("\n"))
			if (line.startsWith("TASK")) linesPar.add(line);

		// Check
		Assert.assertTrue("There should be 5 tasks", linesPar.size() == 5);
	}

}
