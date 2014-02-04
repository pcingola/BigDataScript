package ca.mcgill.mcb.pcingola.bigDataScript.test;

import java.util.HashMap;

import org.junit.Test;

/**
 * Quick test cases when creating a new feature...
 * 
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

	@Test
	public void test89() {
		HashMap<String, Object> expectedValues = new HashMap<String, Object>();
		expectedValues.put("f", "file.txt");
		expectedValues.put("f2", "file.vcf");
		expectedValues.put("f3", "file.vcf");
		expectedValues.put("f4", "file.txt.vcf");
		runAndCheckMultiple("test/run_89.bds", expectedValues);
	}

}
