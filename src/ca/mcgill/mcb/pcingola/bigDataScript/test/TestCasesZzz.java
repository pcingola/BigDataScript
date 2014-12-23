package ca.mcgill.mcb.pcingola.bigDataScript.test;

/**
 * Quick test cases when creating a new feature...
 *
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

	public void test92() {
		verbose = true;
		runAndCheck("test/run_92.bds", "outs", "TASK 1\nTASK 2\n");
	}

	//	@Test
	//	public void test02() {
	//		verbose = true;
	//		runAndCheckpoint("test/checkpoint_02.bds", null, "l", "15");
	//	}
}
