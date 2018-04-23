package org.bds.test;

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
		TestCasesVm.class, // Virtual machine
		TestCasesInterpolate.class, // Variable interpolation
		TestCasesFunctionDeclaration.class, // Function declaration 
		TestCasesLang.class, // Language (compiler)
		TestCasesRun.class, // Running bds code
		TestCasesRun2.class, // Running bds code
		TestCasesRun3.class, // Running bds code (classes / object)
		//		TestCasesGraph.class, // Running bds code: Task graphs and dependencies 
		//		TestCasesCheckpoint.class, // Running bds code: Checkpoint and recovery
		//		TestCasesTesting.class, //
		//		TestCasesCommandLineOptions.class, // 
		//		TestCasesClusterGeneric.class, // Running on a generic cluster
		//		TestCasesRemote.class, // Accessing remote data (cloud storage)
})
public class TestSuiteVm {

}
