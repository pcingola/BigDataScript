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
	public void test12_circularDependency() {
		runAndCheckStderr("test/graph_12.bds", "Fatal error: test/graph_12.bds, line 18, pos 1. Circular dependency on task 'graph_12.bds.");
	}
}
