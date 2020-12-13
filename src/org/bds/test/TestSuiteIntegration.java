package org.bds.test;

import org.bds.test.integration.TestCasesIntegrationAws;
import org.bds.test.integration.TestCasesIntegrationCheckpoint;
import org.bds.test.integration.TestCasesIntegrationClusterGeneric;
import org.bds.test.integration.TestCasesIntegrationClusterSsh;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Invoke all test cases
 *
 * @author pcingola
 */

@RunWith(Suite.class)
@SuiteClasses({ //
		TestCasesIntegrationCheckpoint.class, // Running bds code: Checkpoint and recovery
		TestCasesIntegrationAws.class, // Executioner AWS
		TestCasesIntegrationClusterGeneric.class, // Executoner Cluster Generic
		TestCasesIntegrationClusterSsh.class, // Executioner Ssh
})
public class TestSuiteIntegration {

}
