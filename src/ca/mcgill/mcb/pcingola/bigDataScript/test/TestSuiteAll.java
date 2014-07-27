package ca.mcgill.mcb.pcingola.bigDataScript.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Invoke all test cases
 *
 * @author pcingola
 */
public class TestSuiteAll {

	public static void main(String args[]) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite();

		suite.addTestSuite(TestCasesTail.class);
		suite.addTestSuite(TestCasesLang.class);
		suite.addTestSuite(TestCasesRun.class);
		suite.addTestSuite(TestCasesRun2.class);
		suite.addTestSuite(TestCasesGraph.class);
		suite.addTestSuite(TestCasesCheckpoint.class);

		return suite;
	}
}
