package ca.mcgill.mcb.pcingola.bigDataScript.test;

import org.junit.Test;

/**
 * Test cases aboug graph construction and 'implicit wait' commands
 *
 * @author pcingola
 *
 */
public class TestCasesGraph extends TestCasesBase {

	@Test
	public void test01() {
		runAndCheck("test/graph_01.bds", "output", "IN\nTASK 1\nTASK 2\n");
	}

	@Test
	public void test02() {
		runAndCheck("test/graph_02.bds", "output", "IN\nTASK 1\nTASK 2\nTASK 3\n");
	}

	@Test
	public void test03() {
		runAndCheckpoint("test/graph_03.bds", "test/graph_03.chp", "out", "Task start\nTask end\n");
	}

	@Test
	public void test04() {
		runAndCheckpoint("test/graph_04.bds", "test/graph_04.chp", "out", "IN\nTASK 1\nTASK 2\n");
	}

}
