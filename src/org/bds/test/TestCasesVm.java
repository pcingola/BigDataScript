package org.bds.test;

import org.bds.util.Gpr;
import org.junit.Test;

/**
 * Test cases for language & compilation
 *
 * Note: These test cases just check language parsing and compilation (what is supposed to compile OK, and what is not).
 *
 * @author pcingola
 *
 */
public class TestCasesVm extends TestCasesBase {

	@Test
	public void test00() {
		Gpr.debug("Test");
		runVmAndCheck("test/vm00.asm", "a", "42");
	}

	@Test
	public void test01() {
		Gpr.debug("Test");
		runVmAndCheck("test/vm01.asm", "z", "8");
	}

}
