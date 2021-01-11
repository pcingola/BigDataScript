package org.bds.test.unit;

import org.bds.test.TestCasesBase;
import org.bds.util.Gpr;
import org.junit.Test;

import junit.framework.Assert;

/**
 * Test cases for reporting
 *
 * @author pcingola
 *
 */
public class TestCasesReport extends TestCasesBase {

	@Test
	public void test01_report() {
		Gpr.debug("Test");
		String report = runAndGetReport("test/report_01.bds", true);
		Assert.assertTrue("Yaml report doesn't have the expected 'tasksExecuted' entry", report.indexOf("tasksExecuted: 1") > 0);
		Assert.assertTrue("Yaml report doesn't have the expected 'tasksFailed' entry", report.indexOf("tasksFailed: 0") > 0);
	}

}
