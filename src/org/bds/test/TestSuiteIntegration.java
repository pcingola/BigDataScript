package org.bds.test;

import org.bds.test.integration.TestCasesIntegrationAws;
import org.bds.test.integration.TestCasesIntegrationCheckpoint;
import org.bds.test.integration.TestCasesIntegrationClusterGeneric;
import org.bds.test.integration.TestCasesIntegrationClusterSsh;
import org.bds.test.integration.TestCasesIntegrationRemote;
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
		TestCasesIntegrationClusterGeneric.class, // Executoner Cluster Generic
		TestCasesIntegrationClusterSsh.class, // Executioner Ssh
		TestCasesIntegrationRemote.class, // Remote files: S3, HTTP, FTP
		TestCasesIntegrationAws.class, // Executioner AWS
		TestCasesIntegrationCheckpoint.class, // Running bds code: Checkpoint and recovery from S3
})
public class TestSuiteIntegration {

}
