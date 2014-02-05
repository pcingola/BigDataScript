package ca.mcgill.mcb.pcingola.bigDataScript.test;


/**
 * Quick test cases when creating a new feature...
 * 
 * @author pcingola
 *
 */
public class TestCasesZzz extends TestCasesBase {

	public void test01() {
		runAndCheckpoint("test/checkpoint_01.bds", null, "i", "10");
	}

}
