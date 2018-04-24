package org.bds.test;

import org.bds.util.Gpr;
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
		Gpr.debug("Test");
		runAndCheckpoint("test/graph_03.bds", "test/graph_03.chp", "out", "Task start\nTask end\n");
	}

	//	@Test
	//	public void test04() {
	//		Gpr.debug("Test");
	//		runAndCheckpoint("test/graph_04.bds", "test/graph_04.chp", "out", "IN\nTASK 1\nTASK 2\n");
	//	}
	//
	//	@Test
	//	public void test09() {
	//		Gpr.debug("Test");
	//		runAndCheckStderr("test/graph_09.bds", "Circular dependency");
	//	}
	//
	//	@Test
	//	public void test12_circularDependency() {
	//		Gpr.debug("Test");
	//		runAndCheckStderr("test/graph_12.bds", "Fatal error: test/graph_12.bds, line 18, pos 1. Circular dependency on task 'graph_12.bds.");
	//	}
	//
	//	@Test
	//	public void test01() {
	//		Gpr.debug("Test");
	//		runAndCheckpoint("test/checkpoint_01.bds", null, "i", "10");
	//	}

}
