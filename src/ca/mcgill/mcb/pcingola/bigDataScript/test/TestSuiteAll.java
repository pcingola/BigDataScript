package ca.mcgill.mcb.pcingola.bigDataScript.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Invoke all test cases
 *
 * @author pcingola
 */

@RunWith(Suite.class)
@SuiteClasses({ TestCasesTail.class, //
		TestCasesLang.class, //
		TestCasesInterpolate.class, //
		TestCasesExecutioners.class, //
		TestCasesRun.class, //
		TestCasesRun2.class, //
		TestCasesGraph.class, //
		TestCasesCheckpoint.class, //
		TestCasesCommandLineOptions.class, //
		TestCasesClusterGeneric.class, //
		TestCasesRemote.class, //
})
public class TestSuiteAll {

}
