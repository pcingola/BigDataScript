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
		TestCasesLang.class, // Language (compiler)
		TestCasesInterpolate.class, // Variable interpolation
		TestCasesExecutioners.class, // Task executioners
		TestCasesFunctionDeclaration.class, // Function declaration
		TestCasesRun.class, // Running bds code
		TestCasesRun2.class, // Running bds code
		TestCasesRun3.class, // Running bds code (classes / object)
		TestCasesGraph.class, // Running bds code: Task graphs and dependencies
		TestCasesCheckpoint.class, // Running bds code: Checkpoint and recovery
		TestCasesTesting.class, // Check bds unit testing system
		TestCasesCommandLineOptions.class, // Check command line options
		TestCasesClusterGeneric.class, // Running on a generic cluster
		TestCasesClusterSsh.class, // Run on an 'ssh cluster'
		TestCasesRemote.class, // Accessing remote data (cloud storage)
		TestCasesReport.class, // Report generation
		TestCasesTaskImproper.class, // Improper tasks
		TestCasesTaskDetached.class, // Detached tasks
		TestCasesAws.class, // Executioned AWS
})
public class TestSuiteAll {

}
